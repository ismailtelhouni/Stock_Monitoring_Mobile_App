package com.example.stock.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stock.R;
import com.example.stock.dao.StockDao;
import com.example.stock.model.Stock;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class StockInOutActivity extends AppCompatActivity {

    private static final String TAG = "TAGStockInOutActivity";
    TextView stockTextView , underRipe , ripeValue , overripeValue;
    StockDao stockDao;
    private Map<Integer, Double> ripeMap = new HashMap<>();
    private Map<Integer, Double> underripeMap = new HashMap<>();
    private Map<Integer, Double> overripeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.content_stock_in_out);

        stockDao = new StockDao( FirebaseDatabase.getInstance() , FirebaseAuth.getInstance() , this , getSupportFragmentManager() );

        stockTextView = findViewById(R.id.stockTextView);
        underRipe = findViewById(R.id.under_ripe);
        ripeValue = findViewById(R.id.ripe_value);
        overripeValue = findViewById(R.id.overripe_value);

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

            }
        });
    }
    private void getLastStocks(LinkedList<Stock> stocks) {

        Calendar calendar = Calendar.getInstance();

        // Réinitialisez les quantités pour chaque jour
        ripeMap.put(1, 0.0);
        underripeMap.put(1, 0.0);
        overripeMap.put(1, 0.0);

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
                        overripeMap.put(1, overripeMap.get(1) + var*stock.getQuantity());
                    } else if (stock.getCategory() == -1) {
                        underripeMap.put(1, underripeMap.get(1) + var*stock.getQuantity());
                    } else if (stock.getCategory() == 0) {
                        ripeMap.put(1,ripeMap.get(1)+var*stock.getQuantity());
                    }
                }
            }
        }

        Log.d(TAG , "tessssssssssssssst "+ ripeMap.get(0)+" hhhh " +ripeMap.toString() + underripeMap.toString() + overripeMap.toString());

        underRipe.setText(String.valueOf(underripeMap.get(1)));
        ripeValue.setText(String.valueOf(ripeMap.get(1)));
        overripeValue.setText(String.valueOf(overripeMap.get(1)));
        stockTextView.setText(String.valueOf( underripeMap.get(1) + ripeMap.get(1) +overripeMap.get(1) ));

    }

}