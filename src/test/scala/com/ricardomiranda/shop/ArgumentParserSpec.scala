package com.ricardomiranda.shop

import org.scalatest.{Matchers, WordSpec}


class ArgumentParserSpec extends WordSpec with Matchers {

  val wellFormedArguments: Seq[String] = Seq(
    "-j",
    getClass.getResource("/products_3.json").getPath
  )

  val missingFileArguments: Seq[String] = Seq(
    "-j",
    "non_existing_test_file.json" 
  )

  "Config parser" should {
    "parse well formed arguments correctly" in {
      val actual: Option[ArgumentParser] =
        ArgumentParser.parser.parse(wellFormedArguments, ArgumentParser())
      assert(actual.isDefined)
    }

    "fail when a JSON file does not exist" in {
      val actual: Option[ArgumentParser] =
        ArgumentParser.parser.parse(missingFileArguments, ArgumentParser())
      assert(actual.isEmpty)
    }
  }
}
