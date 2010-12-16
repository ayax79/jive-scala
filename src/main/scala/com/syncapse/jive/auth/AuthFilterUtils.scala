package com.syncapse.jive.auth

import com.jivesoftware.community.JiveContext
import com.jivesoftware.community.lifecycle.JiveApplication
import java.util.{Map => JMap, List => JList}
import javax.servlet.Filter
import collection.JavaConversions
import org.acegisecurity.util.FilterChainProxy

object AuthFilterUtils {

  protected val DEFAULT_PATTERN = "/**"

  def addFilterPattern(pattern: String, beanNames: String*) = JiveApplication.getContext match {
    case ctx: JiveContext =>
      val filterChainProxy = acquireFilterChainProxy(ctx)
      val chainMap = filterChainProxy.getFilterChainMap.asInstanceOf[JMap[String, JList[Filter]]]
      val defaultFilterChain = chainMap.remove(DEFAULT_PATTERN).asInstanceOf[JList[Filter]]
      val myChain = buildFilterList(ctx, beanNames.toList)
      chainMap.put(pattern, myChain)
      chainMap.put(DEFAULT_PATTERN, defaultFilterChain)
    case _ => throw new IllegalArgumentException("No jive context could be obtained")
  }

  protected def acquireFilterChainProxy(context: JiveContext): FilterChainProxy =
    context.getSpringBean("filterChainProxy").asInstanceOf[FilterChainProxy]


  protected def buildFilterList(ctx: JiveContext, beanNames: List[String]): JList[Filter] =
    JavaConversions.asJavaList(beanNames.map{
      b => ctx.getSpringBean(b)
    })


}