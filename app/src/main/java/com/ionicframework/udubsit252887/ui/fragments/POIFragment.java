package com.ionicframework.udubsit252887.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.models.Poi;
import com.ionicframework.udubsit252887.ui.dialogs.POIErrorDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class POIFragment extends Fragment {


    private View rootView;
    private ListView mPOIListView;
    private FirebaseListAdapter<Poi> mPOIAdapter;
    private String limitQueryStr;
    private Query databaseRef;
    public POIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//merge
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu,menu);
        MenuItem item = menu.findItem(R.id.search);
//        item.setVisible(true);
        SearchView searchView =  (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                limitQueryStr = s;
                CustomPOIAdapter();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_poi, container, false);
        mPOIListView = (ListView) rootView.findViewById(R.id.poi_listview);

        CustomPOIAdapter();

        return rootView;
    }

    private void CustomPOIAdapter(){
        if(limitQueryStr !=null){
            //input query is != empty
            databaseRef = FirebaseDatabase.getInstance().getReference(Constants.POI_KEY)
                    .orderByChild("name")
                    .startAt(limitQueryStr)
                    .endAt(limitQueryStr +"~");

        }
        else {
            databaseRef = FirebaseDatabase.getInstance().getReference(Constants.POI_KEY).orderByChild("name");

        }

        mPOIAdapter = new FirebaseListAdapter<Poi>(
                getActivity(),
                Poi.class,
                R.layout.item_layout_poi,
                databaseRef){
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

                try {
                    startActivity(mapIntent);
                }
                catch (Exception e)
                {
                    POIErrorDialog poiErrorDialog = new POIErrorDialog();
                    poiErrorDialog.show(getActivity().getFragmentManager(),"Test");
                }

            }
        });

    }


}
