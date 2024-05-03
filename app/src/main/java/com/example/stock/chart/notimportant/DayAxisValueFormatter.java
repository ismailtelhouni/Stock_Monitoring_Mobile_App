package com.example.stock.chart.notimportant;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DayAxisValueFormatter extends ValueFormatter {

    private static final String TAG = "TAGDayAxisValueFormatter";
    public String[] daysOfWeek = new String[7];
    private final String[] daysOWeek = {"Su" , "Mo", "Tu", "We", "Th", "Fr", "Sa"};

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // Convertit la valeur float en index pour obtenir le jour de la semaine correspondant
        int index = (int) value;

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for ( int i = 0 ; i<7 ; i++){

            int var = dayOfWeek + i ;
            Log.d( TAG , "var : "+var);
            daysOfWeek[i] = daysOWeek[ var%7 ];
        }
        Log.d(TAG, Arrays.toString(daysOfWeek));

        // Assure que l'index est dans la plage des jours de la semaine
        if (index >= 0 && index < daysOfWeek.length) {
            return daysOfWeek[index];
        } else {
            return "";
        }
    }
}
