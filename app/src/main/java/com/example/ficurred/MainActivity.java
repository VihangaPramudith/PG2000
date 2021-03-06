package com.example.ficurred;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }

    public void redirectToManager(View view) {
        Intent intent = new Intent(this, ManagerSignIn.class);
        startActivity(intent);
    }

    public void redirectToAdmin(View view) {
        Intent intent = new Intent(this, AdminSignIn.class);
        startActivity(intent);
    }
}