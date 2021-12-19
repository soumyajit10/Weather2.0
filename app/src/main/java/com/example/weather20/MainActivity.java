package com.example.weather20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weather20.data.SunshinePreferences;
import com.example.weather20.utilites.NetworkUtilites;
import com.example.weather20.utilites.OpenWeatherJsonUtilites;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]> {
    private TextView mErrorMessageDisplay;
    private ProgressBar loadingIndicator;
    private RecyclerView mRecyclerView;
    private WeatherAdapter mWeatherAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FORECAST_LOADER_ID = 0;
    private static boolean PREFERENCES_UPDATED =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mErrorMessageDisplay= (TextView)findViewById(R.id.error_message_display);
        loadingIndicator=(ProgressBar)findViewById(R.id.loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mWeatherAdapter = new WeatherAdapter(this);
        mRecyclerView.setAdapter(mWeatherAdapter);



        getLoaderManager().initLoader(FORECAST_LOADER_ID,null,this);





    }

    private void showWeatherData(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorData(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }




    @Override
    public void onClick(String weatherForDay) {

        Toast.makeText(this,"opening",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this,DetailActivity.class);
        i.putExtra(Intent.EXTRA_TEXT,weatherForDay);
        startActivity(i);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<String[]>(this) {
            String[] mWeatherData = null;

            @Override
            protected void onStartLoading() {
                if (mWeatherData != null){
                    deliverResult(mWeatherData);
                }else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String location = SunshinePreferences.getPreferredWeatherLocation(MainActivity.this);
                URL weatherRequestUrl = NetworkUtilites.buildUrl(location);
                try {
                    String jsonWeatherResponse = NetworkUtilites.getResponseFromHttpUrl(weatherRequestUrl);
                    String[] simpleJsonWeatherData = OpenWeatherJsonUtilites.getSimpleWeatherStringFromJson(MainActivity.this,jsonWeatherResponse);
                    return  simpleJsonWeatherData;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
            public  void  deliverResult(String[] data){
                mWeatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        mWeatherAdapter.setWeatherData(data);

        if (data != null){
            showWeatherData();

        }else {
            showErrorData();
        }


    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    private void invalidData(){
        mWeatherAdapter.setWeatherData(null);
    }


    //  public class FetchWeatherAsyncTask extends AsyncTask<String,Void,String[]>{
    //   @Override
    // protected void onPreExecute() {
    //   super.onPreExecute();
    //   loadingIndicator.setVisibility(View.VISIBLE);
    //  }

    //  @Override
    // protected String[] doInBackground(String... strings) {
    //if (strings.length==0){
    //  return null;
    //  }

    // String location = strings[0];


    // }

    // @Override
    //  protected void onPostExecute(String[] weatherData) {

    // }
    //}


    private void openLocationInMap(){
        String address = "1,Queens Way,Maidan,Kolkata,West bengal 700071";
        Intent location = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+ address));
        if (location.resolveActivity(getPackageManager())!= null){
            Toast.makeText(this,"opening maps",Toast.LENGTH_SHORT).show();
            startActivity(location);
        }else {
            Log.d(TAG,"error in location activity"+ location.toString()+"no receiving apps installed");
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_UPDATED){
            Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show();
            getLoaderManager().restartLoader(FORECAST_LOADER_ID,null,this);
            PREFERENCES_UPDATED= false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id ==R.id.refresh){
            invalidData();  // mWeatherAdapter.setWeatherData(null);
            getLoaderManager().restartLoader(FORECAST_LOADER_ID,null,this);
            return true;
        }
        if (id==R.id.map){
            openLocationInMap();
            return true;
        }
        if (id == R.id.settings){
            Toast.makeText(this,"getting ready",Toast.LENGTH_SHORT).show();
            Intent setting = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

