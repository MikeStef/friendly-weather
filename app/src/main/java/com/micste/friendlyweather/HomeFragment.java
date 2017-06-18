package com.micste.friendlyweather;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "com.micste";
    private static final String ENDPOINT = "https://api.darksky.net/forecast/cdaa639f3e10a478e51cadcd04c74db1/59.3293,-18.0685?units=auto";
    private RequestQueue requestQueue;
    private Gson gson;
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        context = getActivity().getApplicationContext();

        requestQueue = Volley.newRequestQueue(context);
        fetchWeather();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void fetchWeather() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onWeatherLoaded, onWeatherError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onWeatherLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JsonParser parser = new JsonParser();
            JsonObject data = parser.parse(response).getAsJsonObject();
            Weather weather = gson.fromJson(data.get("currently"), Weather.class);
            weather.setPlace("My current location");
            Log.d(TAG, weather.getSummary() + " " + weather.getTemperature());
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.saveData(weather);

        }
    };

    private final Response.ErrorListener onWeatherError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.toString());
        }
    };

}
