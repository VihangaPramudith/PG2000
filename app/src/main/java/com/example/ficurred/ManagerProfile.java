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

public class ManagerProfile extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    EditText emailAddress, password;

    SharedPreferences sharedPreferences;
    String s_emailAddress;

    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manager_profile);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        s_emailAddress = sharedPreferences.getString(EMAIL_KEY, null);

        manager = new Manager();
    }

    public void updateProfile(View view) {
        emailAddress = findViewById(R.id.add_admin_email);
        password = findViewById(R.id.add_admin_password);

        DatabaseReference upRef = FirebaseDatabase.getInstance().getReference().child("Manager");

        if (TextUtils.isEmpty(emailAddress.getText().toString()) && TextUtils.isEmpty(password.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please input data", Toast.LENGTH_SHORT).show();
        else if (!TextUtils.isEmpty(emailAddress.getText().toString()) && TextUtils.isEmpty(password.getText().toString())) {
            upRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        manager = dataSnapshot.getValue(Manager.class);
                        if (manager.getEmailAddress().equals(s_emailAddress)) {

                            DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference().child("Manager").child(dataSnapshot.getKey());
                            dbRef1.removeValue();

                            manager.setEmailAddress(emailAddress.getText().toString());
                            updateAdminProfiles(emailAddress.getText().toString());

                            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference();
                            dbRef2.child("Manager").push().setValue(manager);

                            Toast.makeText(getApplicationContext(), "Please Sign In again", Toast.LENGTH_SHORT).show();

                            logOut();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (TextUtils.isEmpty(emailAddress.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
            upRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        manager = dataSnapshot.getValue(Manager.class);
                        if (manager.getEmailAddress().equals(s_emailAddress)) {

                            DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference().child("Manager").child(dataSnapshot.getKey());
                            dbRef1.removeValue();

                            manager.setPassword(password.getText().toString());

                            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference();
                            dbRef2.child("Manager").push().setValue(manager);

                            Toast.makeText(getApplicationContext(), "Please Sign In again", Toast.LENGTH_SHORT).show();

                            logOut();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (!TextUtils.isEmpty(emailAddress.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
            upRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        manager = dataSnapshot.getValue(Manager.class);
                        if (manager.getEmailAddress().equals(s_emailAddress)) {

                            DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference().child("Manager").child(dataSnapshot.getKey());
                            dbRef1.removeValue();

                            manager.setEmailAddress(emailAddress.getText().toString());
                            updateAdminProfiles(emailAddress.getText().toString());
                            manager.setPassword(password.getText().toString());

                            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference();
                            dbRef2.child("Manager").push().setValue(manager);

                            Toast.makeText(getApplicationContext(), "Please Sign In again", Toast.LENGTH_SHORT).show();

                            logOut();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void updateAdminProfiles(String newManager) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin admin;

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    admin = dataSnapshot.getValue(Admin.class);

                    if (admin.getManager().equals(s_emailAddress)) {

                        DatabaseReference dbRef_new = FirebaseDatabase.getInstance().getReference().child("Admin").child(dataSnapshot.getKey());
                        dbRef_new.removeValue();

                        admin.setManager(newManager);

                        DatabaseReference pushRef = FirebaseDatabase.getInstance().getReference();
                        pushRef.child("Admin").push().setValue(admin);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteUser(View view) {
        password = findViewById(R.id.admin_email);

        if (TextUtils.isEmpty(password.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please input your password", Toast.LENGTH_SHORT).show();
        else {

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Manager");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean isPasswordIncorrect = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        manager = dataSnapshot.getValue(Manager.class);
                        if (manager.getEmailAddress().equals(s_emailAddress) && manager.getPassword().equals(password.getText().toString())) {

                            isPasswordIncorrect = false;
                            DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Manager").child(dataSnapshot.getKey());
                            deleteRef.removeValue();
                            Toast.makeText(getApplicationContext(), "All the data relating to your profile will be deleted...", Toast.LENGTH_SHORT).show();
                            break;

                        } else
                            isPasswordIncorrect = true;
                    }
                    if (isPasswordIncorrect) {
                        Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        deleteAdminProfiles();
                        logOut();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void deleteAdminProfiles() {
        DatabaseReference dbRef_new = FirebaseDatabase.getInstance().getReference().child("Admin");
        dbRef_new.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Admin admin;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    admin = dataSnapshot.getValue(Admin.class);

                    if (admin.getManager().equals(s_emailAddress)) {

                        DatabaseReference delRef_new = FirebaseDatabase.getInstance().getReference().child("Admin").child(dataSnapshot.getKey());
                        delRef_new.removeValue();
                        logOut();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}