package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import spray.json._
import DefaultJsonProtocol._


case class Products(products: Seq[Product])
case class Product(code: String, name: String, price: Double) 


case class Checkout(products: Products)

object Checkout extends StrictLogging {
  def apply(pricing_rules: String): Checkout = {
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
    logger.info(s"Converting config file - $configurationFilePath - to Products object")
    Core.getFileContents(filePath = configurationFilePath)
      .get
      .parseJson
      .convertTo[Products]
  }

  /** Method to create a Set[String] of the products on the Triggerise's shop
   * 
   * @param products the Seq of products on Triggerise's shop
   * @return Set[String] whith the code of products
   */
  def availableProducts(products: Seq[Product]): Set[String] = products.map(_.code.trim.toUpperCase).toSet

  /** Method that validates if a product is available on Triggerise's shop
   * 
   * @param product Code of product to be checked
   * @param products the Set[String] of products on Triggerise's shop
   * @return Boolean True if product is available for sale
   */
  def isValidProduct(product: String, products: Set[String]): Boolean = products.contains(product.trim.toUpperCase)
}