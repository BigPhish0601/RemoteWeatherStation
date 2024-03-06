/******************************************************************************
  SHT15 Example
  Joel Bartlett @ SparkFun Electronics
  16 Sept 2015
  https://github.com/sparkfun/SparkFun_ISL29125_Breakout_Arduino_Library

  This example shows how to get the temperature in F or C and humidity
  Developed/Tested with:
  SparkFun RedBoard
  Arduino IDE 1.6.5

  Connections:
  GND  -> A2
  Vcc  -> A3
  DATA -> A4
  SCK  -> A5

  Requires:
  SHT1X Arduino Library
  https://github.com/sparkfun/SHT15_Breakout/

  This code is beerware.
  Distributed as-is; no warranty is given.
******************************************************************************/
#include <SHT1X.h>

//variables for storing values
float tempC = 0;
float tempF = 0;
float humidity = 0;
char dataPayload[50];


//Create an instance of the SHT1X sensor
SHT1x sht15(9, 8);//Data, SCK

void setup()
{
  Serial.begin(9600); // Open serial connection to report values to host
}
//-------------------------------------------------------------------------------------------
void loop()
{
    readSensor();
    sendData();
    delay(10000);
}
//-------------------------------------------------------------------------------------------
void readSensor()
{
  // Read values from the sensor
  //tempC = sht15.readTemperatureC();
  tempF = sht15.readTemperatureF();
  humidity = sht15.readHumidity();
}
//-------------------------------------------------------------------------------------------
void sendData()
{
  /*
   * The format that the data is being
   * sent is simple. TEMPERATURE~HUMIDITY
   * The temperature(F) will be followed by
   * a tilde(~) and then by the humidity(%)
   * The python program which the data is being sent
   * to will handle separating them
   * using the tilde as a delimiter
   */
   String payload = String(tempF) + "~" + String(humidity);
   Serial.println(payload);//Send the payload through serial
}
