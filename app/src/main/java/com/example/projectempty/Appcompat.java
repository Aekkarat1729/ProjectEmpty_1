package com.example.projectempty;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Appcompat extends AppCompatActivity
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        languageManager languageManager = new languageManager(this);
        languageManager.updateResource(languageManager.getLang());
    }
}
