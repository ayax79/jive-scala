package com.syncapse.jive.content

import java.io.InputStream
import java.lang.String
import com.jivesoftware.community.impl.DbImage
import com.jivesoftware.community.{ImageException, Image, ImageContentResource}
import com.jivesoftware.community.event.ImageEvent
import java.util.{Collections}
import actors.Actor
import com.syncapse.jive.event.JiveEventDispatcher

trait Imageable extends ImageContentResource with JiveObjectAble {
  protected case class DeleteImage(image: DbImage)

  protected object ImageActor extends Actor {
    var images: List[Long] = List.empty


    def act() = loop {
      react {
        case DeleteImage(i) => {
          images = images - i.getID
          fireImageEvent(image, ImageEvent.Type.DELETED)
        }
      }
    }
  }

  def deleteImage(image: Image) = {
    ImageActor ! DbImage(image)
  }

  def addImage(image: Image) = {}

  def createImage(p1: String, p2: String, p3: InputStream) = null

  def getImage(imageId: Long) = null

  def getImages = null

  def getImageCount = 0

  protected def asDbImage(image: Image): DbImage =
    if (image.isInstanceOf[DbImage]) {
      dbImage = image.asInstanceOf[DbImage]
    }
    else {
      try {
        dbImage = getImage(image.getID).asInstanceOf[DbImage]
      }
      catch {
        case e: Exception =>
          throw new ImageException(ImageException.GENERAL_ERROR, e)
      }
    }

  protected def fireImageEvent(image: Image, imageType: ImageEvent.Type) {
    JiveEventDispatcher.fire(new ImageEvent(imageType, this, image, Collections.emptyMap))
  }
}