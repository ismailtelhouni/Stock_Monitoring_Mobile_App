package com.example.stock.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.stock.dto.CountStock;
import com.example.stock.dto.ProductsQuality;
import com.example.stock.dto.Temperature;
import com.example.stock.model.Stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

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

        long[] lastWeekTimestamps = getLastWeekTimestamps(Calendar.getInstance());
        long startOfLastWeek = lastWeekTimestamps[0];
        long endOfLastWeek = lastWeekTimestamps[1];

        Log.d("Timestamp", "Start of last week: " + startOfLastWeek);
        Log.d("Timestamp", "End of last week: " + endOfLastWeek);

        DatabaseReference databaseReference = db.getReference("stock");

        LinkedList<Stock> stocks =new LinkedList<>();
        Query inQuery = databaseReference.child("in");
        Query outQuery = databaseReference.child("out");
        inQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    Log.d("Firebase", "In Query DataSnapshot: " + dataSnapshot.toString());

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Stock stock = new Stock();

                        Object classificationValue = snapshot.child("classification").getValue();

                        // Check if the classification value is of type Double
//                        if (classificationValue instanceof String) {
//                            Log.e(TAG, "Skipping key: " + snapshot.getKey() + " because classification is not a Double.");
//                            continue; // Skip this iteration if the value is not a Double
//                        } else {
//                            Double classification = (Double) classificationValue;
//                            stock.setCategory(classification);
//                        }
                        if (classificationValue instanceof Double) {
                            Double classification = (Double) classificationValue;
                            stock.setCategory(classification);
                        } else if (classificationValue instanceof Long) {
                            Double classification = ((Long) classificationValue).doubleValue();
                            stock.setCategory(classification);
                        } else {
                            Log.e(TAG, "Skipping key: " + snapshot.getKey() + " because classification is not a Double or Long.");
                            continue; // Skip this iteration if the value is not a Double or Long
                        }

                        stock.setType("in");
                        stock.setId(snapshot.getKey());
                        stock.setName(snapshot.child("name").getValue(String.class));
                        stock.setQuantity(snapshot.child("quantity").getValue(Double.class));
                        Double timestamp = snapshot.child("timestamp").getValue(Double.class);
                        Date date = null;
                        if ( timestamp != null) {
                            long entryDateMillis = timestamp.longValue();

                            date = new Date(entryDateMillis);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                            String formattedDate = sdf.format(date);

                            // Afficher la date formatée
                            Log.d(TAG, "Formatted Date: " + formattedDate);

                        }
                        stock.setEntryDate(timestamp);
                        stock.setDate(date);


                        stocks.add(stock);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "In Query Cancelled: " + e.getMessage());
                listener.onStocksFetchFailure(e);
            }
        });

        outQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    Log.d("Firebase", "Out Query DataSnapshot: " + dataSnapshot.toString());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Stock stock = new Stock();
                        stock.setType("out");
                        stock.setId(snapshot.getKey());
                        stock.setName(snapshot.child("name").getValue(String.class));
                        stock.setCategory(snapshot.child("classification").getValue(Double.class));
                        stock.setQuantity(snapshot.child("quantity").getValue(Double.class));
                        Double timestamp = snapshot.child("timestamp").getValue(Double.class);
                        Date date = null;
                        if ( timestamp != null) {
                            long entryDateMillis = timestamp.longValue();

                            date = new Date(entryDateMillis);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                            String formattedDate = sdf.format(date);
                            // Afficher la date formatée
                            Log.d(TAG, "Formatted Date: " + formattedDate);
                        }
                        stock.setEntryDate(timestamp);
                        stock.setDate(date);
                        stocks.add(stock);
                    }
                }
                Log.d("TAG","stocks : "+ stocks );
                listener.onStocksFetchSuccess(stocks);
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "In Query Cancelled: " + e.getMessage());
                listener.onStocksFetchFailure(e);
            }
        });
    }
    public void getCountStock( OnCountStockFetchListener listener ){

        DatabaseReference databaseReference = db.getReference("stock");

        Query inQuery = databaseReference.child("in");
        Query outQuery = databaseReference.child("out");

        CountStock countStock = new CountStock();
            inQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Log.d("Firebase", "In Query DataSnapshot: " + dataSnapshot.toString());

                        double count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String key = snapshot.getKey();
                            Double quantity = snapshot.child("quantity").getValue(Double.class);
                            if (quantity != null) {
                                count += quantity;
                            } else {
                                Log.e(TAG, "Quantity is null for key: " + key);
                            }
                        }
                        countStock.setNbrTotal(count);
                    }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "In Query Cancelled: " + e.getMessage());
                listener.onCountStockFetchFailure(e);
            }
        });

        outQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    Log.d("Firebase", "Out Query DataSnapshot: " + dataSnapshot.toString());
                    double count = countStock.getNbrTotal();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String key = snapshot.getKey();
                        Double quantity = snapshot.child("quantity").getValue(Double.class);
                        if (quantity != null) {
                            count -= quantity;
                        } else {
                            Log.e(TAG, "Quantity is null for key: " + key);
                        }
                    }
                    countStock.setNbrTotal(count);
                }
                listener.onCountStockFetchSuccess(countStock);
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "In Query Cancelled: " + e.getMessage());
                listener.onCountStockFetchFailure(e);
            }
        });

    }

    public void getProductsQualityToday( OnProductsQualityTodayFetchListener listener ) {

        DatabaseReference databaseReference = db.getReference("stock");

        Query inQuery = databaseReference.child("in");
        Query outQuery = databaseReference.child("out");

        ProductsQuality productsQuality = new ProductsQuality( 0 , 0 , 0 );

        inQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for( DataSnapshot data : task.getResult().getChildren() ){

                        String key = data.getKey();
                        Double quantity = data.child("quantity").getValue(Double.class);
                        double classification ;
                        Object classificationValue = data.child("classification").getValue();

                        if (classificationValue instanceof Double) {
                            classification = (Double) classificationValue;
                        } else if (classificationValue instanceof Long) {
                            classification = ((Long) classificationValue).doubleValue();
                        } else {
                            Log.e(TAG, "Skipping key: " + key + " because classification is not a Double or Long.");
                            continue; // Skip this iteration if the value is not a Double or Long
                        }

                        if (quantity != null) {
                            if (classification == 0){
                                productsQuality.setRipe( productsQuality.getRipe() + quantity);
                            } else if (classification == 1) {
                                productsQuality.setUnderripe( productsQuality.getUnderripe() + quantity);
                            } else if (classification == -1) {
                                productsQuality.setOverripe( productsQuality.getOverripe() + quantity );
                            }
                        } else {
                            Log.e(TAG, "Quantity is null for key: " + key);
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onProductsQualityTodayFetchFailure(e);
            }
        });
        outQuery.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for( DataSnapshot data : task.getResult().getChildren() ){

                        String key = data.getKey();
                        Double quantity = data.child("quantity").getValue(Double.class);
                        Double classification = data.child("classification").getValue(Double.class);

                        if (quantity != null) {
                            if (classification == 0){
                                productsQuality.setRipe( productsQuality.getRipe() - quantity);
                            } else if (classification == 1) {
                                productsQuality.setUnderripe( productsQuality.getUnderripe() - quantity);
                            } else if (classification == -1) {
                                productsQuality.setOverripe( productsQuality.getOverripe() - quantity );
                            }
                        } else {
                            Log.e(TAG, "Quantity is null for key: " + key);
                        }

                    }
                    listener.onProductsQualityTodayFetchSuccess(productsQuality);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onProductsQualityTodayFetchFailure(e);
            }
        });
    }

    public void getTemperature(OnTemperatureFetchListener listener) {

        DatabaseReference databaseReference = db.getReference("sonsor").child("temperature");
        Temperature temperature = new Temperature();
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){

                    DataSnapshot dataSnapshot = task.getResult().child("last");
                    Log.d(TAG, dataSnapshot.toString());

                    // Vérification et récupération de la température
                    Double temperatureValue = dataSnapshot.child("temperature").getValue(Double.class);
                    if (temperatureValue != null) {
                        temperature.setTemperature(temperatureValue);
                    } else {
                        temperature.setTemperature(0.0); // Exemple de valeur par défaut
                        Log.e(TAG, "La température est null");
                    }

                    // Vérification et récupération de l'humidité
                    Double humidityValue = dataSnapshot.child("humidity").getValue(Double.class);
                    if (humidityValue != null) {
                        temperature.setHumidity(humidityValue);
                    } else {
                        temperature.setHumidity(0.0); // Exemple de valeur par défaut
                        Log.e(TAG, "L'humidité est null");
                    }

                    // Vérification et récupération de la date
                    Double dateValue = dataSnapshot.child("date").getValue(Double.class);
                    if (dateValue != null) {
                        temperature.setDate(dateValue);
                    } else {
                        temperature.setDate(0.0); // Exemple de valeur par défaut
                        Log.e(TAG, "La date est null");
                    }

                    // Vérification et récupération de la validité
                    Boolean isValidValue = dataSnapshot.child("isValid").getValue(Boolean.class);
                    if (isValidValue != null) {
                        temperature.setValid(isValidValue);
                    } else {
                        temperature.setValid(false); // Exemple de valeur par défaut
                        Log.e(TAG, "La validité est null");
                    }

                    // Appel du listener de succès
                    listener.onTemperatureFetchSuccess(temperature);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onTemperatureFetchFailure(e);
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
    public interface OnProductsQualityTodayFetchListener {
        void onProductsQualityTodayFetchSuccess(ProductsQuality qualityToday);
        void onProductsQualityTodayFetchFailure(Exception e);
    }
    public interface OnTemperatureFetchListener {
        void onTemperatureFetchSuccess(Temperature temperature);
        void onTemperatureFetchFailure(Exception e);
    }
    public long[] getLastWeekTimestamps(Calendar date) {
        Calendar calendar = (Calendar) date.clone();

        // Récupérer le début de la semaine courante
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Récupérer le début de la semaine dernière
        long startOfLastWeek = calendar.getTimeInMillis();

        // Récupérer la fin de la semaine dernière
        calendar.add(Calendar.DAY_OF_WEEK, -6);
        long endOfLastWeek = calendar.getTimeInMillis();

        return new long[]{startOfLastWeek, endOfLastWeek};
    }
}
