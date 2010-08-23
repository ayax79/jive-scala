package com.syncapse.jive.content

import org.joda.time.DateTime
import com.jivesoftware.base.User
import com.jivesoftware.community.{JiveContentObject}
import com.jivesoftware.community.objecttype.JiveObjectType

trait Contentable extends JiveContentObject with JiveObjectAble {

  var plainSubject = ""
  var subject = ""
  var jiveObjectType: JiveObjectType = null
  var containerID: Long = 0L
  var contentType: Int = 0
  var containerType: Int = 0
  var status = null
  var unfilteredSubject = ""
  var modificationDate : DateTime = null
  var creationDate : DateTime = null
  var plainBody = null
  var body = null
  var authors : List[User]
  var user = null

  def getPlainSubject = plainSubject

  def getSubject = subject

  def getJiveObjectType = jiveObjectType

  def getContainerType = containerType

  def getContainerID = containerID

  def getStatus = status

  def getUnfilteredSubject = unfilteredSubject

  def getModificationDate = modificationDate.toDate

  def getCreationDate = creationDate.toDate 

  def getPlainBody = plainBody

  def getBody = body

  def getAuthors = null

  def getUser = user
  
}