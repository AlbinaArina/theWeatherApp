package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_field.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG);
                } else {
                    String city = user_field.getText().toString();
                    String key = "7bd9ebcd3edf63a3334abb934e011aad";
//                    String urlToGetLonLat = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + key;
                    Map<String, ArrayList<String>> map = new HashMap<>();
                    fillMap(map);

                    String lat = Objects.requireNonNull(map.get(city)).get(0);
                    String lon = Objects.requireNonNull(map.get(city)).get(1);
                    String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
                    new GetUrlData().execute(url);
                }
            }
        });
    }

    private void fillMap(Map<String, ArrayList<String>> map) {
        map.put("Moscow", new ArrayList<>(Arrays. asList( "55.7504461" , "37.6174943")));
        map.put("Kiev", new ArrayList<>(Arrays. asList( "50.4500336" , "30.5241361")));
        map.put("London", new ArrayList<>(Arrays. asList( "51.5073219" , "-0.1276474")));
        map.put("Wroclaw", new ArrayList<>(Arrays. asList( "51.1263106" , "16.97819633051261")));
        map.put("Krakow", new ArrayList<>(Arrays. asList( "50.0469432" , "19.997153435836697")));
        map.put("Lviv", new ArrayList<>(Arrays. asList( "49.841952" , "24.0315921")));
        map.put("Paris", new ArrayList<>(Arrays. asList( "48.8588897" , "2.3200410217200766")));
        map.put("Gdansk", new ArrayList<>(Arrays. asList( "54.3482908" , "18.6540231")));
        map.put("Odessa", new ArrayList<>(Arrays. asList( "46.4873195" , "30.7392776")));
        map.put("Roma", new ArrayList<>(Arrays. asList( "41.8933203" , "12.4829321")));
        map.put("Kharkiv", new ArrayList<>(Arrays. asList( "49.9923181" , "36.2310146")));
        map.put("NYC", new ArrayList<>(Arrays. asList( "40.7127281" , "-74.0060152")));
        map.put("Warsaw", new ArrayList<>(Arrays. asList( "52.2337172" , "21.071432235636493")));
    }

    @SuppressLint("StaticFieldLeak")
    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            result_info.setText(R.string.waiting);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("Temperature: " + jsonObject.getJSONObject("main").getDouble("temp"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}