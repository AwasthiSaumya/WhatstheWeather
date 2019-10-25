package com.example.saumyaawasthi.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView result;
    String enter,main,description;

    public class DownloadContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i("URL", strings[0]);
            String result = "";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather = jsonObject.getString("weather");
                Log.i("weather info",weather);
                JSONArray arr=new JSONArray(weather);
                for(int i=0;i<arr.length();i++){
                    JSONObject part=arr.getJSONObject(i);
                    Log.i("main",part.getString("main"));
                    Log.i("des",part.getString("description"));
                    main=part.getString("main");
                    description=part.getString("description");
                    result.setText(main+":"+description+"\n");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void clicked(View view){
        enter=city.getText().toString();
        DownloadContent task = new DownloadContent();
        try {
            task.execute("http://openweathermap.org/data/2.5/weather?q="+enter+"&appid=b6907d289e10d714a6e88b30761fae22");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("city",enter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=(EditText)findViewById(R.id.editText);
        result=(TextView)findViewById(R.id.textView);
    }
}
