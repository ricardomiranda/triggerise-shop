package com.ricardomiranda.shop

import org.scalatest.{Matchers, WordSpec}


class BillingSpec extends WordSpec with Matchers {
  "Regular Billing constructor" should {
    "Create a Regular Billing given correct parameters" in {
      val actual: Regular = Regular(code = "PEN")
      assert(actual.isInstanceOf[Regular])
    }

    "Has the code stored" in {
      val regular: Regular = Regular(code = "PEN")
      val actual: String = regular.code
      val expected: String = "PEN"
      assert(expected.equals(actual))
    }
  }

  "Compute bill for a regular code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val regular: Regular = Regular(code = "PEN")
      val actual: Double = regular.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 10.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val regular: Regular = Regular(code = "PEN")
      val actual: Double = regular.computeBill(price, quantity)
      val expected: Double = 10d
      assert(expected.equals(actual))
    }
  }

  "TwoForOne Billing constructor" should {
    "Create a TwoForOne Billing given correct parameters" in {
      val actual: TwoForOne = TwoForOne(code = "PEN")
      assert(actual.isInstanceOf[TwoForOne])
    }

    "Has the code stored" in {
      val twoForOne: TwoForOne = TwoForOne(code = "PEN")
      val actual: String = twoForOne.code
      val expected: String = "PEN"
      assert(expected.equals(actual))
    }
  }

  "Compute bill for a twoForOne code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val twoForOne: TwoForOne = TwoForOne(code = "PEN")
      val actual: Double = twoForOne.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 5.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val twoForOne: TwoForOne = TwoForOne(code = "PEN")
      val actual: Double = twoForOne.computeBill(price, quantity)
      val expected: Double = 5d
      assert(expected.equals(actual))
    }
  }
}

class BillingFactorySpec extends WordSpec with Matchers {
  "Billing Factory" should {
    "Create a Regular Billing if keyword regular is given" in {
      val actual: Billing = BillingFactory(billingType = "regular", code = "PEN")
      assert(actual.isInstanceOf[Regular])
    }
    
    "Create a TwoForOne Billing if keyword two_for_one is given" in {
      val actual: Billing = BillingFactory(billingType = "two_for_one", code = "PEN")
      assert(actual.isInstanceOf[TwoForOne])
    }
    
    "Create a XOrMore Billing if keyword x_or_more is given" in {
      val actual: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 2)
      assert(actual.isInstanceOf[XOrMore])
    }
  }

  "Compute bill for a regular code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val billing: Billing = BillingFactory(billingType = "regular", code = "PEN")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 10.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val billing: Billing = BillingFactory(billingType = "regular", code = "PEN")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 10d
      assert(expected.equals(actual))
    }
  }

  "Compute bill for a two_for_one code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val billing: Billing = BillingFactory(billingType = "two_for_one", code = "PEN")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 5.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val billing: Billing = BillingFactory(billingType = "two_for_one", code = "PEN")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 5.00
      assert(expected.equals(actual))
    }
  }

  "XorMore Billing constructor" should {
    "Create a XOrMore Billing given correct parameters" in {
      val actual: XOrMore = XOrMore(code = "PEN", promoPrice = 22.00, x = 2)
      assert(actual.isInstanceOf[XOrMore])
    }

    "Has the code stored" in {
      val xOrMore: XOrMore = XOrMore(code = "PEN", promoPrice = 22.00, x = 2)
      val actual: String = xOrMore.code
      val expected: String = "PEN"
      assert(expected.equals(actual))
    }
  }

  "Compute bill for a x_or_more code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 2)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 25.00 if quanty is 1, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 1
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 25.00
      assert(expected.equals(actual))
    }

    "Compute 50.00 if quanty is 2, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 2
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 50.00
      assert(expected.equals(actual))
    }

    "Compute 25.00 if quanty is 3, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 3
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 75.00
      assert(expected.equals(actual))
    }

    "Compute 25.00 if quanty is 4, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 4
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 88.00
      assert(expected.equals(actual))
    }

    "Compute 25.00 if quanty is 10, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 10
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 220.00
      assert(expected.equals(actual))
    }

    "Compute 25.00 if quanty is 15, price 25.00 and promoPrice is 22.00" in {
      val price: Double = 25.00
      val quantity: Long = 15
      val billing: Billing = BillingFactory(billingType = "x_or_more", code = "PEN", promoPrice = 22.00, x = 4)
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 330.00
      assert(expected.equals(actual))
    }
  }
}
