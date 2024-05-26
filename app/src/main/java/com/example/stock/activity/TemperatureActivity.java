package com.example.stock.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TemperatureActivity extends AppCompatActivity {

    private TextView temperatureTextView;
    private DatabaseReference temperatureRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        // Initialize Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        temperatureRef = database.getReference("sonsor").child("temperature");

        // Initialize views
        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Fetch temperature value from Realtime Database
        fetchTemperature();
    }

    private void fetchTemperature() {
        // Query the temperature data, order by key in descending order and limit to 1
//        temperatureRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
////                    listener.onFetchFailure(task.getException());
//                }
//                else {
//                    Log.d("dataFromFirebase", String.valueOf(task.getResult().toString()));
//                    Integer value = task.getResult().getValue(Integer.class);
//                    if (value != null) {
////                        stock.setValue(value);
//                        Log.d("dataFromFirebase", "DataSnapshot exists, :" + value);
////                        listener.onFetchListener(stock);
//                    } else {
//                        Log.d("dataFromFirebase", "DataSnapshot exists, mais la valeur est null ");
//                    }
//                }
//            }
//        });

        temperatureRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the last entry
                    DataSnapshot lastEntry = dataSnapshot.getChildren().iterator().next();

                    // Get the temperature value from the last entry
                    Double temperature = lastEntry.child("temperature").getValue(Double.class);

                    // Display temperature on TextView
                    temperatureTextView.setText(temperature + "°C");
                } else {
                    // Temperature data does not exist
                    temperatureTextView.setText("No temperature data available");
                    Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error getting temperature data
                temperatureTextView.setText("Error fetching temperature data");
                
            }
        });
    }
}
