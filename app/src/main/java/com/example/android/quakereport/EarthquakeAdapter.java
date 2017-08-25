package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nishant on 4/1/17.
 */

public class EarthquakeAdapter extends ArrayAdapter<earthquake> {

    public EarthquakeAdapter(Context context, ArrayList<earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    private int getMagnitudeColor(double magnitude)
    {
        switch((int)Math.floor(magnitude))
        {
            case 0:
            case 1:
                return ContextCompat.getColor(getContext(),R.color.magnitude1);
            case 2:
                return  ContextCompat.getColor(getContext(),R.color.magnitude2);
            case 3:
                return  ContextCompat.getColor(getContext(),R.color.magnitude3);
            case 4:
                return ContextCompat.getColor(getContext(),R.color.magnitude4);
            case 5:
                return ContextCompat.getColor(getContext(),R.color.magnitude5);
            case 6:
                return  ContextCompat.getColor(getContext(),R.color.magnitude6);
            case 7:
                return ContextCompat.getColor(getContext(),R.color.magnitude7);
            case 8:
                return ContextCompat.getColor(getContext(),R.color.magnitude8);
            case 9:
                return ContextCompat.getColor(getContext(),R.color.magnitude9);
            default:
                return ContextCompat.getColor(getContext(),R.color.magnitude10plus);
        }
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list, parent, false);
        }

        earthquake erth = getItem(position);

        TextView mag = (TextView) view.findViewById(R.id.magnitude);
        mag.setText(erth.getMagnitude().toString());

        GradientDrawable gradientDrawable=(GradientDrawable)mag.getBackground();
        int magnitudeColor=getMagnitudeColor(erth.getMagnitude());
        gradientDrawable.setColor(magnitudeColor);

        TextView dt=(TextView)view.findViewById(R.id.date);
        dt.setText(erth.getDate());

        TextView loc_pre=(TextView)view.findViewById(R.id.location_pre);
        TextView loc_Suff=(TextView)view.findViewById(R.id.location_suf);

        try
        {
            String[] s=erth.getLocation().split("of",2);
            loc_pre.setText(s[0]+" of");
            loc_Suff.setText(s[1].substring(1));
        }
        catch (Exception e)
        {
            loc_pre.setText("NEAR");
            loc_Suff.setText(erth.getLocation());
        }

        dt.setVisibility(View.VISIBLE);
        mag.setVisibility(View.VISIBLE);
        loc_pre.setVisibility(View.VISIBLE);
        loc_Suff.setVisibility(View.VISIBLE);
        return view;
    }
}
