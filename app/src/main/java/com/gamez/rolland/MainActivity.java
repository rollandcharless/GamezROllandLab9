package com.gamez.rolland;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference root;

    private EditText fullName;
    private EditText age;
    private EditText gender;

    private String saveCurrentDate = "";
    private String saveCurrentTime = "";
    private String uniqueId = "";

    private TextView rName;
    private TextView rAge;
    private TextView rGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        root = db.getReference("Users");
        fullName = findViewById(R.id.tName);
        age = findViewById(R.id.tAge);
        gender = findViewById(R.id.tGender);

        rName = findViewById(R.id.rName);
        rAge = findViewById(R.id.rAge);
        rGender = findViewById(R.id.rGender);
//        keylist = new ArrayList<>();
//        init();
    }

    public void saveData(EditText fullName, EditText age, EditText gender) {

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM:dd:yyyy");
        saveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss:SSS");
        saveCurrentTime = currentTime.format(calendarTime.getTime());

        uniqueId = saveCurrentDate.concat(saveCurrentTime);

        String getFullName = fullName.getText().toString().trim();
        String getAge = age.getText().toString().trim();
        String getGender = gender.getText().toString().trim();

        User user = new User(getFullName, getAge, getGender);

        root.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "saving is Successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    public void getData() {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nameData = dataSnapshot.child("fullname").getValue(String.class);
                    String ageData = dataSnapshot.child("age").getValue(String.class);
                    String genderData = dataSnapshot.child("gender").getValue(String.class);

                    rName.setText(nameData);
                    rAge.setText(ageData);
                    rGender.setText(genderData);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void option(View v) {
        if (v.getId() == R.id.btnSave) {
            saveData(fullName, age, gender);
        }
        if (v.getId() == R.id.searchBtn) {
            getData();
        }
    }
}
