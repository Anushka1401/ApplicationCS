package com.example.applicationcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewingUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    List<ModelUser> list=new ArrayList<>();
    AdapterUser adapter;

    //Drawer
    static final float END_SCALE=0.7f;
    LinearLayout contentView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_user);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        recyclerView=findViewById(R.id.recyclerView_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addData();

        menuIcon = findViewById(R.id.menu_icon);
        contentView=findViewById(R.id.content);

        //Menu Hooks
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);


        navigationDrawer();

    }

    private void addData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("isOperator", "1")//looks for the corresponding value with the field
                // in the database
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                //opslist.append("\n Operator Name: " +(CharSequence) document.get("FullName"));
                                list.add(new ModelUser(R.drawable.dp,String.valueOf((CharSequence) document.get("FullName")),
                                        String.valueOf((CharSequence) document.get("uId")),
                                        String.valueOf((CharSequence) document.get("PhoneNumber")),
                                        String.valueOf((CharSequence) document.get("UserEmail"))));
                                adapter = new AdapterUser(ViewingUser.this,list,list.size());
                                recyclerView.setAdapter(adapter);

                                // These values must exactly match the fields in the db

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_op);

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

            case R.id.nav_home:
                Intent intent = new Intent (this, Admin.class);
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

}