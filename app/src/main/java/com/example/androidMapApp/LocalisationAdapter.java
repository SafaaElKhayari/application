package com.example.androidMapApp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.googlemapapi.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LocalisationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Localisation> locationList;
    private LayoutInflater inflater;


    public LocalisationAdapter(Context context, ArrayList<Localisation> locationList) {
        this.context = context;
        this.locationList = locationList;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Localisation getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //utilisé pour créer un nouvel View(ou Layout) objet à partir de notre mise en page  adapter_item.XML.
        view = inflater.inflate(R.layout.adapter_item, null);

        Localisation currentLocation = getItem(position);

        final String sitename = currentLocation.sitename;
        final String description = currentLocation.description;
        final double latitude = currentLocation.latitude;
        final double longitude = currentLocation.longitude;
        final String date = currentLocation.date;

        final int numberVotes = currentLocation.numberVotes;
        final int sumVotes = currentLocation.sumVotes;
        final double rating = numberVotes == 0 ? 0.0 : (double)sumVotes / numberVotes;

        TextView locationSitenameView = view.findViewById(R.id.location_sitename);
        locationSitenameView.append(sitename);

        TextView locationDescriptionView = view.findViewById(R.id.location_description);
        locationDescriptionView.setText(description);

        DecimalFormat df = new DecimalFormat("#.##");

        TextView locationRatingView = view.findViewById(R.id.location_rating);
        locationRatingView.append( df.format(rating) );

        TextView locationDateView = view.findViewById(R.id.location_date);
        locationDateView.append(date);



        // click de see on map
        Button btn = view.findViewById(R.id.see_location_on_map_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("finish");

                i.putExtra("sitename", sitename);
                i.putExtra("description", description);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("date", date);
                i.putExtra("numberVotes", numberVotes);
                i.putExtra("sumVotes", sumVotes);

                context.sendBroadcast(i);
            }
        });

        return view;
    }
}
