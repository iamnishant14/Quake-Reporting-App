/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String url = "http://earthquake.usgs.gov/fdsnws/event/1/query";
    private ListView earthquakeListView;
    private TextView textView;
    private ProgressBar progress;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        // Create a fake list of earthquake locations.
        //fetchData fetchData=new fetchData();
        //fetchData.execute(url);


        // Find a reference to the {@link ListView} in the layout

        // Create a new {@link ArrayAdapter} of earthquakes

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView = (ListView) findViewById(R.id.list);
        textView= (TextView)findViewById(R.id.emptyText);
        earthquakeListView.setEmptyView(textView);
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net=cm.getActiveNetworkInfo();
        if(net!=null&&net.isConnected())
        {
            getLoaderManager().initLoader(1, null, this);
        }
        else
        {
            progress=(ProgressBar)findViewById(R.id.progress);
            progress.setVisibility(View.GONE);
            textView.setText(R.string.no_internet);
        }
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = QueryUtils.getUrl(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude=sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri=Uri.parse(url);
        Uri.Builder builder=baseUri.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","1000");
        builder.appendQueryParameter("minmag",minMagnitude);
        builder.appendQueryParameter("orderby",orderBy);
        return new EarthquakeActivityLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<earthquake>> loader, ArrayList<earthquake> data) {
        if (data != null && !data.isEmpty()) {
           earthquakeListView.setAdapter(new EarthquakeAdapter(this,data));


        }
        textView.setText(R.string.no_data);
        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        Toast toast=Toast.makeText(this,"Data updated",Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<earthquake>> loader) {
        new EarthquakeActivityLoader(this, "");
    }



}
