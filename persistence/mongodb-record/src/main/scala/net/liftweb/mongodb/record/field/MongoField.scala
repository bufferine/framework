/*
 * Copyright 2011 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftweb
package mongodb
package record
package field

import common.{Box, Empty}
import http.js.JE.{JsNull, JsRaw}
import json.Printer
import json.JsonAST._

import net.liftweb.record.BaseField

import com.mongodb.DBObject

/*
 * Trait that allows for saving the field with a name that is
 * different than the field name
 */
trait MongoField {
  this: BaseField =>
  /**
   * Return Full(name) to use that name in the encoded Mongo object, or
   * Empty to use the same name as in Scala. Defaults to Empty.
   */
  def mongoName: Box[String] = Empty
}

/**
* Describes common aspects related to Mongo fields
*/
trait MongoFieldFlavor[MyType] {
  this: BaseField =>
  /*
  * convert this field's value into a DBObject so it can be stored in Mongo.
  */
  def asDBObject: DBObject

  // set this field's value using a DBObject returned from Mongo.
  def setFromDBObject(obj: DBObject): Box[MyType]

  /**
  * Returns the field's value as a valid JavaScript expression
  */
  def asJs = asJValue match {
    case JNothing => JsNull
    case jv => JsRaw(Printer.compact(render(jv)))
  }

  /** Encode the field value into a JValue */
  def asJValue: JValue

}
