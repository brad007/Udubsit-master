package com.ionicframework.udubsit252887.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.models.Poi;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIFragment extends Fragment {


    private View rootView;
    private ListView mPOIListView;
    private FirebaseListAdapter<Poi> mPOIAdapter;

    public POIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_poi, container, false);
        mPOIListView = (ListView) rootView.findViewById(R.id.poi_listview);

        mPOIAdapter = new FirebaseListAdapter<Poi>(
                getActivity(),
                Poi.class,
                R.layout.item_layout_poi,
                FirebaseDatabase.getInstance().getReference("poi").orderByChild("name")) {
            @Override
            protected void populateView(View v, Poi model, int position) {
                TextView pointsText = (TextView) v.findViewById(R.id.poi_textview);
                pointsText.setText(model.getName());
                TextView pointDescr = (TextView) v.findViewById(R.id.poi_building);
                pointDescr.setText(model.getDescription());
            }
        };

        mPOIListView.setAdapter(mPOIAdapter);

        mPOIListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poi poi = mPOIAdapter.getItem(position);

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + poi.getLatitude() + "," + poi.getLongitude() + "(" + poi.getDescription() + ")=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        return rootView;
    }

}
