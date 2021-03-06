package com.syncapse.jive.auth

import org.acegisecurity.AuthenticationManager
import org.springframework.beans.factory.annotation.Required


trait AuthenticationManagerComponent {

  protected var authenticationManager: AuthenticationManager = _

  @Required
  def setAuthenticationManager(a: AuthenticationManager) = {
    authenticationManager = a
  }

}