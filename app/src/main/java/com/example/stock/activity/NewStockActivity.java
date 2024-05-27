package com.example.stock.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewStockActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAGNewStockActivity";
    private TextInputEditText titleEditText , descriptionEditText , typeEditText , inOutEditText, startDateEditText , endDateEditText;
    private ProgressBar progressBar;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Calendar calendar;
    private Button btnSaveTask;
    private CardView btnDate , btnTime;
    private TextView textDate,textTime ;
    private String StringTime;
    private FirebaseDatabase db;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        Log.d(TAG,"user :"+currentUser.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        titleEditText = findViewById(R.id.title);
        descriptionEditText = findViewById(R.id.description);
        typeEditText = findViewById(R.id.type);
        inOutEditText = findViewById(R.id.in_out);
        btnSaveTask = findViewById(R.id.btn_save_task);
        progressBar = findViewById(R.id.progressBar);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        textDate = findViewById(R.id.item_date_time);
        textTime = findViewById(R.id.item_card_time);
        calendar = Calendar.getInstance();

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnSaveTask.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_date){

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this ,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            textDate.setText(dayOfMonth + "." + (month + 1) + "." + year);
                        }
                    }, year, month, dayOfMonth);

            datePickerDialog.show();

        } else if ( view.getId()==R.id.btn_time ) {

            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            textTime.setText(hourOfDay + ":" + minute);

                            StringTime = hourOfDay + ":" + minute;
                            Log.d(TAG , "timesdddddddd :"+StringTime);

                        }
                    }, hourOfDay, minute, true);
            timePickerDialog.show();
        }
        else if (view.getId()==R.id.btn_save_task) {

//            taskItem.setTitle(String.valueOf(titleEditText.getText()));
//            taskItem.setDescription(String.valueOf(descriptionEditText.getText()));
//            taskItem.setDate(String.valueOf(textDate.getText()));
//            taskItem.setTime((String) textTime.getText());
            Map<String, Object> stock = new HashMap<>();
            Map<String, String> timestamp = ServerValue.TIMESTAMP;

            Log.d("TAG" , "testeknkj");

            stock.put("name",String.valueOf(titleEditText.getText()));
            stock.put("quantity",Double.valueOf(String.valueOf(descriptionEditText.getText())));
            stock.put("timestamp",timestamp);
            stock.put("classification",Double.valueOf(Double.parseDouble(String.valueOf(typeEditText.getText()))));

            DatabaseReference stockRef = db.getReference("stock").child(String.valueOf(inOutEditText.getText()));

            // Générer une clé unique pour le nouvel élément de stock
            String stockKey = stockRef.push().getKey();

            // Enregistrez le stock avec la clé générée
            assert stockKey != null;
            stockRef.child(stockKey).setValue(stock)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(NewStockActivity.this , " add Success " , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent( getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewStockActivity.this , " error : "+e.toString() , Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}