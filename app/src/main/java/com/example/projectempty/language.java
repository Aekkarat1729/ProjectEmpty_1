package com.example.projectempty;

import android.app.Application;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class language extends Appcompat


{
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_language);

        ImageButton en = findViewById(R.id.btn_en);
        ImageButton thai = findViewById(R.id.btn_thai);
        languageManager lang = new languageManager(this);

        en.setOnClickListener(View ->
        {

            lang.updateResource("en");
            recreate();


        });

        thai.setOnClickListener(View ->
        {
            lang.updateResource("th");
            recreate();

        });



    }

}
