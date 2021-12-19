package com.example.weather20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private String[] mWeatherData;



    private final WeatherAdapterOnClickHandler mClickHandler;


    public interface WeatherAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }
    public WeatherAdapter(WeatherAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;
        View view = inflater.inflate(layoutId, viewGroup, false);
        return new WeatherAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(WeatherAdapter.WeatherAdapterViewHolder weatherAdapterViewHolder, int i) {


        weatherAdapterViewHolder.mWeatherTextView.setText(mWeatherData[i]);

    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;

    }


    public class WeatherAdapterViewHolder extends RecyclerView.ViewHolder implements com.example.weather20.WeatherAdapterViewHolder{
        public final TextView mWeatherTextView;


        public WeatherAdapterViewHolder(View itemView) {
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.weatherInfo);
            itemView.setOnClickListener(this::Onclick);
        }
        @Override
        public void Onclick(View v){
            int AdapterPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[AdapterPosition];
            mClickHandler.onClick(weatherForDay);

        }

    }
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

}
