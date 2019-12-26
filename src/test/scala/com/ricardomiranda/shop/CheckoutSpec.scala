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
      val products: Products = Products(products = testProductsEmpty.products)
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set()
      assert(expected.equals(actual))
    }

    "create an sequence with 1 element from a unitary sequence of Products" in {
      val products: Products = Products(products = testProducts1.products)
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set("TICKET")
      assert(expected.equals(actual))
    }

    "create an sequence with 3 elements from a sequence of 3 Products" in {
      val products: Products = Products(products = testProducts3.products)
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set("TICKET", "HAT", "HOODIE")
      assert(expected.equals(actual))
    }
  }

  "validProduct" should {
    "Be false if both empty" in {
      val product: String = ""
      val products: Products = Products(products = testProductsEmpty.products)
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean =  checkout.isValidProduct(product)
       assert(!actual)
    }

    "Be false if Triggerise's shop has no products" in {
      val product: String = "HAT"
      val products: Products = Products(products = testProductsEmpty.products)
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean =  checkout.isValidProduct(product)
      assert(!actual)       
    }

    "Be false if Triggerise's shop has other products" in {
      val product: String = "HAT"
      val products: Products = Products(products = testProducts1.products)
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean =  checkout.isValidProduct(product)
      assert(!actual)       
    }

    "Be true if Triggerise's shop has the product" in {
      val product: String = "HAT"
      val products: Products = Products(products = testProducts3.products)
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean =  checkout.isValidProduct(product)
      println(checkout.products.products)
      println(product)
      assert(actual)       
    }
  }

  "Checkout constructor" should {
    "Create a checkout object with no products from empty data file" in {
      val actual: Seq[Product] = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).products.products
      val expected: Seq[Product] = testProductsEmpty.products
      assert(expected.equals(actual))
    }

    "Create a checkout object with 1 product from data file with 1 product" in {
      val actual: Seq[Product] = Checkout(pricing_rules = getClass.getResource("/products_1.json").getPath).products.products
      val expected: Seq[Product] = testProducts1.products
      assert(expected.equals(actual))
    }

    "Create a checkout object with 2 products from data file with 2 products" in {
      val actual: Seq[Product] = Checkout(pricing_rules = getClass.getResource("/products_2.json").getPath).products.products
      val expected: Seq[Product] = testProducts2.products
      assert(expected.equals(actual))
    }

    "Create a checkout object with 3 products from data file with 3 products" in {
      val actual: Seq[Product] = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).products.products
      val expected: Seq[Product] = testProducts3.products
      assert(expected.equals(actual))
    }
  }

  "scan" should {
    "Add no item to shopping cart if shop has no products and item is empty String" in {
      val item: String = ""
      val products: Products = Products(products = testProductsEmpty.products)
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add no item to shopping cart if shop has no products" in {
      val item: String = "HAT"
      val products: Products = Products(products = testProductsEmpty.products)
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add one item to shopping cart if shop has that product" in {
      val item: String = "TICKET"
      val products: Products = Products(products = testProducts1.products)
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq(item)
      assert(expected.equals(actual))
    }

    "Add no item to shopping cart if shop does not have that product" in {
      val item: String = "HAT"
      val products: Products = Products(products = testProducts1.products)
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add four items to shopping cart" in {
      val ticket: String = "TICKET"
      val hat: String = "HAT"
      val products: Products = Products(products = testProducts3.products)
      val checkout: Checkout = Checkout(products = products).scan(ticket).scan(hat).scan(hat).scan(ticket)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq(ticket, hat, hat, ticket)
      assert(expected.equals(actual))
    }
  }
}