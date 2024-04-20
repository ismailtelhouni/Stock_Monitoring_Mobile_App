package com.example.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.stock.R;
import com.example.stock.databinding.ActivityMainBinding;
import com.example.stock.fragment.HomeFragment;
import com.example.stock.fragment.NotificationsFragment;
import com.example.stock.fragment.TestFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAGMainActivity";
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.tool_bar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menu_notification){
                    Toast.makeText( getApplicationContext() , "Vous avez cliqué sur le menu notification",Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId()==R.id.logout) {
                    Toast.makeText( getApplicationContext() , "Vous avez cliqué sur le menu logout",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent( getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if(itemId == R.id.home){
                replaceFragment(new HomeFragment());
            } else if( itemId == R.id.add_new_task ){
                replaceFragment(new TestFragment());
            } else if( itemId == R.id.notification ){
                replaceFragment(new NotificationsFragment());
            }
            return true;
        });

    }

    private void replaceFragment( Fragment fragment ) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}