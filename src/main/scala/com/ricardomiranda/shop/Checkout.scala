package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import spray.json.DefaultJsonProtocol._
import spray.json._


case class Configuration(billingType: BillingType, products: Seq[Product])

case class Product(code: String, name: String, price: Double)

case class BillingType(regular: Seq[String], twoForOne: Seq[String], xOrMore: Seq[XOrMoreType])

case class XOrMoreType(code: String, promoPrice: Double, x: Long)

case class Checkout(billingCodes: Map[String, Billing] = Map(),
                    shoppingKart: Seq[String] = Seq(),
                    products: Seq[Product]) extends StrictLogging {

  /** Returns the Set[String] of products available ont the Triggerise's shop
   */
  lazy val availableProducts: Set[String] = this.products.map(_.code.trim.toUpperCase).toSet
  /** Returns a predicate wether a products is available in Triggerise's shop
   */
  val isValidProduct: String => Boolean = (product) => this.availableProducts.contains(product.trim.toUpperCase)

  /** Method to add an item to shopping list
   */
  def scan(item: String): Checkout = {
    this.isValidProduct(item) match {
      case true =>
        logger.info(s"Item ${item} added to shopping kart")
        this.copy(shoppingKart = item.trim.toUpperCase +: shoppingKart)
      case false =>
        logger.info(s"Item ${item} was not added to shopping kart")
        this
    }
  }

  /** Method to start a new bill
   *
   * @return Checkout Empties chopping kart so that a new billing process is possible
   */
  def resetShppingKart: Checkout = {
    logger.info(s"New empty shopping kart")
    this.copy(shoppingKart = Seq())
  }

  /** Method to print the account total
   *
   * @return Checkout Empties chopping kart so that a new billing process is possible
   */
  def total: Unit = println(f"total: ${this.calcTotal}%.2f")

  /** Method to compute the account total
   *
   * @return calcTotal
   */
  def calcTotal: Double = {
    val total: Double =
      this.distinctItemsInShoppingKart
        .map(x => this.billingCodes(x).computeBill(this.getCodePrice(code = x), this.countItemsSameCode(x)))
        .sum

    logger.info(f"Total is ${total}%.2f Euro")
    total
  }

  /** Method to count the number of items in a shopping kart of a specific code
   *
   * @param code
   * @return Number of items in a shopping kart of a specific code
   */
  def countItemsSameCode(code: String): Long = this.shoppingKart.count(_ == code)

  /** Method to find distinct codes in shopping kart
   *
   * @return Set of distinct codes in shopping kart
   */
  def distinctItemsInShoppingKart: Set[String] = this.shoppingKart.toSet

  /** Method to get the tag price of a code
   *
   * @param code
   * @return Tag price of a code
   */
  def getCodePrice(code: String): Double = {
    this.products.filter(_.code == code.trim.toUpperCase).headOption match {
      case Some(x) => x.price
      case None => 0.00
    }
  }
}

object Checkout extends StrictLogging {

  /** Checkout constructor from a configuration file
   *
   * @param pricing_rules The file path of the configuration.
   * @return Checkout
   */
  def apply(pricing_rules: String): Checkout = {
    def readBillingCodes(billingType: BillingType): Map[String, Billing] = {
      billingType.regular.map(x => (x, BillingFactory(billingType = "regular", code = x))).toMap ++
        billingType.twoForOne.map(x => (x, BillingFactory(billingType = "two_for_one", code = x))).toMap ++
        billingType.xOrMore
          .map(x => (
            x.code,
            BillingFactory(billingType = "x_or_more", code = x.code, promoPrice = x.promoPrice, x = x.x)
          ))
          .toMap
    }

    logger.info(s"Creating a Checkout object form config file - ${pricing_rules}")
    val configuration: Configuration = this.convertConfigFileContentsToObject(configurationFilePath = pricing_rules)
    new Checkout(
      billingCodes = readBillingCodes(billingType = configuration.billingType),
      products = configuration.products
    )
  }

  implicit val xOrMoreTypeFormat: RootJsonFormat[XOrMoreType] = jsonFormat3(XOrMoreType)
  implicit val billingTypeFormat: RootJsonFormat[BillingType] = jsonFormat3(BillingType)
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