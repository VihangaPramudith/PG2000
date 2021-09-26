package com.example.ficurred;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class ManagerDashboard extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedPreferences;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manager_dashboard);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        emailAddress = sharedPreferences.getString(EMAIL_KEY, null);
    }

    public void redirectToManageProfiles(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EMAIL_KEY, emailAddress);

        editor.apply();

        Intent intent = new Intent(this, ManagerProfile.class);
        startActivity(intent);
    }

    public void redirectToAdminProfiles(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EMAIL_KEY, emailAddress);

        editor.apply();

        Intent intent = new Intent(this, ManagerAdminHandling.class);
        startActivity(intent);
    }

    public void redirectToIncomeReport(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EMAIL_KEY, emailAddress);

        editor.apply();

        Intent intent = new Intent(this, ManagerIncomeReport.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}