package com.example.stock.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stock.R;
import com.example.stock.chart.notimportant.DayAxisValueFormatter;
import com.example.stock.dao.StockDao;
import com.example.stock.model.Stock;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class ProductsQualityActivity extends AppCompatActivity {

    private static final String TAG = "TAGProductsQualityActivity";
    private LineChart lineChart;
    private StockDao stockDao;
    private Map<Integer, Double> ripeMap = new HashMap<>();
    private Map<Integer, Double> underripeMap = new HashMap<>();
    private Map<Integer, Double> overripeMap = new HashMap<>();
    private ArrayList<Entry> dataRipe = new ArrayList<>();
    private ArrayList<Entry> dataunderripe = new ArrayList<>();
    private ArrayList<Entry> dataOverripeMap = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_quality);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lineChart = findViewById(R.id.line_chart);
        stockDao = new StockDao( FirebaseDatabase.getInstance() , FirebaseAuth.getInstance() , this , getSupportFragmentManager() );

        showDialog();
        fetchDataAndProcess();

    }

    private void fetchDataAndProcess() {
        stockDao.getStocks(new StockDao.OnStocksFetchListener() {
            @Override
            public void onStocksFetchSuccess(LinkedList<Stock> stocks) {
                getLastStocks(stocks);
            }

            @Override
            public void onStocksFetchFailure(Exception e) {
                Log.e(TAG , "error : "+e.toString());
            }
        });
    }

    private void getLastStocks(LinkedList<Stock> stocks) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);

        for (int i = 0; i < 7; i++) {
            // Réinitialisez les quantités pour chaque jour
            ripeMap.put(i, 0.0);
            underripeMap.put(i, 0.0);
            overripeMap.put(i, 0.0);

            // Boucle à travers les stocks pour ce jour spécifique
            Log.d(TAG," sizeeeeeeeeee : "+ stocks);
            for (Stock stock : stocks) {
                if (stock.getDate() != null) {
                    Calendar stockCalendar = Calendar.getInstance();
                    stockCalendar.setTime(stock.getDate());

                    int var = 1;

                    if(stock.getType()== "out"){
                        var = -1;
                    }

                    Log.d(TAG, "stockCalendar.get(Calendar.DAY_OF_MONTH) : "+stockCalendar.get(Calendar.DAY_OF_MONTH) + " calendar.get(Calendar.DAY_OF_MONTH)  ; "+calendar.get(Calendar.DAY_OF_MONTH)  );
                    if (stockCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        if (stock.getCategory() == 1){
                            overripeMap.put(i, overripeMap.get(i) + var*stock.getQuantity());
                        } else if (stock.getCategory() == -1) {
                            underripeMap.put(i, underripeMap.get(i) + var*stock.getQuantity());
                        } else if (stock.getCategory() == 0) {
                            ripeMap.put(i,ripeMap.get(i) + var*stock.getQuantity());
                        }
                    }
                }
            }

            // Passer au jour précédent
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d(TAG , "tessssssssssssssst "+ ripeMap.get(0)+" hhhh " +ripeMap.toString() + underripeMap.toString() + overripeMap.toString());

//        dataRipe.add(new Entry(0 , Objects.requireNonNull(ripeMap.get(0)).floatValue()));
        if (ripeMap != null && ripeMap.get(0) != null) {
            dataRipe.add(new Entry(0, ripeMap.get(0).floatValue()));
        } else {
            Log.e("ProductsQualityActivity", "ripeMap or key 0 is null");
        }

        dataRipe.add(new Entry(1 , Objects.requireNonNull(ripeMap.get(1)).floatValue()));
        dataRipe.add(new Entry(2 , Objects.requireNonNull(ripeMap.get(2)).floatValue()));
        dataRipe.add(new Entry(3 , Objects.requireNonNull(ripeMap.get(3)).floatValue()));
        dataRipe.add(new Entry(4 , Objects.requireNonNull(ripeMap.get(4)).floatValue()));
        dataRipe.add(new Entry(5 , Objects.requireNonNull(ripeMap.get(5)).floatValue()));
        dataRipe.add(new Entry(6 , Objects.requireNonNull(ripeMap.get(6)).floatValue()));

        dataunderripe.add(new Entry(0 , Objects.requireNonNull(underripeMap.get(0)).floatValue()));
        dataunderripe.add(new Entry(1 , Objects.requireNonNull(underripeMap.get(1)).floatValue()));
        dataunderripe.add(new Entry(2 , Objects.requireNonNull(underripeMap.get(2)).floatValue()));
        dataunderripe.add(new Entry(3 , Objects.requireNonNull(underripeMap.get(3)).floatValue()));
        dataunderripe.add(new Entry(4 , Objects.requireNonNull(underripeMap.get(4)).floatValue()));
        dataunderripe.add(new Entry(5 , Objects.requireNonNull(underripeMap.get(5)).floatValue()));
        dataunderripe.add(new Entry(6 , Objects.requireNonNull(underripeMap.get(6)).floatValue()));

        dataOverripeMap.add(new Entry(0 , Objects.requireNonNull(overripeMap.get(0)).floatValue()));
        dataOverripeMap.add(new Entry(1 , Objects.requireNonNull(overripeMap.get(1)).floatValue()));
        dataOverripeMap.add(new Entry(2 , Objects.requireNonNull(overripeMap.get(2)).floatValue()));
        dataOverripeMap.add(new Entry(3 , Objects.requireNonNull(overripeMap.get(3)).floatValue()));
        dataOverripeMap.add(new Entry(4 , Objects.requireNonNull(overripeMap.get(4)).floatValue()));
        dataOverripeMap.add(new Entry(5 , Objects.requireNonNull(overripeMap.get(5)).floatValue()));
        dataOverripeMap.add(new Entry(6 , Objects.requireNonNull(overripeMap.get(6)).floatValue()));

        showLCProductQuality(calendar);
        hideDialog();

    }

    private void showLCProductQuality(Calendar calendar) {
        LineDataSet lineDataSet1 = new LineDataSet( dataRipe , "Ripe" );
        LineDataSet lineDataSet2 = new LineDataSet( dataunderripe , "Under Ripe" );
        LineDataSet lineDataSet3 = new LineDataSet( dataOverripeMap , "Over Ripe" );

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);

        int yellowColor = ContextCompat.getColor(this, R.color.yellow);
        int greenColor = ContextCompat.getColor(this, R.color.green);
        int redColor = ContextCompat.getColor(this, R.color.red);
        lineDataSet1.setColor(yellowColor);
        lineDataSet2.setColor(greenColor);
        lineDataSet3.setColor(redColor);

        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleHoleRadius(9);
        lineDataSet1.setDrawValues(true);
        lineDataSet1.setValueTextSize(15);
        lineDataSet1.setValueTextColor(yellowColor);

        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setCircleHoleRadius(9);
        lineDataSet2.setDrawValues(true);
        lineDataSet2.setValueTextSize(15);
        lineDataSet2.setValueTextColor(greenColor);

        lineDataSet3.setDrawCircles(true);
        lineDataSet3.setDrawCircleHole(true);
        lineDataSet3.setCircleHoleRadius(9);
        lineDataSet3.setDrawValues(true);
        lineDataSet3.setValueTextSize(15);
        lineDataSet3.setValueTextColor(redColor);

        lineChart.setNoDataText("No Data");

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new DayAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        Description description = new Description();
        description.setText("Zoo");
        description.setTextColor(Color.BLUE);
        description.setTextSize(10);
        description.setPosition(0.5f, 0.5f);
        lineChart.setDescription(description);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }
    private void showDialog(){
//        pieText.setVisibility(View.GONE);
//        pieTextLayout.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        ripeProductsQuality.setVisibility(View.GONE);
    }
    private void hideDialog(){
//        pieText.setVisibility(View.VISIBLE);
//        pieTextLayout.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.GONE);
//        ripeProductsQuality.setVisibility(View.VISIBLE);
    }
}