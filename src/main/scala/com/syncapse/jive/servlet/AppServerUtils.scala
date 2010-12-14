package com.syncapse.jive.servlet

import org.apache.catalina.core.{StandardContext, ApplicationContextFacade}
import javax.management.ObjectName
import javax.servlet.ServletContext
import com.syncapse.jive.Loggable
import org.apache.catalina.deploy.{FilterMap, FilterDef}

trait AppServerWrapper {
  def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String)
}

object AppServerUtils extends AppServerWrapper with Loggable {

  def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String) = determineAppServer(servletContext) match {
    case Some(asw) => asw.registerFilter(servletContext, filterName, filterClass, filterMapping)
    case None => throw new IllegalArgumentException("Could not determine app server")
  }

  protected def determineAppServer(ctx: ServletContext): Option[AppServerWrapper] = ctx match {
    case acf: ApplicationContextFacade => Some(TomcatWrapper.asInstanceOf[AppServerWrapper])
    case _ => None
  }

  protected object TomcatWrapper extends AppServerWrapper {
    def registerFilter(servletContext: ServletContext, filterName: String, filterClass: String, filterMapping: String) = {
      val field = classOf[ApplicationContextFacade].getField("context")
      field.setAccessible(true)

      field.get("context") match {
        case sc: StandardContext => handleStandardContext(sc, filterName, filterClass, filterMapping)
        case _ => logger.warn("Context is of an unown type")
      }
    }

    protected def checkServletExists(children: List[ObjectName], nm: String): Boolean = children match {
      case Nil => false
      case head :: tail if (head.getCanonicalName == nm) => true
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