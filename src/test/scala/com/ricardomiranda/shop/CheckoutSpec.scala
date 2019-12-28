package com.ricardomiranda.shop

import org.scalatest.{Matchers, WordSpec}


class CheckoutSpec extends WordSpec with Matchers {
  val testProductsEmpty: (Seq[Product], BillingType) = (
    Seq(),
    BillingType(
      regular = Seq(),
      twoForOne = Seq()
      )
  )

  val testProducts1: (Seq[Product], BillingType) = (
    Seq(
      Product(
        code = "TICKET",
        name = "Triggerise Ticket",
        price = 5.00
      )
    ),
    BillingType(
      regular = Seq(),
      twoForOne = Seq(
        "TICKET"
      )
    )
  )

  val testProducts2: (Seq[Product], BillingType) = (
    Seq(
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
    ),
    BillingType(
      regular = Seq(
        "HOODIE"
      ),
      twoForOne = Seq(
        "TICKET"
      )
    )
  )

  val testProducts3: (Seq[Product], BillingType) = (
    Seq(
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
    ),
    BillingType(
      regular = Seq(
        "HOODIE",
        "HAT"
      ),
      twoForOne = Seq(
        "TICKET"
      )
    )
  )

  val testCheckoutProductsEmpty: (Seq[Product], Map[String, Billing]) = (
    Seq(),
    Map()
  )

  val testCheckoutProducts1: (Seq[Product], Map[String, Billing]) = (
    Seq(
      Product(
        code = "TICKET",
        name = "Triggerise Ticket",
        price = 5.00
      )
    ),
    Map(
      "TICKET" -> TwoForOne(code = "TICKET")
    )
  )

  val testCheckoutProducts2: (Seq[Product], Map[String, Billing]) = (
    Seq(
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
    ),
    Map(
      "TICKET" -> TwoForOne(code = "TICKET"),
      "HOODIE" -> Regular(code = "HOODIE")
    )
  )

  val testCheckoutProducts3: (Seq[Product], Map[String, Billing]) = (
    Seq(
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
    ),
    Map(
      "TICKET" -> TwoForOne(code = "TICKET"),
      "HOODIE" -> Regular(code = "  Hoodie"),
      "HAT" -> Regular("hat   ")
    )
  )  

  "convertConfigFileContentsToObject" should {
    "convert an existing json config file with no products to Products object with no products" in {
      val configuration: Configuration = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_empty.json").getPath)
      val actual: (Seq[Product], BillingType) = (configuration.products, configuration.billingType)
      val expected: (Seq[Product], BillingType) = testProductsEmpty
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 1 product to Products object with 1 product" in {
      val configuration: Configuration = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_1.json").getPath)
      val actual: (Seq[Product], BillingType) = (configuration.products, configuration.billingType)
      val expected: (Seq[Product], BillingType) = testProducts1
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 2 products to Products object with 2 products" in {
      val configuration: Configuration = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_2.json").getPath)
      val actual: (Seq[Product], BillingType) = (configuration.products, configuration.billingType)
      val expected: (Seq[Product], BillingType) = testProducts2
      assert(expected.equals(actual))
    }

    "convert an existing json config file with 3 products to Products object with 3 products with regular billing" in {
      val configuration: Configuration = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_3.json").getPath)
      val actual: (Seq[Product], BillingType) = (configuration.products, configuration.billingType)
      val expected: (Seq[Product], BillingType) = testProducts3
      assert(expected.equals(actual))
    }
  }
   
  "availableProducts" should {
    "create an empty set with a empty sequence of Products" in {
      val (products: Seq[Product], _) = testProductsEmpty
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set()
      assert(expected.equals(actual))
    }

    "create an sequence with 1 element from a unitary sequence of Products" in {
      val (products: Seq[Product], _) = testProducts1
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set("TICKET")
      assert(expected.equals(actual))
    }

    "create an sequence with 3 elements from a sequence of 3 Products" in {
      val (products: Seq[Product], _) = testProducts3
      val actual: Set[String] = Checkout(products = products).availableProducts
      val expected: Set[String] = Set("TICKET", "HAT", "HOODIE")
      assert(expected.equals(actual))
    }
  }

  "validProduct" should {
    "Be false if both empty" in {
      val product: String = ""
      val (products: Seq[Product], _) = testProductsEmpty
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean = checkout.isValidProduct(product)
      assert(!actual)
    }

    "Be false if Triggerise's shop has no products" in {
      val product: String = "HAT"
      val (products: Seq[Product], _) = testProductsEmpty
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean = checkout.isValidProduct(product)
      assert(!actual)
    }

    "Be false if Triggerise's shop has other products" in {
      val product: String = "HAT"
      val (products: Seq[Product], _) = testProducts1
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean = checkout.isValidProduct(product)
      assert(!actual)
    }

    "Be true if Triggerise's shop has the product" in {
      val product: String = "HAT"
      val (products: Seq[Product], _) = testProducts3
      val checkout: Checkout = Checkout(products = products)
      val actual: Boolean = checkout.isValidProduct(product)
      assert(actual)
    }
  }

  "Checkout constructor" should {
    "Create a checkout object with no products from empty data file" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: (Seq[Product], Map[String, Billing]) = (co.products, co.billingCodes)
      val expected: (Seq[Product], Map[String, Billing]) = testCheckoutProductsEmpty
      assert(expected.equals(actual))
    }

    "Create a checkout object with 1 product from data file with 1 product" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_1.json").getPath)
      val actual: (Seq[Product], Map[String, Billing]) = (co.products, co.billingCodes)
      val expected: (Seq[Product], Map[String, Billing]) =testCheckoutProducts1
      println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@2")
      println(actual)
      println(expected)
      assert(expected.equals(actual))
    }

    "Create a checkout object with 2 products from data file with 2 products" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_2.json").getPath)
      val actual: (Seq[Product], Map[String, Billing]) = (co.products, co.billingCodes)
      val expected: (Seq[Product], Map[String, Billing]) =testCheckoutProducts2 
      assert(expected.equals(actual))
    }

    "Create a checkout object with 3 products from data file with 3 products" in {
      val configuration: Configuration = Checkout.convertConfigFileContentsToObject(
        configurationFilePath = getClass.getResource("/products_3.json").getPath)
      val actual: (Seq[Product], BillingType) = (configuration.products, configuration.billingType)
      val expected: (Seq[Product], BillingType) = testProducts3
      assert(expected.equals(actual))
    }
  }

  "scan" should {
    "Add no item to shopping cart if shop has no products and item is empty String" in {
      val item: String = ""
      val (products: Seq[Product], _) = testProductsEmpty
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add no item to shopping cart if shop has no products" in {
      val item: String = "HAT"
      val (products: Seq[Product], _) = testProductsEmpty
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add one item to shopping cart if shop has that product" in {
      val item: String = "TICKET"
      val (products: Seq[Product], _) = testProducts1
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq(item)
      assert(expected.equals(actual))
    }

    "Add no item to shopping cart if shop does not have that product" in {
      val item: String = "HAT"
      val (products: Seq[Product], _) = testProducts1
      val checkout: Checkout = Checkout(products = products).scan(item)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq()
      assert(expected.equals(actual))
    }

    "Add four items to shopping cart" in {
      val ticket: String = "TICKET"
      val hat: String = "HAT"
      val (products: Seq[Product], _) = testProducts3
      val checkout: Checkout = Checkout(products = products).scan(ticket).scan(hat).scan(hat).scan(ticket)
      val actual: Seq[String] = checkout.items
      val expected: Seq[String] = Seq(ticket, hat, hat, ticket)
      assert(expected.equals(actual))
    }
  }

  "Checkout Total" should {
    "Be 0.00 Euro for an empty chopping kart" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Double = co.calcTotal
      val expected: Double = 0.00
      assert(expected.equals(actual))
    }

    "Be 0.00 Euro for another empty chopping kart" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath)
      val actual: Double = co.calcTotal
      val expected: Double = 0.00
      assert(expected.equals(actual))
    }

    "Be 7.50 Euro for a chopping kart with 1 hat" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("hat")
      val actual: Double = co.calcTotal
      val expected: Double = 7.50
      assert(expected.equals(actual))
    }

    "Be 15.00 Euro for a chopping kart with 2 hats" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("hat").scan("  Hat ")
      val actual: Double = co.calcTotal
      val expected: Double = 15.00
      assert(expected.equals(actual))
    }
  }

  "Get Code Price" should {
    "Be 0.00 if there are no Products and no code" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Double = co.getCodePrice(code = "  ")
      val expected: Double = 0.00
      assert(expected.equals(actual))
    }

    "Be 0.00 if there are no Products" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Double = co.getCodePrice(code = "hat  ")
      val expected: Double = 0.00
      assert(expected.equals(actual))
    }

    "Be 0.00 if code not found in Products List" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_1.json").getPath)
      val actual: Double = co.getCodePrice(code = "hat  ")
      val expected: Double = 0.00
      assert(expected.equals(actual))
    }

    "Be 7.50 for HAT" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath)
      val actual: Double = co.getCodePrice(code = "hat  ")
      val expected: Double = 7.50
      assert(expected.equals(actual))
    }
  }

  "Checkout Scan" should {
    "Not scan if Products and code are empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Not scan if there are Products but there is no scan" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath)
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Not scan if there are Products but code scan is empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("")
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Not scan if there are Products but code scan is not valid" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("PEN")
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "2 HAT if there are Products and 2 HAT are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("hat")
      val actual: Seq[String] = co.items
      val expected: Seq[String] = Seq("HAT", "HAT")
      assert(expected.equals(actual))
    }

    "1 HAT if there are Products and 1 HAT is scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT")
      val actual: Seq[String] = co.items
      val expected: Seq[String] = Seq("HAT")
      assert(expected.equals(actual))
    }

    "1 HAT and 1 HOODIE if there are Products and 1 HAT and 1 HOODIE are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("HOODIE")
      val actual: Seq[String] = co.items
      val expected: Seq[String] = Seq("HOODIE", "HAT")
      assert(expected.equals(actual))
    }
  }

  "Count items with the same code" should {
    "Be 0 if both products and items are empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Long = co.countItemsSameCode("")
      val expected: Long = 0
      assert(expected.equals(actual))
    }

    "Be 0 if items is empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath)
      val actual: Long = co.countItemsSameCode("")
      val expected: Long = 0
      assert(expected.equals(actual))
    }

    "Be 0 if products is empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).scan("hat")
      val actual: Long = co.countItemsSameCode("hat")
      val expected: Long = 0
      assert(expected.equals(actual))
    }

    "Be 0 if scanned item is not valid" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("PEN")
      val actual: Long = co.countItemsSameCode("PEN")
      val expected: Long = 0
      assert(expected.equals(actual))
    }

    "Be 1 if 1 item is scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT")
      val actual: Long = co.countItemsSameCode("HAT")
      val expected: Long = 1
      assert(expected.equals(actual))
    }

    "Be 1 if 2 different items are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("TICKET")
      val actual: Long = co.countItemsSameCode("HAT")
      val expected: Long = 1
      assert(expected.equals(actual))
    }

    "Be 2 if 2 items are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("HAT")
      val actual: Long = co.countItemsSameCode("HAT")
      val expected: Long = 2
      assert(expected.equals(actual))
    }

    "Be 3 if 3 HAT are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("HAT").scan("HOODIE").scan("TICKET").scan("HAT")
      val actual: Long = co.countItemsSameCode("HAT")
      val expected: Long = 3
      assert(expected.equals(actual))
    }
  }

  "Distinct itmes in shopping kart" should {
    "Be an empty Set if items and products are empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath)
      val actual: Set[String] = co.distinctItemsInShoppingKart
      assert(actual.isEmpty)
    }

    "Be an empty Set if products is empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).scan("HAT")
      val actual: Set[String] = co.distinctItemsInShoppingKart
      assert(actual.isEmpty)
    }

    "Be an empty Set if there are products but items is empty" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath)
      val actual: Set[String] = co.distinctItemsInShoppingKart
      assert(actual.isEmpty)
    }

    "Be an empty Set if there are products but scanned item is not valid" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("PEN")
      val actual: Set[String] = co.distinctItemsInShoppingKart
      assert(actual.isEmpty)
    }

    "Set with HAT if there are products and scanned item is HAT" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT")
      val actual: Set[String] = co.distinctItemsInShoppingKart
      val expected: Set[String] = Set("HAT")
      assert(expected.equals(actual))
    }

    "Set with HAT if there are products and scanned items are HAT" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("HAT").scan("HAT").scan("HAT")
      val actual: Set[String] = co.distinctItemsInShoppingKart
      val expected: Set[String] = Set("HAT")
      assert(expected.equals(actual))
    }

    "Set with HAT, HOODIE and TICKET if they are scanned" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("TICKET").scan("HOODIE").scan("HAT")
      val actual: Set[String] = co.distinctItemsInShoppingKart
      val expected: Set[String] = Set("HAT", "HOODIE", "TICKET")
      assert(expected.equals(actual))
    }
  }

  "Reset shopping kart" should {
    "Return a Checkout object with empty items if there are no products and items" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).resetShppingKart
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Return a Checkout object with empty items if there are items" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).resetShppingKart
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Return a Checkout object with empty items if there are no products" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).scan("HAT").resetShppingKart
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Return a Checkout object with empty items if there are products and items" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").resetShppingKart
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }

    "Return a Checkout object with empty items if there are products and several items" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("HAT").scan("HOODIE").resetShppingKart
      val actual: Seq[String] = co.items
      assert(actual.isEmpty)
    }
  }

  "Test available products" should {
    "Be an empty Set if there are no products" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_empty.json").getPath).resetShppingKart
      val actual: Set[String] = co.availableProducts
      assert(actual.isEmpty)
    }

    "Set with HAT, HOODIE and TICKET if they are in config file" in {
      val co: Checkout = Checkout(pricing_rules = getClass.getResource("/products_3.json").getPath).scan("HAT").scan("TICKET").scan("HOODIE").scan("HAT")
      val actual: Set[String] = co.availableProducts
      val expected: Set[String] = Set("HAT", "HOODIE", "TICKET")
      assert(expected.equals(actual))
    }
  } 
}