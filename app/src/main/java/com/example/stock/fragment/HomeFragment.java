package com.example.stock.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stock.R;
import com.example.stock.activity.ProductsQualityActivity;
import com.example.stock.activity.StockInOutActivity;
import com.example.stock.dao.StockDao;
import com.example.stock.dto.CountStock;
import com.example.stock.dto.ProductsQuality;
import com.example.stock.dto.Temperature;
import com.example.stock.model.Stock;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAGHomeFragment";
    private static final int REQUEST_CODE = 123;
    //    private LineChart mpLineChart;
    private ArrayList<BarEntry> dataIN;
    private ArrayList<BarEntry> dataOut;
    Map<Integer, Double> entriesMap = new HashMap<>();
    Map<Integer, Double> exitsMap = new HashMap<>();
    private BarChart barChart , bcProductsQuality;
    private PieChart pieChart;
    private StockDao stockDao;
    private TextView pieText , ripeValue , temperatureValue , humidityValue , inValue , outValue ;
    private LinearLayout pieTextLayout , ripeProductsQuality;
    private ProgressBar progressBar;

    private View viewLayout;
    int[] colorClassArray = new int[]{Color.BLUE , Color.LTGRAY};
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
//        mpLineChart = view.findViewById(R.id.line_chart);
        barChart = view.findViewById(R.id.mp_BarChart);
        pieChart = view.findViewById(R.id.pie_chart);
        pieText = view.findViewById(R.id.pie_text);
        TextView pieTextValue = view.findViewById(R.id.pie_text_value);
        temperatureValue = view.findViewById(R.id.temperature_value);
        humidityValue = view.findViewById(R.id.humidity_value);
        inValue = view.findViewById(R.id.in_value);
        outValue = view.findViewById(R.id.out_value);
        progressBar = view.findViewById(R.id.progressBar);
        pieTextLayout = view.findViewById(R.id.pie_text_layout);
        bcProductsQuality = view.findViewById(R.id.bc_products_quality);
        ripeValue = view.findViewById(R.id.ripe_products_quality_value);
        ripeProductsQuality = view.findViewById(R.id.ripe_products_quality);
        Button buttonCreatePDF = view.findViewById(R.id.generatePdfButton);
        CardView cardProductsQuality = view.findViewById(R.id.card_products_quality);
        askForPermissions();
        buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });
        showDialog();
        fetchDataAndProcess();
        fetchDataCountStock();
        fetchProductsQuality();
        fetchTemperature();
        cardProductsQuality.setOnLongClickListener(view1 -> {
            PopupMenu popupMenu = showMenu(view );
            return false;
        });

        // line chart

//        LineDataSet lineDataSet1 = new LineDataSet( dataValues1() , "Stock IN" );
//        LineDataSet lineDataSet2 = new LineDataSet( dataValues2() , "Stock OUT" );
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(lineDataSet1);
//        dataSets.add(lineDataSet2);
//        lineDataSet1.setColor(Color.RED);
//        lineDataSet2.setColor(Color.BLUE);
//        lineDataSet1.setDrawCircles(true);
//        lineDataSet1.setDrawCircleHole(true);
//        lineDataSet1.setCircleHoleRadius(9);
//        lineDataSet1.setDrawValues(false);
//        lineDataSet2.setDrawValues(false);
//
//        mpLineChart.setNoDataText("No Data");
//
//        Legend legend = mpLineChart.getLegend();
//        legend.setEnabled(false);
//
//        XAxis xAxis = mpLineChart.getXAxis();
//        xAxis.setValueFormatter(new DayAxisValueFormatter());
//
//        Description description = new Description();
//        description.setText("Zoo");
//        description.setTextColor(Color.BLUE);
//        description.setTextSize(10);
//        description.setPosition(0.5f, 0.5f);
//        mpLineChart.setDescription(description);
//
//        LineData data = new LineData(dataSets);
//        mpLineChart.setData(data);
//        mpLineChart.invalidate();

        // bar chart

        // pie chart

        viewLayout= view ;
        return view;
    }

    private PopupMenu showMenu(View view) {

        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenu().add("Stock State");
        popupMenu.getMenu().add("Products Quality");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getTitle().toString()) {
                case "Stock State":
                {
                    Intent intent = new Intent(requireContext() , StockInOutActivity.class);
                    requireActivity().startActivity(intent);
                    return true;
                }
                case "Products Quality":
                {
                    Intent intent = new Intent(requireContext(), ProductsQualityActivity.class);
                    requireActivity().startActivity(intent);
                    return true;
                }
                default:
                    return false;
            }
        });

        return popupMenu;
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions(requireActivity() , new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE);
    }
    private void fetchTemperature() {
        stockDao.getTemperature(new StockDao.OnTemperatureFetchListener() {
            @Override
            public void onTemperatureFetchSuccess(Temperature temperature) {

                temperatureValue.setText(String.valueOf(temperature.getTemperature()));
                humidityValue.setText(String.valueOf(temperature.getHumidity()));

            }

            @Override
            public void onTemperatureFetchFailure(Exception e) {

                Log.e(TAG , "error in fetch Temperature");

            }
        });
    }
    private void showPieChart(float valueFloat) {
        ArrayList<PieEntry> dataVal = new ArrayList<>();
        dataVal.add(new PieEntry( valueFloat , ""));
        dataVal.add(new PieEntry(100- valueFloat , ""));

        PieDataSet pieDataSet = new PieDataSet(dataVal,"");
        pieDataSet.setColors(colorClassArray);
        pieDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setCenterTextSize(10);
        pieChart.setCenterTextRadiusPercent(100);
        pieChart.setHoleRadius(80);

        pieChart.setDrawRoundedSlices(true);


        Legend legPie = pieChart.getLegend();
        legPie.setEnabled(false);

        pieChart.setDescription(null);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    private void showBCStockInOut(Calendar calendar) {
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
        barChart.setVisibleXRangeMaximum(7);

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

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setDrawLabels(false);


        barChart.invalidate();
    }
    private void showBCProductsQuality(ProductsQuality qualityToday){

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry( 0, (float) qualityToday.getUnderripe()));
        dataVals.add(new BarEntry(1 , (float) qualityToday.getRipe() ));
        dataVals.add(new BarEntry(2 , (float) qualityToday.getOverripe() ));

        BarDataSet barDataSet1 = new BarDataSet(dataVals , "");

        barDataSet1.setColors(Color.GREEN , Color.YELLOW , Color.RED);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);

        Legend legend = bcProductsQuality.getLegend();
        legend.setEnabled(false);

        bcProductsQuality.setData(barData);

        bcProductsQuality.setDescription(null);

        YAxis yAxisRight = bcProductsQuality.getAxisRight();
        yAxisRight.setDrawLabels(false);

        String[] xLabel = {"underripe" , "Ripe", "Overripe "};

        XAxis xAxis = bcProductsQuality.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabel));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        bcProductsQuality.invalidate();

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
    private void fetchDataCountStock() {
        stockDao.getCountStock(new StockDao.OnCountStockFetchListener() {
            @Override
            public void onCountStockFetchSuccess(CountStock countStock) {
                Log.d( TAG , "testttmbacj : "+countStock.getNbrTotal() );
                double count = countStock.getNbrTotal();
                String countString = String.valueOf(count);
                pieText.setText(countString);

                double value = (count/6000)*100;
                float valueFloat = (float) value;

                showPieChart(valueFloat);

            }

            @Override
            public void onCountStockFetchFailure(Exception e) {

            }
        });
    }
    private void fetchProductsQuality(){

        stockDao.getProductsQualityToday(new StockDao.OnProductsQualityTodayFetchListener() {
            @Override
            public void onProductsQualityTodayFetchSuccess(ProductsQuality qualityToday) {
                Log.d("TAGHomeFragmentT" , " qualityToday :"+qualityToday.toString());
                showBCProductsQuality(qualityToday);
                double doubleRipe = qualityToday.getRipe()*100/(qualityToday.getOverripe()+qualityToday.getRipe()+qualityToday.getUnderripe());
                String formattedRipeValue = String.format("%.2f", doubleRipe)+" %";
                ripeValue.setText(formattedRipeValue);
            }

            @Override
            public void onProductsQualityTodayFetchFailure(Exception e) {
                Log.e("TAGHomeFragmentT", e.toString());
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
        dataVals.add(new BarEntry(2 , 2  ));
        return dataVals;
    }
    private ArrayList<PieEntry> dataValuesPie(){
        ArrayList<PieEntry> dataVal = new ArrayList<>();
        dataVal.add(new PieEntry(75 , ""));
        dataVal.add(new PieEntry(25 , ""));
        return dataVal;
    }
    private void getLastStocks( LinkedList<Stock> stocks){

        // Initialisation des variables pour stocker les quantités pour chaque jour de la semaine précédente

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
                        if (Objects.equals(stock.getType(), "in")){
                            entriesMap.put(i, entriesMap.get(i) + stock.getQuantity());
                        }else {
                            exitsMap.put(i,exitsMap.get(i)+stock.getQuantity());
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
        inValue.setText(String.valueOf(entriesMap.get(6)));

        dataOut.add(new BarEntry(0 , Objects.requireNonNull(exitsMap.get(0)).floatValue()));
        dataOut.add(new BarEntry(1 , Objects.requireNonNull(exitsMap.get(1)).floatValue()));
        dataOut.add(new BarEntry(2 , Objects.requireNonNull(exitsMap.get(2)).floatValue()));
        dataOut.add(new BarEntry(3 , Objects.requireNonNull(exitsMap.get(3)).floatValue()));
        dataOut.add(new BarEntry(4 , Objects.requireNonNull(exitsMap.get(4)).floatValue()));
        dataOut.add(new BarEntry(5 , Objects.requireNonNull(exitsMap.get(5)).floatValue()));
        dataOut.add(new BarEntry(6 , Objects.requireNonNull(exitsMap.get(6)).floatValue()));
        outValue.setText(String.valueOf(exitsMap.get(6)));

        Log.d(TAG, "Stock IN : "+entriesMap + " Stock  out  ; "+exitsMap  );
        showBCStockInOut(calendar);
        hideDialog();
    }
    private void showDialog(){
        pieText.setVisibility(View.GONE);
        pieTextLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ripeProductsQuality.setVisibility(View.GONE);
    }
    private void hideDialog(){
        pieText.setVisibility(View.VISIBLE);
        pieTextLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        ripeProductsQuality.setVisibility(View.VISIBLE);
    }
    public void createPDF(){
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            requireActivity().getDisplay().getRealMetrics(displayMetrics);

        }
        else requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        viewLayout.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.AT_MOST ),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.AT_MOST));

        viewLayout.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        PdfDocument document = new PdfDocument();
        int viewWidth = viewLayout.getMeasuredWidth();
        int viewHeight = viewLayout.getMeasuredHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        viewLayout.draw(canvas);
        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "exempleXML.pdf";
        File file = new File(downloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(requireContext(), "Writing succefully ", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing" + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}