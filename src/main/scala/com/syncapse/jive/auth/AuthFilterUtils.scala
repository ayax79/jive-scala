package com.syncapse.jive.auth

import com.jivesoftware.community.JiveContext
import com.jivesoftware.community.lifecycle.JiveApplication
import org.acegisecurity.intercept.web.FilterInvocationDefinitionMap
import org.acegisecurity.{SecurityConfig, ConfigAttributeDefinition}
import org.acegisecurity.util.FilterChainProxy

object AuthFilterUtils {

  def addFilterPattern(pattern: String, beanNames: String*) = JiveApplication.getContext match {
    case ctx: JiveContext =>
      acquireFilterChainProxy(ctx).getFilterInvocationDefinitionSource match {
        case f: FilterInvocationDefinitionMap =>


          val cad = new ConfigAttributeDefinition
          beanNames.foreach{
            b => cad.addConfigAttribute(new SecurityConfig(b))
          }
          f.addSecureUrl(pattern, cad)
        case _ => throw new IllegalArgumentException("Filter Invocation map not of correct type")
      }
    case _ => throw new IllegalArgumentException("No jive context could be obtained")
  }

  protected def acquireFilterChainProxy(context: JiveContext): FilterChainProxy =
    context.getSpringBean("filterChainProxy").asInstanceOf[FilterChainProxy]

}