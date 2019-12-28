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

  override def computeBill: (Double, Long) => Double = (quantity, price) => {
    val subtotal: Double = quantity * price
    logger.info(s"Subtotal for code ${code} is ${subtotal%2.2f} Euro (${quantity} units at ${price%2.2f} Euro)")
    subtotal
  }
}

case class TwoForOne(code: String) extends Billing with StrictLogging {

  override def computeBill: (Double, Long) => Double = (quantity, price) => {
    val subtotal: Double = (quantity / 2) * price
    logger.info(s"Subtotal for code ${code} is ${subtotal%2.2f} Euro (${quantity} units at ${price%2.2f} Euro)")
    subtotal
  }
}


object BillingFactory extends StrictLogging {

  /** Method that dynamically creates an object of the intended Billing type
   *
   * @param code The merchandise code
   * @param billingType The type of billing to do with for this code
   * @return A Billing object
   */
  def apply(code: String, billingType: String): Billing = {
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
}


