package com.syncapse.jive.event

import com.jivesoftware.community.JiveEvent

object JiveEventDispatcher {

  def fire[E <: JiveEvent](event: E) = {
    // finish implementing
//    JiveApplication.getContext.getEventDispatcher.fire(event)
    throw new UnsupportedOperationException("This is has not yet been implemented")
  }


}