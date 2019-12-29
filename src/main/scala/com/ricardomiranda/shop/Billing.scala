package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging

sealed trait Billing {

  /** Method to compute the amount due relative to an item
   *
   * @return amount due relative to an item
   */
  def computeBill: (Double, Long) => Double
}

case class Regular(code: String) extends Billing with StrictLogging {

  override def computeBill: (Double, Long) => Double = (price, quantity) => {
    val subtotal: Double = quantity * price
    logger.info(s"Subtotal for code ${code} is ${subtotal%2.2f} Euro (${quantity} units at ${price%2.2f} Euro)")
    subtotal
  }
}

case class TwoForOne(code: String) extends Billing with StrictLogging {

  override def computeBill: (Double, Long) => Double = (price, quantity) => {
    val subtotal: Double = (quantity + 1) / 2 * price
    logger.info(s"Subtotal for code ${code} is ${subtotal%2.2f} Euro (${quantity} units at ${price%2.2f} Euro)")
    subtotal
  }
}

case class XOrMore(code: String, promoPrice: Double, x: Long) extends Billing with StrictLogging {

  override def computeBill: (Double, Long) => Double = (price, quantity) => {
    val subtotal: Double = quantity match {
      case q if q >= this.x => 
        q * this.promoPrice
      case q => 
        q * price
    }

    logger.info(s"Subtotal for code ${code} is ${subtotal%2.2f} Euro (${quantity} units at ${price%2.2f} Euro)")
    subtotal
  }
}

object BillingFactory extends StrictLogging {

  /** Method that dynamically creates an object of the intended Billing type
   *
   * @param billingType The type of billing to do with for this code
   * @param code The merchandise code
   * @return A Billing object
   */
  def apply(billingType: String, code: String): Billing = {
    billingType.trim.toLowerCase match {
      case "regular" =>
        logger.info(s"Code ${code} will be billed without any promotion")
        Regular(code = code.trim.toUpperCase)
      case "two_for_one" =>
        logger.info(s"Code ${code} will be billed 2-for-1 promotion")
        TwoForOne(code = code.trim.toUpperCase)
      case _ =>
        logger.error(s"Code ${code} does not have a known billing type")
        sys.exit(1)
    }
  }

  /** Method that dynamically creates an object of the intended Billing type
   *
   * @param billingType The type of billing to do with for this code
   * @param code The merchandise code
   * @param promoPrice The promotional price
   * @param x The minimum quantity to have the promotional price
   * @return A Billing object
   */
  def apply(
    billingType: String, 
    code: String,
    promoPrice: Double,
    x: Long): Billing = {
    billingType.trim.toLowerCase match {
      case "x_or_more" =>
        logger.info(s"Code ${code} will be billed without any promotion")
        XOrMore(code = code.trim.toUpperCase, promoPrice = promoPrice, x = x)
      case _ =>
        logger.error(s"Code ${code} does not have a known billing type")
        sys.exit(1)
    }
  }
}


