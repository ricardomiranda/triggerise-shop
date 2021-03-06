package com.ricardomiranda.shop

import com.typesafe.scalalogging.StrictLogging
import scala.annotation.tailrec
import util.control.Exception._

object Main extends App with StrictLogging {
  logger.info("Starting program Triggerise Shop")

  def help: Unit = println("""Menu options are:
                              | 1 -> Calc new shopping kart
                              | 2 -> Exit program
                              | 3 -> This help""".stripMargin)
  
  def choice: Long = 
    allCatch.
      opt { readLine("What do you want to do now? (1 -> Continue, 2 -> Exit, 3 -> Help)\n").toLong }.
      getOrElse(3)

  @tailrec
  def menu(co: Checkout, input: Long): Unit = input match {
    case 1 => 
      readLine("Please enter list of products separated by commas.\nitems: ").
        trim.
        split(",").
        foldLeft(co) { (acc, x) => acc.scan(x) }.
        total

      menu(co = co.resetShppingKart, input = choice)
    case 2 => 
      logger.info("Program terminated by user choice")
    case _ => 
      help 
      menu(co = co, input = choice)
  }

  val arguments: ArgumentParser = ArgumentParser.parser.parse(args, ArgumentParser()) match {
    case Some(config) => config
    case None => throw new IllegalArgumentException()
  }

  val co: Checkout = Checkout(pricing_rules = arguments.pricing_rules)
  help
  menu(co = co, input = 1)

  logger.info("Terminating program Triggerise Shop")
}
