package com.ricardomiranda.shop

import java.net.{HttpURLConnection, URL}
import java.nio.file.{Files, Paths}

import scopt.OptionParser


/** Case class for the config object.
  *
  * @param pricing_rules The path to the json file with the configuration.
  */
case class ArgumentParser(pricing_rules: String = "",
                          databaseConfigurationFile: String = "",
                          endToEndTestConfigurationFilePath: String = ""
                         )

/** Object that parses the arguments received. */
object ArgumentParser {

  val parser: OptionParser[ArgumentParser] =
    new scopt.OptionParser[ArgumentParser](programName = "TriggeriseShop") {
      head(xs = "TriggeriseShop")

      opt[String]('j', name = "pricing_rules")
        .valueName("")
        .required()
        .action((x, c) => c.copy(pricing_rules = x)
        )
        .validate(x => if (validateConfigFileExistance(value = x)) {
          success
        } else {
          failure(msg = "Triggerise shop JSON file does not exist")
        })
        .text("pricing_rules")
    }

  /** Method to check if the json file received exists.
    *
    * @param filePath the json file to check.
    * @return true if the file exists, false if not.
    */
  def isLocalFileExists(filePath: String): Boolean = {
    Files.exists(Paths.get(filePath))
  }

  /** Method that checks if a remote address exists and is reachable.
    *
    * @param address The address to check.
    * @return True if the address exists, false if not.
    */
  def isRemoteAddressExists(address: String): Boolean = {
    val url: URL = new URL(address)
    val httpUrlConnection: HttpURLConnection  = url.openConnection.asInstanceOf[HttpURLConnection]
    httpUrlConnection.setRequestMethod("HEAD")
    httpUrlConnection.getResponseCode == HttpURLConnection.HTTP_OK
  }

  /** Method to check if the value passed as argument exists, either locally or remotely.
    *
    * @param value The argument to check.
    * @return True if the file exists, false if not.
    */
  def validateConfigFileExistance(value: String): Boolean = {
    isLocalFileExists(filePath = value) || isRemoteAddressExists(address = value)
  }
}
