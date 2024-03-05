package com.example.projectempty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;


import com.bumptech.glide.load.engine.Resource;

import java.util.Locale;

public class languageManager  {
    private Context ct;
    private SharedPreferences sharedPreferences;
    public languageManager(Context ctx)
    {
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale); // Set the locale directly on the configuration
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);
    }

    public String getLang()
    {
       return sharedPreferences.getString("lang","en");
    }
    public void setLang(String code)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();


    }

}
