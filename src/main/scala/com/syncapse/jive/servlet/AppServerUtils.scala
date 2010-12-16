package com.syncapse.jive.servlet

import javax.management.ObjectName
import javax.servlet.ServletContext
import com.syncapse.jive.Loggable
import org.apache.catalina.deploy.{FilterMap, FilterDef}
import org.apache.catalina.core.{ApplicationContext, StandardContext, ApplicationContextFacade}
import java.lang.reflect.Field

trait AppServerWrapper {
  def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String, cl: ClassLoader)
}

object AppServerUtils extends AppServerWrapper with Loggable {

  def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String, cl: ClassLoader)
    = determineAppServer(servletContext) match {
    case Some(asw) => asw.registerFilter(servletContext, filterName, filterClass, filterMapping, cl)
    case None => throw new IllegalArgumentException("Could not determine app server")
  }

  protected def determineAppServer(ctx: ServletContext): Option[AppServerWrapper] = ctx match {
    case acf: ApplicationContextFacade => Some(TomcatWrapper.asInstanceOf[AppServerWrapper])
    case _ => None
  }

  protected object TomcatWrapper extends AppServerWrapper {
    def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String, cl: ClassLoader) = {
      def getField[T](nm: String, c: Class[T]): Option[Field] = c.getDeclaredFields.toList.find{
        p => p.getName == nm
      }
      val oldLoader = Thread.currentThread.getContextClassLoader
      Thread.currentThread.setContextClassLoader(cl)


      getField("context", classOf[ApplicationContextFacade]) match {
        case Some(field) =>
          field.setAccessible(true)
          field.get(servletContext) match {
            case sc: StandardContext => handleStandardContext(sc, filterName, filterClass, filterMapping)
            case ac: ApplicationContext => getField("context", ac.getClass) match {
              case Some(field2) =>
                field2.setAccessible(true)
                field2.get(ac) match {
                  case sc2: StandardContext => handleStandardContext(sc2, filterName, filterClass, filterMapping)
                  case _ => logger.warn("Context is of an unknown type")
                }
              case None => logger.warn("could not find the context field")
            }
            case _ => logger.warn("Context is of an unknown type")
          }
        case None => logger.warn("Could not find the context field")
      }

      Thread.currentThread.setContextClassLoader(oldLoader)
    }

    protected def checkServletExists(children: List[ObjectName], nm: String): Boolean = children match {
      case Nil => false
      case head :: tail if (head != null && head.getCanonicalName == nm) => true
      case head :: tail => checkServletExists(tail, nm)
    }

    protected def handleStandardContext(sc: StandardContext, filterName: String, filterClass: String, filterMapping: String) = checkServletExists(sc.getChildren.toList, filterName) match {
      case false if (sc.findFilterDef(filterName) == null) =>
        sc.addFilterDef(buildFilterDef(filterName, filterClass))
        sc.addFilterMap(buildFilterMap(filterName, filterMapping))
      case _ => logger.warn(String.format("Filter %s has already been deployed", filterClass))
    }


    protected def buildFilterDef(filterName: String, filterClass: String): FilterDef = {
      val fd = new FilterDef
      fd.setFilterName(filterName)
      fd.setDisplayName(filterName)
      fd.setFilterClass(filterClass)
      fd
    }

    protected def buildFilterMap(filterName: String, filterMapping: String): FilterMap = {
      val fm = new FilterMap
      fm.setFilterName(filterName)
      fm.setDispatcher(filterMapping)
      fm
    }
  }


}