package com.example.android.quakereport;

import android.util.Log;

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
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static String SAMPLE_JSON_RESPONSE =null;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static String makeHTTPRequest(URL url) throws IOException
    {
        if(url==null)
            return SAMPLE_JSON_RESPONSE;
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try
        {
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode()==200)
            {
                inputStream=httpURLConnection.getInputStream();
                SAMPLE_JSON_RESPONSE=readFromStream(inputStream);
            }
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if(httpURLConnection!=null)
                httpURLConnection.disconnect();
            if(inputStream!=null)
                inputStream.close();
        }
        return SAMPLE_JSON_RESPONSE;
    }

    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder stringBuilder= new StringBuilder();
        if(inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
            String line =bufferedReader.readLine();
            while(line !=null)
            {
                stringBuilder.append(line);
                line=bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static URL createURL(String requestURL)
    {
        URL url=null;
        try
        {
            url=new URL(requestURL);
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    /**
     * Return a list of {@link earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<earthquake> extractEarthquakes(String requestURL) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<earthquake> earthquakes = new ArrayList<>();
        URL url=createURL(requestURL);
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            SAMPLE_JSON_RESPONSE=makeHTTPRequest(url);
            if(SAMPLE_JSON_RESPONSE==null)
                return null;
            JSONObject obj = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = obj.getJSONArray("features");
            Date date;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy hh:mm:ss", Locale.US);
            for (int i = 0; i < features.length(); i++) {
                JSONObject erthquake = features.getJSONObject(i);
                JSONObject properties = erthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                mag = Double.parseDouble(decimalFormat.format(mag));
                String place = properties.getString("place");
                long time = properties.getLong("time");
                date = new Date(time);
                String dateToDisplay = simpleDateFormat.format(date);
                earthquakes.add(new earthquake(mag, place, dateToDisplay));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Problem parsing the url to jsonResponse"+e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static String getUrl(int position) 
    {
        String url = "";
        try 
        {
            JSONObject jsonObject = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = jsonObject.getJSONArray("features");
            JSONObject erthquake = features.getJSONObject(position);
            JSONObject properties = erthquake.getJSONObject("properties");
            url = properties.getString("url");
        }
        catch (Exception e) 
        {
            Log.e("QueryUtlis", "Problem parsing the JSON results", e);
        }
        return url;
    }

}