package com.fgcu.maintenanceproject.weatherstationandroidapplication.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.EndDatePicker;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.Model.Conversions;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.Model.RestClient;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.Model.WeatherDataDto;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.R;
import com.fgcu.maintenanceproject.weatherstationandroidapplication.StartDatePicker;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherStationActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    RestClient restClient = new RestClient();

    private StartDatePicker startDatePicker;
    private EndDatePicker endDatePicker;
    private CheckBox humidityCheckbox;
    private CheckBox temperatureCheckbox;
    private Button populateButton;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private GraphView lineGraph;
    private String start_date;
    private String end_date;
    private long maxX;
    private long minX;

    private String restEndpointURL = "http://weatherstationyeahbuddy-dev.us-east-1.elasticbeanstalk.com/api/values/betweendates";


    private DataPoint[] humidityDataPoints;
    private DataPoint[] temperatureDataPoints;
    //Graph Stuff
    private LineGraphSeries<DataPoint> humidityLineGraphSeries;
    private LineGraphSeries<DataPoint> temperatureLineGraphSeries;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy hh:mm");

    private Conversions conversions = new Conversions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateControls();
        addActionListeners();


        lineGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        lineGraph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");


    }

    public void instantiateControls() {
        startDatePicker = new StartDatePicker(this, R.id.startDate);
        endDatePicker = new EndDatePicker(this, R.id.endDate);
        humidityCheckbox = (CheckBox) findViewById(R.id.humidityCheckbox);
        temperatureCheckbox = (CheckBox) findViewById(R.id.temperatureCheckbox);
        populateButton = (Button) findViewById(R.id.populateButton);
        lineGraph = (GraphView) findViewById(R.id.graph);
    }

    public void addActionListeners() {

        populateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (startDatePicker.get_date() == 0 || endDatePicker.get_date() == 0) {
                    //Notify user that they need to enter a date into both fields
                    Context context = getApplicationContext();
                    CharSequence text = "Start and end date must have values!!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }


                //Temperature or humidity must be checked before fields can be populated
                if (!humidityCheckbox.isChecked() && !temperatureCheckbox.isChecked()) {
                    //Notify user that they need to have either the humidity or temperature checked
                    Context context = getApplicationContext();
                    CharSequence text = "Humidity and/or Temperature must be checked!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Log.d("ATTENTION", "At least one checkbox must be checked!");
                    return;
                }


                //Get start and end date from datepickers
                start_date = startDatePicker.get_date() + "-" + startDatePicker.get_month() + "-" + startDatePicker.get_year();
                end_date = endDatePicker.get_date() + "-" + endDatePicker.get_month() + "-" + endDatePicker.get_year();
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");

                //Convert start and end date into Date objects
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = f.parse(start_date);
                    startDate.setMonth(startDate.getMonth() + 1);
                    endDate = f.parse(end_date);
                    endDate.setMonth(endDate.getMonth() + 1);

                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }


                //List<WeatherDataDto> weatherDataList = restClient.getWeatherDataBetweenTwoDates(startDate.getTime(), endDate.getTime());
                String weatherDataJson = null;
                try {

                    weatherDataJson = new TestCommand()
                            .execute(startDate.getTime(), endDate.getTime())
                            .get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                List<WeatherDataDto> weatherDataList = null;

                        ObjectMapper mapper = new ObjectMapper();

                        if(weatherDataJson.equals("[]")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lineGraph.removeAllSeries();
                                }
                            });


                            Context context = getApplicationContext();
                            CharSequence text = "No data between those dates :(";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            return;
                        }

                try {
                    weatherDataList = mapper.readValue(weatherDataJson, new TypeReference<List<WeatherDataDto>>(){});
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // List<WeatherDataDto> weatherDataList = null;
//
//                try {
//                    String myBody = "[{\"date_time\":1524672914373,\"humidity\":55.46,\"temp_in_f\":80},{\"date_time\":1524672919196,\"humidity\":70,\"temp_in_f\":40}]";
//                    ObjectMapper mapper = new ObjectMapper();
//                    weatherDataList = mapper.readValue(myBody, new TypeReference<List<WeatherDataDto>>() {
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


//
                if (humidityCheckbox.isChecked() && temperatureCheckbox.isChecked()) {
//                    //show humidity and temperature
//
                    humidityDataPoints = conversions.convertWeatherDataListToHumidityDataPointList(weatherDataList);
                    temperatureDataPoints = conversions.convertWeatherDataListToTemperatureDataPoints(weatherDataList);

                    humidityLineGraphSeries = new LineGraphSeries<>(humidityDataPoints);
                    temperatureLineGraphSeries = new LineGraphSeries<>(temperatureDataPoints);

                    humidityLineGraphSeries.setColor(Color.BLUE);
                    temperatureLineGraphSeries.setColor(Color.RED);
                    humidityLineGraphSeries.setDrawDataPoints(true);
                    humidityLineGraphSeries.setDataPointsRadius(10);
                    temperatureLineGraphSeries.setDrawDataPoints(true);
                    temperatureLineGraphSeries.setDataPointsRadius(10);
                    minX = weatherDataList.get(0).getDate_time();
                    maxX = weatherDataList.get(weatherDataList.size() - 1).getDate_time();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineGraph.removeAllSeries();
                            lineGraph.addSeries(humidityLineGraphSeries);
                            lineGraph.addSeries(temperatureLineGraphSeries);
                            lineGraph.setTitleTextSize(20);
                            lineGraph.setTitle("Weather Station");
                            lineGraph.getGridLabelRenderer().setVerticalAxisTitle("Humidity/Temperature");
                            lineGraph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");
                            // set date label formatter
                            //lineGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplication()));
                            //lineGraph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space
                            lineGraph.getViewport().setXAxisBoundsManual(true);
                            lineGraph.getViewport().setMinX(minX);
                            lineGraph.getViewport().setMaxX(maxX);

                            lineGraph.getViewport().setYAxisBoundsManual(true);
                            lineGraph.getViewport().setMaxY(100);
                            lineGraph.getViewport().setMinY(0);
                            //lineGraph.getGridLabelRenderer().setHumanRounding(false);
                            //lineGraph.getGridLabelRenderer().setNumVerticalLabels(1);
                        }
                    });

                } else if (humidityCheckbox.isChecked() && !temperatureCheckbox.isChecked()) {
                    //show humidity only
                    humidityDataPoints = conversions.convertWeatherDataListToHumidityDataPointList(weatherDataList);
                    humidityLineGraphSeries = new LineGraphSeries<>(humidityDataPoints);
                    humidityLineGraphSeries.setColor(Color.BLUE);
                    humidityLineGraphSeries.setDrawDataPoints(true);
                    humidityLineGraphSeries.setDataPointsRadius(10);

                    minX = weatherDataList.get(0).getDate_time();
                    maxX = weatherDataList.get(weatherDataList.size() - 1).getDate_time();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineGraph.removeAllSeries();
                            lineGraph.addSeries(humidityLineGraphSeries);
                            lineGraph.setTitleTextSize(20);
                            lineGraph.setTitle("Weather Station");
                            lineGraph.getGridLabelRenderer().setVerticalAxisTitle("Humidity");
                            lineGraph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");
                            // set date label formatter
                           // lineGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplication()));
                            //lineGraph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space

                            lineGraph.getViewport().setXAxisBoundsManual(true);
                            lineGraph.getViewport().setMinX(minX);
                            lineGraph.getViewport().setMaxX(maxX);

                            lineGraph.getViewport().setYAxisBoundsManual(true);
                            lineGraph.getViewport().setMaxY(100);
                            lineGraph.getViewport().setMinY(0);
                            //lineGraph.getGridLabelRenderer().setHumanRounding(false);
                            //lineGraph.getGridLabelRenderer().setNumVerticalLabels(1);
                        }
                    });
                } else if (!humidityCheckbox.isChecked() && temperatureCheckbox.isChecked()) {
                    //Show temperature only
                    temperatureDataPoints = conversions.convertWeatherDataListToTemperatureDataPoints(weatherDataList);
                    temperatureLineGraphSeries = new LineGraphSeries<>(temperatureDataPoints);
                    temperatureLineGraphSeries.setColor(Color.RED);
                    temperatureLineGraphSeries.setDrawDataPoints(true);
                    temperatureLineGraphSeries.setDataPointsRadius(10);

                    minX = weatherDataList.get(0).getDate_time();
                    maxX = weatherDataList.get(weatherDataList.size() - 1).getDate_time();
//
//                    lineGraph.removeAllSeries();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineGraph.removeAllSeries();
                            lineGraph.addSeries(temperatureLineGraphSeries);
                            lineGraph.setTitleTextSize(20);
                            lineGraph.setTitle("Weather Station");
                            lineGraph.getGridLabelRenderer().setVerticalAxisTitle("Temperature");
                            lineGraph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");
                            // set date label formatter

                            lineGraph.getViewport().setXAxisBoundsManual(true);
                            lineGraph.getViewport().setMinX(minX);
                            lineGraph.getViewport().setMaxX(maxX);

                            lineGraph.getViewport().setYAxisBoundsManual(true);
                            lineGraph.getViewport().setMaxY(100);
                            lineGraph.getViewport().setMinY(0);
                            //lineGraph.getGridLabelRenderer().setHumanRounding(false);
                            //lineGraph.getGridLabelRenderer().setNumVerticalLabels(1);
                        }
                    });
                } else {
                    //should never reach here
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        // startDateCalendarSelector.setText(sdf.format(myCalendar.getTime()));
    }

    public class TestCommand extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... dates) {

            //List<WeatherDataDto> weatherDataDtoList = null;
            Date date = new Date(dates[0]);
            Date date2 = new Date(dates[1]);
            String json = conversions.convertStartDateAndEndDateToJson(date.getTime(), date2.getTime());

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20000, TimeUnit.MILLISECONDS)
                    .readTimeout(20000, TimeUnit.MILLISECONDS)
                    .writeTimeout(20000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(restEndpointURL)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String myBody = response.body().string();
                //ObjectMapper mapper = new ObjectMapper();

                //weatherDataDtoList = mapper.readValue(myBody, new TypeReference<List<WeatherDataDto>>(){});
                return myBody;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }

}
