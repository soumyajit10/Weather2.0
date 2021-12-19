package com.example.weather20.utilites;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class OpenWeatherJsonUtilites {

    public  static String[] getSimpleWeatherStringFromJson(Context context,String forecastJsonStr) throws JSONException {
        final String OWM_LIST = "list";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN= "min";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION= "main";
        final String OWM_MESSAGE_CODE = "cod";

        String[] parsedWeatherData = null;
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        parsedWeatherData = new String[weatherArray.length()];

        long localDate = System.currentTimeMillis();
        long utcDate = SunshineDateUtilites.getUTCDateFromLocal(localDate);
        long startDay = SunshineDateUtilites.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;

            /* These are the values that will be collected */
            long dateTimeMillis;
            double high;
            double low;
            String description;

            /* Get the JSON object representing the day */
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + SunshineDateUtilites.DAY_IN_MILLIS * i;
            date = SunshineDateUtilites.getFriendlyDateString(context, dateTimeMillis, false);

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);
            low = temperatureObject.getDouble(OWM_MIN);
            highAndLow = SunshineWeatherUtilites.formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }



    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}


