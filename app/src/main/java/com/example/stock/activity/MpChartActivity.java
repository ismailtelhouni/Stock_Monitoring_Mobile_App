package com.example.stock.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stock.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.dynamite.DynamiteModule;

import java.util.ArrayList;

public class MpChartActivity extends AppCompatActivity {

    LineChart mpLineChart;
    int colorArray[] = {
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4
    };
    int[] colorClassArray = new int[] { Color.BLUE , Color.CYAN ,Color.GREEN ,Color.MAGENTA };
    String[] legendName = {
            "Cow",
            "Dog",
            "Cat",
            "Rat"
    };

    // bar chart

    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mp_chart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // line chart


        mpLineChart = findViewById(R.id.line_chart);
        LineDataSet lineDataSet1 = new LineDataSet( dataValues1() , "Data Set 1" );
        LineDataSet lineDataSet2 = new LineDataSet( dataValues2() , "Data Set 2" );
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        lineDataSet1.setLineWidth(4);
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleColor(Color.GRAY);
        lineDataSet1.setCircleHoleColor(Color.GREEN);
        lineDataSet1.setCircleRadius(10);
        lineDataSet1.setCircleHoleRadius(9);
        lineDataSet1.setValueTextSize(10);
        lineDataSet1.setValueTextColor(Color.BLUE);
        lineDataSet1.enableDashedLine(5 , 10,0);
        lineDataSet1.setColors(colorArray , this );


//        mpLineChart.setBackgroundColor(Color.GREEN);
        mpLineChart.setNoDataText("No Data");

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.RED);
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setFormToTextSpace(10);

        LegendEntry[] legendEntries = new LegendEntry[4];

        for ( int i=0 ; i<legendEntries.length ; i++){

            LegendEntry legendEntry = new LegendEntry();
            legendEntry.formColor = colorClassArray[i];
            legendEntry.label = String.valueOf(legendName[i]);
            legendEntries[i]=legendEntry;

        }

        legend.setCustom(legendEntries);


        Description description = new Description();
        description.setText("Zoo");
        description.setTextColor(Color.BLUE);
        description.setTextSize(20);
        description.setPosition(0.5f, 0.5f);
        mpLineChart.setDescription(description);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();





        // bar chart

        barChart = findViewById(R.id.mp_BarChart);
        BarDataSet barDataSet1 = new BarDataSet(dataValuesBar1() , "Data Set 1");
        BarDataSet barDataSet2 = new BarDataSet(dataValuesBar2() , "Data Set 2");

        barDataSet1.setColor(Color.RED);
        barDataSet2.setColor(Color.BLUE);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);
        barData.addDataSet(barDataSet2);

        Description description2 = new Description();
        description2.setText("Zoo");
        description2.setTextColor(Color.BLUE);
        description2.setTextSize(20);
        description2.setPosition(0.90f, 0.1f);
        barChart.setDescription(description2);

        barChart.setData(barData);
        barChart.invalidate();

    }
    private ArrayList<Entry> dataValues1(){

        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0 , 20));
        dataVals.add(new Entry(1 , 24));
        dataVals.add(new Entry(2 , 2));
        dataVals.add(new Entry(3 , 10));
        dataVals.add(new Entry(4 , 28));

        return dataVals;
    }
    private ArrayList<Entry> dataValues2(){

        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0 , 12));
        dataVals.add(new Entry(1 , 16));
        dataVals.add(new Entry(2 , 23));
        dataVals.add(new Entry(3 , 1));
        dataVals.add(new Entry(4 , 18));

        return dataVals;
    }

    private ArrayList<BarEntry> dataValuesBar1(){

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0 , 3));
        dataVals.add(new BarEntry(1 , 4));
        dataVals.add(new BarEntry(3 , 6));
        dataVals.add(new BarEntry(4 , 11));

        return dataVals;
    }
    private ArrayList<BarEntry> dataValuesBar2(){

        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(1.8f , 2));
        dataVals.add(new BarEntry(2 , 8));
        dataVals.add(new BarEntry(3.6f , 3));
        dataVals.add(new BarEntry(5 , 7));

        return dataVals;
    }

}