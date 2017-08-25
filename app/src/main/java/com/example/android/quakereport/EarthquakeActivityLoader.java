package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by nishant on 14/1/17.
 */
    public class EarthquakeActivityLoader extends AsyncTaskLoader<ArrayList<earthquake>> {
        String Url=null;
        EarthquakeActivityLoader(Context context, String url)
        {
            super(context);
            Url=url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<earthquake> loadInBackground() {
            if(Url==null)
                return null;
            return QueryUtils.extractEarthquakes(Url);
        }
    }
