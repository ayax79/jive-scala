package com.syncapse.jive.event

import com.jivesoftware.community.lifecycle.JiveApplication
import com.jivesoftware.clearspace.event.BaseEvent

object JiveEventDispatcher {

  def fire[E <: BaseEvent](event: E) = {
    JiveApplication.getContext.getEventDispatcher.fire(event)
  }


}