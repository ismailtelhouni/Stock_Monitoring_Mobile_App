package com.example.stock.dao;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.stock.dto.CountStock;
import com.example.stock.model.Stock;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class StockDao
{
    private static final String TAG = "TAGStockDao";
    private final FirebaseUser currentUser;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final FirebaseDatabase db;

    public StockDao(FirebaseDatabase db , FirebaseAuth mAuth, Context context, FragmentManager fragmentManager ) {
        this.db = db;
        this.currentUser = mAuth.getCurrentUser();
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void getStocks( OnStocksFetchListener listener ){
        LinkedList<Stock> stocks =new LinkedList<>();
        DatabaseReference stockRef = db.getReference("stock");

        stockRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                listener.onStocksFetchFailure(task.getException());
            }
            else {
                Log.d(TAG, String.valueOf(task.getResult().getValue()));
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String category = childSnapshot.getKey();
                        for (DataSnapshot dataType : childSnapshot.getChildren()) {

                            String type = dataType.getKey();
                            for (DataSnapshot data : dataType.getChildren()){

                                String key = data.getKey();
                                Log.d( TAG , "key : " + key);
                                Double quantity = data.child("quantity").getValue(Double.class);
                                Double entryDate = data.child("entryDate").getValue(Double.class);
                                Date date = null;
                                if ( entryDate != null) {
                                    long entryDateMillis = entryDate.longValue();

                                    date = new Date(entryDateMillis);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                                    String formattedDate = sdf.format(date);

                                    // Afficher la date formatée
                                    Log.d(TAG, "Formatted Date: " + formattedDate);

                                }

                                String name = data.child("name").getValue(String.class);
                                Stock stock = new Stock( name , quantity ,entryDate ,category , type );
                                stock.setId( key );
                                stock.setDate(date);
                                stocks.add(stock);
                            }
                        }
                    }
                }
                listener.onStocksFetchSuccess(stocks);
            }
        });

    }
    public void getCountStock( OnCountStockFetchListener listener ){
        CountStock countStock = new CountStock();

        DatabaseReference stockRef = db.getReference("stock");
        final double[] count = {0};
        stockRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                listener.onCountStockFetchFailure(task.getException());
            }
            else {

                Log.d(TAG, String.valueOf(task.getResult().getValue()));
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataType : childSnapshot.getChildren()) {
                            for (DataSnapshot data : dataType.getChildren()){

                                String key = data.getKey();
                                Double quantity = data.child("quantity").getValue(Double.class);
                                if (quantity != null) {
                                    count[0] += quantity;
                                } else {
                                    Log.e(TAG, "Quantity is null for key: " + key);
                                }
                            }
                        }
                    }
                }
                countStock.setNbrTotal(count[0]);
                listener.onCountStockFetchSuccess(countStock);
            }
        });

    }
    public interface OnStocksFetchListener {
        void onStocksFetchSuccess(LinkedList<Stock> stocks);
        void onStocksFetchFailure(Exception e);
    }
    public interface OnCountStockFetchListener {
        void onCountStockFetchSuccess(CountStock countStock);
        void onCountStockFetchFailure(Exception e);
    }

}
