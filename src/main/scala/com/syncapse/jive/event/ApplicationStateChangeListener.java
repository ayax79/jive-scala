package com.syncapse.jive.event;

import com.jivesoftware.base.event.v2.EventListener;
import com.jivesoftware.community.lifecycle.ApplicationStateChangeEvent;

/**
 * Useful class that circumvents some annoying type issues I found when trying to set this up from the scala end
 */
public abstract class ApplicationStateChangeListener implements EventListener<ApplicationStateChangeEvent> {

}
