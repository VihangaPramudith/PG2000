package com.example.ficurred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerViewAdminProfiles extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    LinearLayout layout_view;

    SharedPreferences sharedPreferences;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manager_view_admin_profiles);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        emailAddress = sharedPreferences.getString(EMAIL_KEY, null);

        layout_view = (LinearLayout) findViewById(R.id.income_layout_view);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Admin admin;

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    admin = dataSnapshot.getValue(Admin.class);

                    if (admin.getManager().equals(emailAddress)) {

                        EditText editText = new EditText(getApplicationContext());
                        editText.setText(admin.getEmailAddress());
                        editText.setTextColor(Color.rgb(102, 39, 59));
                        editText.setTextSize(16);
                        layout_view.addView(editText, layoutParams);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}