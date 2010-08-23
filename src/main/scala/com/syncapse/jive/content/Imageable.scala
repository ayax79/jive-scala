package com.syncapse.jive.content

import java.io.InputStream
import java.lang.String
import com.jivesoftware.community.impl.DbImage
import com.jivesoftware.community.{ImageException, Image, ImageContentResource}
import com.jivesoftware.community.event.ImageEvent
import actors.Actor
import com.syncapse.jive.event.JiveEventDispatcher
import java.util.{HashMap, Collections, Map => JMap}

trait Imageable extends ImageContentResource with JiveObjectAble {
  protected case class DeleteImage(image: DbImage)

  protected object ImageActor extends Actor {
    var images: List[Long] = List.empty


    def act() = loop {
      react {
        case DeleteImage(i) => {
          images = images - i.getID
          fireImageEvent(i, ImageEvent.Type.DELETED)
        }
      }
    }
  }

  def deleteImage(image: Image) = {
    ImageActor ! DeleteImage(asDbImage(image))
  }

  def addImage(image: Image) = {}

  def createImage(p1: String, p2: String, p3: InputStream) = null

  def getImage(imageId: Long) = null

  def getImages = null

  def getImageCount = 0

  protected def asDbImage(image: Image): DbImage =
    if (image.isInstanceOf[DbImage]) {
      image.asInstanceOf[DbImage]
    }
    else {
      try {
        getImage(image.getID).asInstanceOf[DbImage]
      }
      catch {
        case e: Exception =>
          throw new ImageException(ImageException.GENERAL_ERROR, e)
      }
    }

  protected def fireImageEvent(image: Image, imageType: ImageEvent.Type) {
    val params: JMap[java.lang.String, _ <: java.lang.Object] = new HashMap
    JiveEventDispatcher.fire(new ImageEvent(imageType, this, image, params))
  }
}