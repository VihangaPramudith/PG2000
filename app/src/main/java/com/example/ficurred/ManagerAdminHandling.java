package com.example.ficurred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerAdminHandling extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedPreferences;
    String emailAddress;

    boolean isUserFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manager_admin_handling);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        emailAddress = sharedPreferences.getString(EMAIL_KEY, null);
    }

    public void redirectToAdminProfilesView(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EMAIL_KEY, emailAddress);

        editor.apply();

        Intent intent = new Intent(this, ManagerViewAdminProfiles.class);
        startActivity(intent);
    }

    public void createAdmin(View view) {
        EditText adminEmail = findViewById(R.id.add_admin_email);
        EditText adminPassword = findViewById(R.id.add_admin_password);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        if (TextUtils.isEmpty(adminEmail.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(adminPassword.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
        else {

            Admin admin = new Admin();

            DatabaseReference dbRefChk = FirebaseDatabase.getInstance().getReference().child("Admin");

            dbRefChk.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    isUserFound = true;
                    Admin admin;

                    for (DataSnapshot snapShot: snapshot.getChildren()) {

                        admin = snapShot.getValue(Admin.class);
                        if (admin.getEmailAddress().equals(adminEmail.getText().toString())) {

                            isUserFound = false;
                            break;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (isUserFound) {
                admin.setEmailAddress(adminEmail.getText().toString());
                admin.setPassword(adminPassword.getText().toString());
                admin.setManager(emailAddress);
                dbRef.child("Admin").push().setValue(admin);

                Toast.makeText(getApplicationContext(), "Administrator Profile created successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Administrator Profile already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAdmin(View view) {
        EditText adminEmail = findViewById(R.id.admin_email);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isUserFound = true;
                Admin admin;

                for (DataSnapshot snapShot: snapshot.getChildren()) {

                    admin = snapShot.getValue(Admin.class);
                    if (admin.getEmailAddress().equals(adminEmail.getText().toString())) {

                        isUserFound = false;
                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(snapShot.getKey());
                        delRef.removeValue();
                        Toast.makeText(getApplicationContext(), "Administrator Profile Deleted Successfully", Toast.LENGTH_SHORT).show();
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (isUserFound) {
            Toast.makeText(getApplicationContext(), "Please Enter Correct Email Address", Toast.LENGTH_SHORT).show();
        }
    }
}