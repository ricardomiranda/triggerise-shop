package com.ricardomiranda.shop

import scala.annotation.tailrec

object Main extends App {
  val arguments: ArgumentParser = ArgumentParser.parser.parse(args, ArgumentParser()) match {
    case Some(config) => config
    case None => throw new IllegalArgumentException()
  }

  val co: Checkout = Checkout(pricing_rules = arguments.pricing_rules)

  println(s"Hello!")
}
