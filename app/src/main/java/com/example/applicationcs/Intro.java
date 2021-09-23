package com.example.applicationcs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Intro extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView imageView;

    int progress =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
            progressBar.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        setProgressValue(progress);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro.this,Login.class);
                startActivity(intent);
                fileList();
            }
        },5000);

        findViewById(R.id.intro).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intro.this,Login.class);
                startActivity(intent);
                fileList();
            }
        });

    }

    private void setProgressValue(final int progress){
        //setting the progress bar
        progressBar.setProgress(progress);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                setProgressValue(progress+30);
            }
        });
        thread.start();
    }



}