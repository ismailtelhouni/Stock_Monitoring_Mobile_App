package com.example.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stock.R;
import com.google.firebase.auth.FirebaseAuth;

public class SwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        // Initialize buttons
        findViewById(R.id.mainActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        findViewById(R.id.temperatureActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTemperatureActivity();
            }
        });

        findViewById(R.id.stockStateActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStockStateActivity();
            }
        });

    }

    // Method to navigate to MainActivity
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to navigate to TemperatureActivity
    private void goToTemperatureActivity() {
        Intent intent = new Intent(this, TemperatureActivity.class);
        startActivity(intent);
    }

    private void goToStockStateActivity() {
        Intent intent = new Intent(this, StockInOutActivity.class);
        startActivity(intent);
    }

    public void logout(View view){

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    public void goToChartActivity(View view){

        Intent intent = new Intent( this, MpChartActivity.class);
        startActivity(intent);
        finish();

    }
    public void goToNewStockActivity(View view ){
        Intent intent = new Intent( this, NewStockActivity.class);
        startActivity(intent);
        finish();
    }
}

