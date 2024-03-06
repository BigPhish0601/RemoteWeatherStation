import json
import requests

endpointUrl = "http://requestbin.fullcontact.com/143d0dp1"
endpointUrl2 = "http://weatherstationyeahbuddy-dev.us-east-1.elasticbeanstalk.com/api/values/insertweatherdata"
username = ""
password = ""
headers = {'content-type' : 'application/json'}


def PostToAWS(data):
    data = data.replace(']', '')
    data = data.replace('[', '')

    r = requests.post(url=endpointUrl2, data=data, headers=headers)
    return r.text
#1461600966000
#1493136966000