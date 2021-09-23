package com.example.applicationcs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Data extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {
    TextView textView1;
    TextView textView2;
    final String log = "Alert:";
    String currentDateString;
    boolean startorend;

    //Drawer
    static final float END_SCALE=0.7f;
    LinearLayout contentView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
     MenuItem menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu2);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuIcon = findViewById(android.R.id.home);
        contentView=findViewById(R.id.linearLayout);

        //Menu Hooks
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);

        navigationDrawer();

        //date range
        textView1= findViewById(R.id.startdate);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datepicker");
                startorend=true;
            }
        });

        textView2 = findViewById(R.id.enddate);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker2 = new DatePickerFragment();
                datePicker2.show(getSupportFragmentManager(), "datepicker2");
                startorend=false;
            }
        });

        //tabs
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabItem tabTable = findViewById(R.id.tabTable);
        TabItem tabGraph = findViewById(R.id.tabGraph);
        TabItem tabPie = findViewById(R.id.tabPie);
        ViewPager viewPager = findViewById(R.id.view_pager);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);

        //tab states
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        currentDateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.getTime());

        textView1=findViewById(R.id.startdate);
        textView2=findViewById(R.id.enddate);


        if (startorend==true) {
            textView1.setText(currentDateString);
        }
        if (startorend==false){
            textView2.setText(currentDateString);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void sendMessageToFragment(View view) throws ParseException {

            String startingdate = textView1.getText().toString();
            String endingdate = textView2.getText().toString();

            if (startingdate.matches("") || endingdate.matches("")){Toast.makeText(Data.this, "Pick a date!", Toast.LENGTH_LONG).show();}
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date strDate1 = sdf.parse(startingdate);
                Date strDate2 = sdf.parse(endingdate);


                if (strDate2.after(strDate1)) {
                    Toast.makeText(Data.this, "Ending date is after", Toast.LENGTH_LONG).show();
                    Events.ActivityFragmentMessage activityFragmentMessageEvent =
                            new Events.ActivityFragmentMessage(startingdate + endingdate);
                    GlobalBus.getBus().post(activityFragmentMessageEvent);

                }
                if (strDate1.after(strDate2)) {
                    Toast.makeText(Data.this, "Starting date is after", Toast.LENGTH_LONG).show();
                }
                if (strDate2.equals(strDate1)) {
                    Toast.makeText(Data.this, "Ending date is same", Toast.LENGTH_LONG).show();
                    Events.ActivityFragmentMessage activityFragmentMessageEvent =
                            new Events.ActivityFragmentMessage(startingdate + endingdate);
                    GlobalBus.getBus().post(activityFragmentMessageEvent);
                }
            }
    }

    @Subscribe
    public void getMessage(Events.FragmentActivityMessage fragmentActivityMessage) {

        Toast.makeText(getApplicationContext(),
                "Message received" + " " + fragmentActivityMessage.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();
    }

    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_data);

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

            case R.id.nav_op:
                Intent intenta = new Intent (this, ViewingUser.class);
                animateNavigationDrawer();
                startActivity(intenta);
                break;

            case R.id.nav_adm:
                Intent intentb = new Intent (this, ViewingAdmin.class);
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