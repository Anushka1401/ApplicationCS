package com.example.applicationcs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final String log = "Alert:";
    static final float END_SCALE=0.7f;

    TextView adminname;
    String phonenumber;
    LinearLayout contentView;
    LottieAnimationView lottie;

    //Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getData();

        menuIcon = findViewById(R.id.menu_icon);
        contentView=findViewById(R.id.content);

        //bottomAnimation
        lottie = findViewById(R.id.coworkers);
        lottie.animate();
        lottie.setRepeatCount(Animation.INFINITE);

        //Menu Hooks
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);


        navigationDrawer();

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        ActivityCompat.requestPermissions(Admin.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        SmsManager mySmsManager = SmsManager.getDefault();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference(date);
        reference.keepSynced(true);


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                Log.i(log, "child added");
                HelperClass helperClass = snapshot.getValue(HelperClass.class);

                String empid = helperClass.getEmpid();
                String empname = helperClass.getEmpname();
                String temp = helperClass.getTemperature();
                int temperature = Integer.parseInt(temp);


                if (temperature >= 99) {
                    Log.e("SEE",phonenumber);
                    Toast.makeText(Admin.this, "ALERT! Employee " + empid + " - " + empname + " has a temperature of " + temperature + " F", Toast.LENGTH_LONG).show();
                    mySmsManager.sendTextMessage(phonenumber, null, "ALERT! Employee " + empid + " - " + empname + " has a temperature of " + temperature + " F", null, null);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
                Log.i(log, String.valueOf(snapshot.getValue()));
                HelperClass helperClass = snapshot.getValue(HelperClass.class);

                String empid = helperClass.getEmpid();
                String empname = helperClass.getEmpname();
                String temp = helperClass.getTemperature();
                int temperature = Integer.parseInt(temp);


                SmsManager mySmsManager = SmsManager.getDefault();


                if (temperature >= 99) {
                    Log.i(log, empname);
                    Toast.makeText(Admin.this, "ALERT! Employee " + empid + " - " + empname + " has a temperature of " + temperature + " F", Toast.LENGTH_LONG).show();
                    mySmsManager.sendTextMessage(phonenumber, null, "ALERT! Employee " + empid + " - " + empname + " has a temperature of " + temperature + " F", null, null);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(log, "Something went wrong");
            }
        });
    }

    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one
        drawerLayout.setScrimColor(getResources().getColor(R.color.white));
        //To make it transparent use drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_logout:
                logoutAdmin();
                break;

            case R.id.nav_op:
                Intent intent = new Intent (this, ViewingUser.class);
                animateNavigationDrawer();
                startActivity(intent);
                break;

            case R.id.nav_adm:
                Intent intenta = new Intent (this, ViewingAdmin.class);
                animateNavigationDrawer();
                startActivity(intenta);
                break;

            case R.id.nav_data:
                Intent intentb = new Intent (this, Data.class);
                animateNavigationDrawer();
                startActivity(intentb);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logoutAdmin() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String current = user.getUid();//getting unique user id

        adminname = findViewById(R.id.adminname);
        db.collection("Users")
                .whereEqualTo("uId", current)//looks for the corresponding value with the field
                // in the database
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                adminname.setText("Admin " + (CharSequence) document.get("FullName") + "\n is currently logged in");
                                phonenumber= String.valueOf((CharSequence) document.get("PhoneNumber"));

                                // These values must exactly match the fields in the db

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void check(View v){
        EditText datedet = findViewById(R.id.datedetail);
        Intent intent = new Intent(this, Retrievetotable.class);
        intent.putExtra("datedetail",datedet.getText().toString());
        startActivity(intent);
    }


}