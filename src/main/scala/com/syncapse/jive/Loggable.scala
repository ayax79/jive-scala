package com.syncapse.jive

import org.slf4j.{LoggerFactory, Logger}

trait Loggable {

  lazy val logger: Logger = LoggerFactory.getLogger(getClass) 

}