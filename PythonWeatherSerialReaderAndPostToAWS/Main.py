import demjson
import psycopg2
import serial
import RestClient
import time
from decimal import Decimal

# com port number,  baud rate
# change so it matches the port number and
# baud rate of your set up
data = serial.Serial('com8', 9600)

r = RestClient
# status_code = r.PostToAWS("something")

while True:
    while data.inWaiting() == 0:
        pass  # do nothing
    try:
        dataString = data.readline()  # read data from serial
        dataString = dataString.decode("utf-8")  # decode into utf-8
        dataString = dataString.rstrip()  # remove garbage i.e. newlines, spaces
        dataList = dataString.split("~")  # split temp and hum at delimiter (~)
        tempF = dataList[0]
        humidity = dataList[1]
        date_time = int(round(time.time() * 1000))

        print("Temperature in F: " + tempF)
        print("Humidity: " + humidity)
        print("Date/Time :" + str(date_time) + "\n")

        weatherStationObject = [{'temp_in_f': tempF, 'humidity': humidity, 'date_time': date_time}]
        json = demjson.encode(weatherStationObject)

        restClient = RestClient
        r = restClient.PostToAWS(json)

        print(json)
        print(r)


    except Exception as e:
        print(e)

