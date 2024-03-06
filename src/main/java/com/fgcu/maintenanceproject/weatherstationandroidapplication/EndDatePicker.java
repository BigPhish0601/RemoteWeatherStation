package com.fgcu.maintenanceproject.weatherstationandroidapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fgcu.maintenanceproject.weatherstationandroidapplication.Activity.WeatherStationActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class EndDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText _endDatePIcker;
    private int _date;
    private int _month;
    private int _year;
    private Context _context;


    public EndDatePicker(Context context, int endDatePickerID) {

        Activity activity = (Activity) context;
        this._endDatePIcker = (EditText) activity.findViewById(endDatePickerID);
        this._endDatePIcker.setOnClickListener(this);
        this._context = context;

    }


    //Activated when OK button of datepicker is selected
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        _year = year;
        _month = month;
        _date = dayOfMonth;

        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        ;
        Log.d("YOU CLICKED ME", "BUDDY");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the startDate
    private void updateDisplay() {

        _endDatePIcker.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_month + 1).append("/").append(_date).append("/").append(_year).append(" "));
    }

    public EditText get_startDatePicker() {
        return _endDatePIcker;
    }

    public int get_date() {
        return _date;
    }

    public int get_month() {
        return _month;
    }

    public int get_year() {
        return _year;
    }
}
