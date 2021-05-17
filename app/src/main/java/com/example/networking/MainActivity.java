package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.jar.Attributes;

public class MainActivity<adapter> extends AppCompatActivity {


    //private ListView listview;
    //private Mountain[] mountains;
    private ArrayList<Mountain> arrayList;
    private ArrayAdapter adapter;


    @SuppressWarnings("SameParameterValue")
    private String readFile(String fileName) {
        try {
            //noinspection CharsetObjectCanBeUsed
            return new Scanner(getApplicationContext().getAssets().open(fileName), Charset.forName("UTF-8").name()).useDelimiter("\\A").next();

        } catch (IOException e) {
            Log.e("MainActivity ==>", "Could not read file: " + fileName);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

        arrayList = new ArrayList();
        adapter = new ArrayAdapter(this, R.layout.list_item_textview, arrayList);
        ListView listView = findViewById(R.id.my_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mountain temp_m = arrayList.get(position);

                Log.d("MainActivity ==>", "Toast");
                Toast.makeText(MainActivity.this, "Fakta om:  " + temp_m.getName() + "Höjd: "
                        + temp_m.getLocation(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    //String s = readFile("mountains.json");
    //Log.d("MainActivity ==>","The following text was found in textfile:\n\n"+s);

    //Gson gson = new Gson();
    //Mountain = gson.fromJson(s,Mountain[].class);

    //for (int i = 0; i < mountains.length; i++) {
    //    Log.d("MainActivity ==>", "Hittade ett berg: "+ mountains[i].getName() + " " + mountains[i].getAuxdata().getWiki());
    //}

@SuppressLint("StaticFieldLeak")
public class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;
        private Object Mountain;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("AsyncTask ==>", json);
            Gson gson = new Gson();
            Mountain[] newMountains = gson.fromJson(json,Mountain[].class);
            arrayList.clear();
            for (int i = 0; i < newMountains.length; i++) {
                Mountain m = newMountains[i];
                Log.d("AsyncTask ==>", "Hittade ett berg: " + newMountains[i]);
                arrayList.add(m);
            }
            adapter.notifyDataSetChanged();
        }
    }
}

/*
Mountain[] newMountains = gson.fromJson(json,Mountain[].class);
    arrayList.clear();
    for (int i = 0; i < newMountains .length; i++) {
        Mountain m= newMountains[i];
        Log.d("AsyncTask ==>", "Hittade ett berg: " + newMountains[i]);
        arrayList.add(m);
    }
    adapter.notifyDataSetChanged();*/

//cleara din lista
//för varje element som hittas i array lägg till i listan
//Notifiera adaptern
