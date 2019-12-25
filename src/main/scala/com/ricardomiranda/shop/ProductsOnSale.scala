package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import spray.json.DefaultJsonProtocol
import spray.json._


object ProductsOnSale extends DefaultJsonProtocol with StrictLogging {

  implicit val productFormat: RootJsonFormat[Product] = jsonFormat3(Product)
  implicit val productsFormat: RootJsonFormat[Products] = jsonFormat1(Products)

  case class Product(code: String, name: String, price: Double)

  case class Products(products: Seq[Product])

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

  def availableProducts(products: Seq[Product]): Set[String] = products.map(_.code.trim.toUpperCase).toSet

  def validProduct(product: String, products: Set[String]): Boolean = products.contains(product.trim.toUpperCase)
}
