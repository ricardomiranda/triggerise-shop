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
      val actual: Billing = BillingFactory(code = "PEN", billingType = "regular")
      assert(actual.isInstanceOf[Regular])
    }
    
    "Create a TwoForOne Billing if keyword two_for_one is given" in {
      val actual: Billing = BillingFactory(code = "PEN", billingType = "two_for_one")
      assert(actual.isInstanceOf[TwoForOne])
    }
  }

  "Compute bill for a regular code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val billing: Billing = BillingFactory(code = "PEN", billingType = "regular")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 10.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val billing: Billing = BillingFactory(code = "PEN", billingType = "regular")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 10d
      assert(expected.equals(actual))
    }
  }

  "Compute bill for a two_for_one code" should {
    "Compute 0.00 if quanty and price are 0" in {
      val price: Double = 0.00
      val quantity: Long = 0
      val billing: Billing = BillingFactory(code = "PEN", billingType = "two_for_one")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 0d
      assert(expected.equals(actual))
    }

    "Compute 10.00 if quanty is 2 and price 5.00" in {
      val price: Double = 5.00
      val quantity: Long = 2
      val billing: Billing = BillingFactory(code = "PEN", billingType = "two_for_one")
      val actual: Double = billing.computeBill(price, quantity)
      val expected: Double = 5d
      assert(expected.equals(actual))
    }
  }
}
