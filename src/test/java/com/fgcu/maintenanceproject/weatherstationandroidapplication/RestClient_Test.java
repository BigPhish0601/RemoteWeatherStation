package com.fgcu.maintenanceproject.weatherstationandroidapplication;

import com.fgcu.maintenanceproject.weatherstationandroidapplication.Model.RestClient;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.Model.WeatherDataDto;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Date;


/**
 * Created by Brannon on 4/19/2018.
 */

public class RestClient_Test {

    private RestClient restClient;

    @Before
    public void setup() {
        restClient = new RestClient();
    }

    @After
    public void teardown() {

    }

//    @Test
//    public void convertWeatherDataListToHumidityDataPointList_Test(){
//
//        String start_date = "12-December-2017";
//
//        String end_date = "22-April-2018";
//
//        Timestamp startTimeStamp = new Timestamp(1524423972000l);
//        Timestamp endTimeStamp = new Timestamp(1524427040000l);
//
//
//        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
//
//        Date startDate = null;
//        Date endDate = null;
//            startDate = new Date(startTimeStamp.getTime());
//            endDate = new Date(endTimeStamp.getTime());
//
//
//        List<WeatherData> weatherDataList = restClient.getWeatherDataBetweenTwoDates(startDate, endDate);
//
//
//    }
//
//    @Test
//    public void weatherResponseParser_Test() {
//
//        String response = "{ \"date_time\": \"2018-04-15 16:30:20\", \"humidity\": \"54.79\", \"temp_in_f\": \"67.95\"}";
//
//        try {
//            WeatherData weatherData = restClient.weatherResponseParser(response);
//
//            double expectedHumidity = 54.79;
//            Timestamp timestamp = Timestamp.valueOf("2018-04-15 16:30:20");
//
//
//            Assert.assertEquals(expectedHumidity, weatherData.getHumidity());
//            Assert.assertEquals(timestamp, weatherData.getTimestamp());
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    @Test
    public void getWeatherBetweenTwoDates_Test()
    {
        long startDate = 1524672855833l;
        long endDate = 1524672922063l;

        RestClient restClient = new RestClient();
        List<WeatherDataDto> weatherDataDtoList = restClient.getWeatherDataBetweenTwoDates(startDate, endDate);


    }

}

