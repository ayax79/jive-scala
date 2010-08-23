package com.syncapse.jive.content

import com.jivesoftware.community.JiveObject

trait JiveObjectAble extends JiveObject {
  var objectType = 0
  var id = 0L

  def getObjectType = objectType
  def getID = id
}