package com.syncapse.jive.event

import com.jivesoftware.clearspace.event.BaseEvent
import com.jivesoftware.base.event.v2.EventDispatcher
import com.jivesoftware.community.lifecycle.JiveApplication
import com.jivesoftware.community.{JiveEvent, JiveContext}

object JiveEventDispatcher {

  def fire[E <: JiveEvent](event: E) = {
    // finish implementing
//    JiveApplication.getContext.getEventDispatcher.fire(event)
    throw new UnsupportedOperationException("This is has not yet been implemented")
  }


}