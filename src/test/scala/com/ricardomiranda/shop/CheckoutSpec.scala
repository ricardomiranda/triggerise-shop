package com.ricardomiranda.shop

import org.scalatest.{Matchers, WordSpec}


class CheckoutSpec extends WordSpec with Matchers {
  val testProductsEmpty: Products = Products(
    products = Seq()
  )

  val testProducts1:Products = Products(
    products = Seq(
      Product(
        code = "TICKET",
        name = "Triggerise Ticket",
        price = 5.00
        )
    )
  ) 

  val testProducts2:Products = Products(
    products = Seq(
      Product(
        code = "TICKET",
        name = "Triggerise Ticket",
        price = 5.00
        ),
      Product(
        code = "HOODIE",
        name = "Triggerise Hoodie",
        price = 20.00
      )
    )
  ) 

  val testProducts3:Products = Products(
    products = Seq(
      Product(
        code = "TICKET",
        name = "Triggerise Ticket",
        price = 5.00
        ),
      Product(
        code = "HOODIE",
        name = "Triggerise Hoodie",
        price = 20.00
      ),
      Product(
        code = "HAT",
        name = "Triggierse Hat",
        price = 7.50
      )
    )
  ) 

  "convertConfigFileContentsToObject" should {
    "convert an existing json config file with no products to Products object with no products" in {
      val actual: Products = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_empty.json").getPath)

      val expected: Products = testProductsEmpty
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 1 product to Products object with 1 product" in {
      val actual: Products = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_1.json").getPath)

      val expected: Products = testProducts1
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 2 products to Products object with 2 products" in {
      val actual: Products = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_2.json").getPath)

      val expected: Products = testProducts2
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 3 products to Products object with 3 products" in {
      val actual: Products = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_3.json").getPath)

      val expected: Products = testProducts3
      assert(expected.equals(actual))
    }
  }

  "availableProducts" should {
    "create an empty set with a empty sequence of Products" in {
      val actual: Set[String] = Checkout.availableProducts(products = testProductsEmpty.products)

      val expected: Set[String] = Set()
      assert(expected.equals(actual))
    }

    "create an sequence with 1 element from a unitary sequence of Products" in {
      val actual: Set[String] = Checkout.availableProducts(products = testProducts1.products)

      val expected: Set[String] = Set("TICKET")
      assert(expected.equals(actual))
    }

    "create an sequence with 3 elements from a sequence of 3 Products" in {
      val actual: Set[String] = Checkout.availableProducts(products = testProducts3.products)

      val expected: Set[String] = Set("TICKET", "HAT", "HOODIE")
      assert(expected.equals(actual))
    }
  }

   "validProduct" should {
     "Be false if both empty" in {
       val product: String = ""
       val validProducts: Set[String] =  Checkout.availableProducts(products = testProductsEmpty.products)
       val actual: Boolean =  Checkout.isValidProduct(product = product, products = validProducts)
       assert(!actual)
     }

     "Be false if Triggerise's shop has no products" in {
       val product: String = "HAT"
       val validProducts: Set[String] =  Checkout.availableProducts(products = testProductsEmpty.products)
       val actual: Boolean =  Checkout.isValidProduct(product = product, products = validProducts)
       assert(!actual)       
     }

     "Be false if Triggerise's shop has other products" in {
       val product: String = "HAT"
       val validProducts: Set[String] =  Checkout.availableProducts(products = testProducts1.products)
       val actual: Boolean =  Checkout.isValidProduct(product = product, products = validProducts)
       assert(!actual)       
     }

     "Be true if Triggerise's shop has the product" in {
       val product: String = "HAT"
       val validProducts: Set[String] =  Checkout.availableProducts(products = testProducts3.products)
       val actual: Boolean =  Checkout.isValidProduct(product = product, products = validProducts)
       assert(actual)       
     }
   }
}