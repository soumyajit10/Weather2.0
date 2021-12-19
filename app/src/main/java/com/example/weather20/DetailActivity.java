package com.example.weather20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String FORECAST_SHARE_HASHTAG = "#Soumyajit";
    private String mForecast;
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherTextView = (TextView)findViewById(R.id.details);
        Intent u = getIntent();
        if (u!=null && u.hasExtra(Intent.EXTRA_TEXT)){
            mForecast= u.getStringExtra(Intent.EXTRA_TEXT);
            mWeatherTextView.setText(mForecast);
        }
    }

    private Intent shareForecast (){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/italic").setText(mForecast + FORECAST_SHARE_HASHTAG).getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details,menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        menuItem.setIntent(shareForecast());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.settings){
            Intent l = new Intent(this,SettingsActivity.class);
            startActivity(l);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

