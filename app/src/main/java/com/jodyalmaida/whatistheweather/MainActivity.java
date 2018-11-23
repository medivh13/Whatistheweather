package com.jodyalmaida.whatistheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView weatherResult;
    public void findWeather(View view){

//        Log.i("citi", cityName.getText().toString());
//        weatherResult.setText(cityName.getText().toString());
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        Downloadtask task = new Downloadtask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityText);
        weatherResult = (TextView) findViewById(R.id.resultText);
    }

    public class Downloadtask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground (String... urls){

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;

                    data = reader.read();
                }
            } catch (Exception e){
                Log.i("do in background ", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute (String result){
            super.onPostExecute(result);

            try {
                String message ="";
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                for(int i = 0; i < arr.length(); i+=1){
                    String main ="";
                    String desc ="";

                    JSONObject jsonPart = arr.getJSONObject(i);

                    main = jsonPart.getString("main");
                    desc = jsonPart.getString("description");

                    if(main !="" && desc !=""){
                        message += main +" : " + desc + "\r\n";
                    }
                }

                if(message !=""){
                    Log.i("citi", message);
                    weatherResult.setText(message);
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            } catch (Exception e){
                Log.i("on post execute ", e.getMessage());
            }
        }
    }
}
