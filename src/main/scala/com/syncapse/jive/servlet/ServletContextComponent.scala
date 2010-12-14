package com.syncapse.jive.servlet

import org.springframework.web.context.ServletContextAware
import javax.servlet.ServletContext

trait ServletContextComponent extends ServletContextAware {

  protected var servletContext: ServletContext = null

  def setServletContext(p1: ServletContext) = {
    servletContext = p1
  }

}