package com.ionicframework.udubsit252887.ui.fragments;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
public class POIFragment extends Fragment implements SearchView.OnQueryTextListener {


    private View rootView;
    private ListView mPOIListView;
    private FirebaseListAdapter<Poi> mPOIAdapter;
    private String limitQueryStr;
    private Query databaseRef;
    private SimpleCursorAdapter simpleCursorAdapter;
    private SearchView mSearchView;

    public POIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }



    //merge
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_poi, container, false);
        mPOIListView = (ListView) rootView.findViewById(R.id.poi_listview);
        mSearchView = (SearchView) rootView.findViewById(R.id.search_viewpoi);
        mSearchView.setOnQueryTextListener(this);

        CustomPOIAdapter();

        return rootView;
    }

    private void CustomPOIAdapter() {
        if (limitQueryStr != null) {
            //input query is != empty
            databaseRef = FirebaseDatabase.getInstance().getReference(Constants.POI_KEY)
                    .orderByChild("name")
                    .startAt(limitQueryStr)
                    .endAt(limitQueryStr + "~");

        } else {
            databaseRef = FirebaseDatabase.getInstance().getReference(Constants.POI_KEY).orderByChild("name");

        }

        mPOIAdapter = new FirebaseListAdapter<Poi>(
                getActivity(),
                Poi.class,
                R.layout.item_layout_poi,
                databaseRef) {
            @Override
            protected void populateView(View v, Poi model, int position) {

                TextView pointsText = (TextView) v.findViewById(R.id.poi_textview);
                pointsText.setText(model.getName());
                TextView pointDescr = (TextView) v.findViewById(R.id.poi_building);
                pointDescr.setText(model.getDescription());

                MatrixCursor extras = new MatrixCursor(new String[]{"Text", "Descr"});
                extras.addRow(new String[]{pointsText.getText().toString(), pointDescr.getText().toString()});
                Log.d("Extras", extras.toString());

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
                } catch (Exception e) {
                    POIErrorDialog poiErrorDialog = new POIErrorDialog();
                    poiErrorDialog.show(getActivity().getFragmentManager(), "Test");
                }

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if(query.length()==0){
            limitQueryStr=null;
            Toast.makeText(getActivity(),"Empty string",Toast.LENGTH_LONG).show();


        }else {
            String s1 = query.substring(0,1).toUpperCase();
            limitQueryStr = s1 + query.substring(1);
            Toast.makeText(getActivity(),"Submit Search",Toast.LENGTH_LONG).show();
        }
        CustomPOIAdapter();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length()==0)
        {
            Toast.makeText(getActivity(),"Empty string",Toast.LENGTH_LONG).show();
            limitQueryStr = null;

        }
        CustomPOIAdapter();
        return false;
    }
}

