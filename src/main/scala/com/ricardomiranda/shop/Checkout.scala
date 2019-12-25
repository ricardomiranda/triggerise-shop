package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import spray.json._
import DefaultJsonProtocol._


case class Products(products: Seq[Product])
case class Product(code: String, name: String, price: Double) 


case class Checkout(items: Seq[String] = Seq(), products: Products) {

  /** Method to add an item to shopping list
   */
  def scan(item: String): Checkout = {
    this.isValidProduct(item) match {
      case true => this.copy(items = item +: items)
      case false => this
    }
  }

  /** Method to compute the account total
   */
  // def total: Double = ???

  /** Returns the Set[String] of products available ont the Triggerise's shop
   */
  val availableProducts: Set[String] = this.products.products.map(_.code.trim.toUpperCase).toSet


  /** Returns a predicate wether a products is available in Triggerise's shop
   */
  val isValidProduct: String => Boolean = (product) => this.availableProducts.contains(product.trim.toUpperCase)
}

object Checkout extends StrictLogging {

  /** Checkout constructor from a configuration file
    *
    * @param pricing_rules The file path of the configuration.
    * @return Checkout
    */
  def apply(pricing_rules: String): Checkout = {
    logger.info(s"Creating a Checkout object form config file - ${pricing_rules}")
    new Checkout(products = this.convertConfigFileContentsToObject(configurationFilePath = pricing_rules))
  }

  implicit val productFormat: RootJsonFormat[Product] = jsonFormat3(Product)
  implicit val productsFormat: RootJsonFormat[Products] = jsonFormat1(Products)

  /** Method to convert the contents of a configuration file into an object of type Products
    *
    * @param configurationFilePath The file path of the configuration.
    * @return Some(Products) if the file path exists or None if not.
    */
  def convertConfigFileContentsToObject(configurationFilePath: String): Products = {
    logger.info(s"Converting config file - ${configurationFilePath} - to Products object")
    Core.getFileContents(filePath = configurationFilePath)
      .get
      .parseJson
      .convertTo[Products]
  }
}