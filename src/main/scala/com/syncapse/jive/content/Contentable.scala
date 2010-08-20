package com.syncapse.jive.content

import com.jivesoftware.community.JiveContentObject
import org.joda.time.DateTime
import com.jivesoftware.base.User

trait Contentable extends JiveContentObject with JiveObjectAble {

  var plainSubject = ""
  var subject = ""
  var jiveObjectType = null
  var containerID = 0L
  var contentType = 0
  var containerType = 0L
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