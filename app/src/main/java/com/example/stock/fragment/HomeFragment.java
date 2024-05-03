package com.example.stock.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stock.R;
import com.example.stock.chart.notimportant.DayAxisValueFormatter;
import com.example.stock.dao.StockDao;
import com.example.stock.model.Stock;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAGHomeFragment";
    private LineChart mpLineChart;
    private ArrayList<BarEntry> dataIN;
    private ArrayList<BarEntry> dataOut;


    // bar chart

    private BarChart barChart;
    private StockDao stockDao;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockDao = new StockDao( FirebaseDatabase.getInstance() , FirebaseAuth.getInstance() , getContext() , requireActivity().getSupportFragmentManager() );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dataIN = new ArrayList<>();
        dataOut = new ArrayList<>();
        mpLineChart = view.findViewById(R.id.line_chart);
        fetchDataAndProcess();

        // line chart
        LineDataSet lineDataSet1 = new LineDataSet( dataValues1() , "Stock IN" );
        LineDataSet lineDataSet2 = new LineDataSet( dataValues2() , "Stock OUT" );
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        lineDataSet1.setColor(Color.RED);
        lineDataSet2.setColor(Color.BLUE);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleHoleRadius(9);
        lineDataSet1.setDrawValues(false);
        lineDataSet2.setDrawValues(false);

        mpLineChart.setNoDataText("No Data");

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setValueFormatter(new DayAxisValueFormatter());

        Description description = new Description();
        description.setText("Zoo");
        description.setTextColor(Color.BLUE);
        description.setTextSize(10);
        description.setPosition(0.5f, 0.5f);
        mpLineChart.setDescription(description);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();

        // bar chart

        barChart = view.findViewById(R.id.mp_BarChart);

        return view;
    }

    private void fetchDataAndProcess() {
        stockDao.getStocks(new StockDao.OnStocksFetchListener() {
            @Override
            public void onStocksFetchSuccess(LinkedList<Stock> stocks) {

                getLastStocks(stocks);

            }

            @Override
            public void onStocksFetchFailure(Exception e) {
                Log.d( TAG , e.toString());
            }
        });
    }
    private ArrayList<Entry> dataValues1(){

        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0 , 20));
        dataVals.add(new Entry(1 , 50));
        dataVals.add(new Entry(2 , 2));
        dataVals.add(new Entry(3 , 10));
        dataVals.add(new Entry(4 , 28));
        dataVals.add(new Entry(5 , 40));
        dataVals.add(new Entry(6 , 28));

        return dataVals;
    }
    private ArrayList<Entry> dataValues2(){

        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0 , 12));
        dataVals.add(new Entry(1 , 16));
        dataVals.add(new Entry(2 , 23));
        dataVals.add(new Entry(3 , 1));
        dataVals.add(new Entry(4 , 18));
        dataVals.add(new Entry(5 , 10));
        dataVals.add(new Entry(6 , 28));

        return dataVals;
    }
    private ArrayList<BarEntry> dataValuesBar1(){

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0 , 3  ));
        dataVals.add(new BarEntry(1 , 4  ));
        dataVals.add(new BarEntry(2 , 6  ));
        dataVals.add(new BarEntry(3 , 11 ));
        dataVals.add(new BarEntry(4 , 6  ));
        dataVals.add(new BarEntry(5 , 8  ));
        dataVals.add(new BarEntry(6 , 10 ));

        return dataVals;
    }
    private ArrayList<BarEntry> dataValuesBar2(){

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0 , 2));
        dataVals.add(new BarEntry(1 , 8));
        dataVals.add(new BarEntry(2 , 3));
        dataVals.add(new BarEntry(3 , 7));
        dataVals.add(new BarEntry(4 , 5));
        dataVals.add(new BarEntry(5 , 10));
        dataVals.add(new BarEntry(6 , 17));

        return dataVals;
    }
    private void getLastStocks( LinkedList<Stock> stocks ){

//        Date date = new Date();
//        Log.d( TAG , date.toString());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        Double dayE0 = (double) 0, dayE1 = (double) 0 , dayE2 = (double) 0 , dayE3 = (double) 0 , dayE4 = (double) 0, dayE5 = (double) 0 , dayE6 = (double) 0;
//        Double dayS0 = (double) 0, dayS1 = (double) 0 , dayS2 = (double) 0 , dayS3 = (double) 0 , dayS4 = (double) 0, dayS5 = (double) 0 , dayS6 = (double) 0;
//
//        for(Stock stock : stocks){
//
//            Log.d(TAG , "stock.getDate() "+stock.getType()+" :"+stock.getDate());
//            if (stock.getDate() != null) {
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(stock.getDate());
//
//                int dayData = cal.get(Calendar.DAY_OF_MONTH);
//                Log.d( TAG , "dayData : "+dayData+ "day : "+ day);
//
//                if (Objects.equals(stock.getType(), "exits")){
//                    if( dayData ==day-6 ){
//                        dayS0 += stock.getQuantity();
//                    } else if(dayData ==day-5){
//                        dayS1 += stock.getQuantity();
//                    } else if(dayData ==day-4){
//                        dayS2 += stock.getQuantity();
//                    } else if(dayData ==day-3){
//                        dayS3 += stock.getQuantity();
//                    } else if(dayData ==day-2){
//                        dayS4 += stock.getQuantity();
//                    } else if(dayData ==day-1){
//                        dayS5 += stock.getQuantity();
//                    } else if(dayData ==day){
//                        dayS6 += stock.getQuantity();
//                    }
//                } else if( Objects.equals(stock.getType(), "entries") ){
//                    if( dayData ==day-6 ){
//                        dayE0 += stock.getQuantity();
//                    } else if(dayData ==day-5){
//                        dayE1 += stock.getQuantity();
//                    } else if(dayData ==day-4){
//                        dayE2 += stock.getQuantity();
//                    } else if(dayData ==day-3){
//                        dayE3 += stock.getQuantity();
//                    } else if(dayData ==day-2){
//                        dayE4 += stock.getQuantity();
//                    } else if(dayData ==day-1){
//                        dayE5 += stock.getQuantity();
//                    } else if(dayData ==day){
//                        dayE6 += stock.getQuantity();
//                    }
//                }
//            } else {
//                // Gérez le cas où la date est null
//                Log.d( TAG , "Gérez le cas où la date est null" );
//            }
//        }
//
//

        // Initialisation des variables pour stocker les quantités pour chaque jour de la semaine précédente
        Map<Integer, Double> entriesMap = new HashMap<>();
        Map<Integer, Double> exitsMap = new HashMap<>();

// Obtenez la date de la semaine précédente
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);

// Boucle à travers chaque jour de la semaine précédente
        for (int i = 0; i < 7; i++) {
            // Réinitialisez les quantités pour chaque jour
            entriesMap.put(i, 0.0);
            exitsMap.put(i, 0.0);

            // Boucle à travers les stocks pour ce jour spécifique
            Log.d(TAG," sizeeeeeeeeee : "+ stocks);
            for (Stock stock : stocks) {
                if (stock.getDate() != null) {
                    Calendar stockCalendar = Calendar.getInstance();
                    stockCalendar.setTime(stock.getDate());

                    Log.d(TAG, "stockCalendar.get(Calendar.DAY_OF_MONTH) : "+stockCalendar.get(Calendar.DAY_OF_MONTH) + " calendar.get(Calendar.DAY_OF_MONTH)  ; "+calendar.get(Calendar.DAY_OF_MONTH)  );
                    if (stockCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        // Vérifiez si c'est une entrée ou une sortie et mettez à jour la quantité appropriée
                        if (Objects.equals(stock.getType(), "entries")) {
                            entriesMap.put(i, entriesMap.get(i) + stock.getQuantity());
                        } else if (Objects.equals(stock.getType(), "exits")) {
                            exitsMap.put(i, exitsMap.get(i) + stock.getQuantity());
                        }
                    }
                }
            }

            // Passer au jour précédent
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

// Maintenant entriesMap et exitsMap contiennent les quantités pour chaque jour de la semaine précédent

        dataIN.add(new BarEntry(0 , Objects.requireNonNull(entriesMap.get(0)).floatValue()));
        dataIN.add(new BarEntry(1 , Objects.requireNonNull(entriesMap.get(1)).floatValue()));
        dataIN.add(new BarEntry(2 , Objects.requireNonNull(entriesMap.get(2)).floatValue()));
        dataIN.add(new BarEntry(3 , Objects.requireNonNull(entriesMap.get(3)).floatValue()));
        dataIN.add(new BarEntry(4 , Objects.requireNonNull(entriesMap.get(4)).floatValue()));
        dataIN.add(new BarEntry(5 , Objects.requireNonNull(entriesMap.get(5)).floatValue()));
        dataIN.add(new BarEntry(6 , Objects.requireNonNull(entriesMap.get(6)).floatValue()));

        dataOut.add(new BarEntry(0 , Objects.requireNonNull(exitsMap.get(0)).floatValue()));
        dataOut.add(new BarEntry(1 , Objects.requireNonNull(exitsMap.get(1)).floatValue()));
        dataOut.add(new BarEntry(2 , Objects.requireNonNull(exitsMap.get(2)).floatValue()));
        dataOut.add(new BarEntry(3 , Objects.requireNonNull(exitsMap.get(3)).floatValue()));
        dataOut.add(new BarEntry(4 , Objects.requireNonNull(exitsMap.get(4)).floatValue()));
        dataOut.add(new BarEntry(5 , Objects.requireNonNull(exitsMap.get(5)).floatValue()));
        dataOut.add(new BarEntry(6 , Objects.requireNonNull(exitsMap.get(6)).floatValue()));

        Log.d(TAG, "Stock IN : "+entriesMap + " Stock  out  ; "+exitsMap  );
        BarDataSet barDataSet1 = new BarDataSet(dataIN , "Data Set 1");
        BarDataSet barDataSet2 = new BarDataSet(dataOut , "Data Set 2");

        barDataSet1.setColor(Color.RED);
        barDataSet2.setColor(Color.BLUE);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);
        barData.addDataSet(barDataSet2);

        barChart.setData(barData);

        String[] days = {"Su" , "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        String[] daysOfWeek = new String[7];
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        for ( int i = 0 ; i<7 ; i++){

            int var = dayOfWeek + i ;
            Log.d( TAG , "var : "+var);
            daysOfWeek[i] = days[ var%7 ];
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(daysOfWeek));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barChart.setDescription(null);

        float barSpace = 0.05f;
        float groupSpace = 0.4f;

        barData.setBarWidth(0.25f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth( groupSpace , barSpace )*7);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0 , groupSpace , barSpace );
        barChart.invalidate();

    }

}