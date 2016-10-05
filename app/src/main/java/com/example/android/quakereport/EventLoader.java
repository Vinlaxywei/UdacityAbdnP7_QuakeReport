package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class EventLoader extends AsyncTaskLoader<List<EarthQuake>> {
    private final String LOG_TAG = EventLoader.class.getSimpleName();

    public EventLoader(Context context) {
        super(context);
        Log.d(LOG_TAG, "EventLoader: ");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground: ");

        String jsonStr = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = "geojson";
        String limit = "3";
        String todayTime = dateFormat.format(System.currentTimeMillis());
        String yesterdayTime = dateFormat.format(System.currentTimeMillis() - 86400 * 1000 * 7);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String minMag = sharedPreferences.getString(
                getContext().getString(R.string.settings_min_magnitude_key),
                getContext().getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(
                getContext().getString(R.string.setting_order_by_key),
                getContext().getString(R.string.setting_order_by_magnitude));
        ;
        final String FORECAST_BASE_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?";
        final String FOMAT_PARAM = "format";
        final String STARTTIME_PARAM = "starttime";
        final String ENDTIME_PARAM = "endtime";
        final String MINMAGNITUDE_PARAM = "minmag";
        final String ORDERBY_PARAM = "orderby";

        try {
            // 构建Uri
            Uri baseUri = Uri.parse(FORECAST_BASE_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter(FOMAT_PARAM, format);
            uriBuilder.appendQueryParameter(STARTTIME_PARAM, yesterdayTime);
            uriBuilder.appendQueryParameter(ENDTIME_PARAM, todayTime);
            uriBuilder.appendQueryParameter(MINMAGNITUDE_PARAM, minMag);
            uriBuilder.appendQueryParameter(ORDERBY_PARAM, orderBy);

            Log.d(LOG_TAG, "loadInBackground: "+uriBuilder.toString());

            // 定义 OkHttp 对象
            OkHttpClient client = new OkHttpClient();

            // 定义请求
            Request request = new Request.Builder()
                    .url(uriBuilder.toString())
                    .build();

            // 使用 OkHttp 对象发起请求，获取到的数据赋值到 Response 对象
            Response response = client.newCall(request).execute();
            jsonStr = response.body().string();
            return getEarthQuakeFromJson(jsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Error ", e);
            e.printStackTrace();
        }

        return null;
    }

    private List<EarthQuake> getEarthQuakeFromJson(String jsonStr) {
        List<EarthQuake> results = new ArrayList<EarthQuake>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonStr);
            JSONArray featuresArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject arrayJson = featuresArray.getJSONObject(i);
                JSONObject propertiesJson = arrayJson.getJSONObject("properties");

                double magnitude = propertiesJson.getDouble("mag");
                String location = propertiesJson.getString("place");
                long date = propertiesJson.getLong("time");
                String webSites = propertiesJson.getString("url");

                results.add(new EarthQuake(magnitude, location, date, webSites));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "getEarthQuakeFromJson: " + results.size());

        return results;
    }
}
