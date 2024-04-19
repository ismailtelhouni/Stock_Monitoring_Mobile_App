package com.example.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stock.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    TextInputEditText editTextEmail , editTextPassword ;
    Button buttonLogin ;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    RelativeLayout itemVisibility;
    ImageButton btnShowPassword , btnHidePassword ;
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent = new Intent(getApplicationContext(),SwitchActivity.class);
            startActivity(intent);

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        itemVisibility = findViewById(R.id.item_visibility);
        TextView forgetYourPassword = findViewById(R.id.forgot_your_password);
        btnShowPassword = findViewById(R.id.btn_show_password);
        btnHidePassword = findViewById(R.id.btn_hide_password);
        forgetYourPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        btnHidePassword.setOnClickListener(this);
        btnShowPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.btn_login ){

            showDialog();
            String email,password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if(TextUtils.isEmpty(email)){

                showDialog();
                Toast.makeText(this , "Enter email" , Toast.LENGTH_SHORT ).show();
                return;
            }
            if(TextUtils.isEmpty(password)){

                showDialog();
                Toast.makeText(this , "password email" , Toast.LENGTH_SHORT ).show();
                return;
            }

            signIn(email,password);

        } else if( view.getId() == R.id.forgot_your_password ){
            Toast.makeText(this , "forgot your password " , Toast.LENGTH_SHORT ).show();
        } else if( view.getId() == R.id.btn_show_password ){

            Toast.makeText(this , "change type" , Toast.LENGTH_SHORT ).show();
            editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnShowPassword.setVisibility(View.GONE);
            btnHidePassword.setVisibility(View.VISIBLE);

        } else if( view.getId() == R.id.btn_hide_password ){

            Toast.makeText(this , "change type" , Toast.LENGTH_SHORT ).show();
            editTextPassword.setInputType( InputType.TYPE_TEXT_VARIATION_PASSWORD );
            btnHidePassword.setVisibility(View.GONE);
            btnShowPassword.setVisibility(View.VISIBLE);

        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    Toast.makeText( this , "Login Successful" , Toast.LENGTH_SHORT ).show();
                    hideDialog();
                    Intent intent = new Intent( this , SwitchActivity.class );
                    startActivity(intent);

                } else {

                    Toast.makeText( this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            });
    }

    public void showDialog(){
        progressBar.setVisibility(View.VISIBLE);
        itemVisibility.setVisibility(View.GONE);
    }
    public void hideDialog(){
        progressBar.setVisibility(View.GONE);
        itemVisibility.setVisibility(View.VISIBLE);
    }
}