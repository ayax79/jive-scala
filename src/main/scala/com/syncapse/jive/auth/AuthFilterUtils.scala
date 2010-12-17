package com.syncapse.jive.auth

import com.jivesoftware.community.JiveContext
import com.jivesoftware.community.lifecycle.JiveApplication
import javax.servlet.Filter
import collection.JavaConversions
import org.springframework.security.util.FilterChainProxy
import java.util.{Map => JMap, List => JList}
import com.syncapse.jive.Loggable

object AuthFilterUtils extends Loggable {

  def addPrePluginFilter(pattern: String, beanNames: String*) = JiveApplication.getContext match {
    case ctx: JiveContext =>
      val filterChainProxy = acquirePreFilterChainProxy(ctx)
      val chainMap: JMap[String, JList[Filter]] = filterChainProxy.getFilterChainMap.asInstanceOf[JMap[String, JList[Filter]]]
      val myFilterList = buildFilterList(ctx, beanNames.toList)
      logger.info("Adding to prePluginFilter list {} : {}", pattern, myFilterList)
      chainMap.put(pattern, JavaConversions.asJavaList(myFilterList))
    case _ => throw new IllegalArgumentException("No jive context could be obtained")
  }

  protected[auth] def acquirePreFilterChainProxy(context: JiveContext): FilterChainProxy =
    context.getSpringBean("pluginPreFilterChain").asInstanceOf[FilterChainProxy]


  protected[auth] def buildFilterList(ctx: JiveContext, beanNames: List[String]): List[Filter] =
    beanNames.map{
      b => ctx.getSpringBean(b).asInstanceOf[Filter]
    }


}