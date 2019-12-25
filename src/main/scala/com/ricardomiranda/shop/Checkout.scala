package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import com.ricardomiranda.shop.ProductsOnSale._


case class Checkout(products: Products) extends StrictLogging 

object Checkout {
  def apply(configurationFilePath: String): Checkout = {
    new Checkout(products = ProductsOnSale.convertConfigFileContentsToObject(configurationFilePath = configurationFilePath))
  }
}