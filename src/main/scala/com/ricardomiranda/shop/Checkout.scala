package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import spray.json._
import DefaultJsonProtocol._


case class Configuration(billingType: BillingType, products: Seq[Product])
case class Product(code: String, name: String, price: Double)
case class BillingType(regular: Seq[String])


case class Checkout(items: Seq[String] = Seq(), products: Seq[Product]) extends StrictLogging {

  /** Method to add an item to shopping list
   */
  def scan(item: String): Checkout = {
    this.isValidProduct(item) match {
      case true =>
        logger.info(s"Item ${item} added to shopping kart")
        this.copy(items = item.toUpperCase +: items)
      case false =>
        logger.info(s"Item ${item} was not added to shopping kart")
        this
    }
  }

  /** Method to count the number of items in a shopping kart of a specific code
   *
   * @param code
   * @return Number of items in a shopping kart of a specific code
   */
  def countItemsSameCode(code: String): Long = this.items.count(_ == code)

  /** Method to find distinct codes in shopping kart
   *
   * @return Set of distinct codes in shopping kart
   */
  def distinctItemsInShoppingKart: Set[String] = this.items.toSet

  /** Method to start a new bill
   *
   * @return Checkout Empties chopping kart so that a new billing process is possible
   */
  def resetBasket: Checkout = {
    logger.info(s"New empty shopping kart")
    this.copy(items = Seq())
  }

  /** Method to compute the account total
   */
  // def total: Double = ???

  /** Returns the Set[String] of products available ont the Triggerise's shop
   */
  lazy val availableProducts: Set[String] = this.products.map(_.code.trim.toUpperCase).toSet


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
    new Checkout(products = this.convertConfigFileContentsToObject(configurationFilePath = pricing_rules).products)
  }

  implicit val billingTypeFormat: RootJsonFormat[BillingType] = jsonFormat1(BillingType)
  implicit val productFormat: RootJsonFormat[Product] = jsonFormat3(Product)
  implicit val configurationFormat: RootJsonFormat[Configuration] = jsonFormat2(Configuration)

  /** Method to convert the contents of a configuration file into an object of type Products
    *
    * @param configurationFilePath The file path of the configuration.
    * @return Some(Products) if the file path exists or None if not.
    */
  def convertConfigFileContentsToObject(configurationFilePath: String): Configuration = {
    logger.info(s"Converting config file - ${configurationFilePath} - to Products object")
    Core.getFileContents(filePath = configurationFilePath)
      .get
      .parseJson
      .convertTo[Configuration]
  }
}