package com.syncapse.jive.auth

import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import org.junit.Test
import com.jivesoftware.community.JiveContext
import org.mockito.Mockito._
import org.mockito.Mock
import junit.framework.Assert._
import javax.servlet.Filter
import org.springframework.security.util.FilterChainProxy

@RunWith(classOf[MockitoJUnitRunner])
class AuthFilterUtilsTest {

  val filter1 = "mockFilter1"
  val filter2 = "mockFilter2"

  @Mock var jiveContext: JiveContext = _
  @Mock("mockFilter1") var mockFilter1: Filter = _
  @Mock("mockFilter2") var mockFilter2: Filter = _

  @Test
  def testBuildFilterList = {


    when(jiveContext.getSpringBean(filter1)).thenReturn(mockFilter1)
    when(jiveContext.getSpringBean(filter2)).thenReturn(mockFilter2)

    val result = AuthFilterUtils.buildFilterList(jiveContext, List(filter1, filter2))
    assertTrue(result.contains(mockFilter1))
    assertTrue(result.contains(mockFilter2))
  }

  @Test
  def addPrePluginFilter = {
    val fpb = new FilterChainProxy
    fpb.setFilterChainMap()




  }

}