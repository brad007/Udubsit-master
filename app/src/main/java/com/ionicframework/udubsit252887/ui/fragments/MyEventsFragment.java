package com.ionicframework.udubsit252887.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Event;
import com.ionicframework.udubsit252887.ui.activities.GroupDetailActivity.PlaceHolderFragment.EventHolder;
import com.ionicframework.udubsit252887.ui.activities.ViewEventActivity;

public class MyEventsFragment extends Fragment {

    private View rootView;
    private RecyclerView mMyEventRecycler;
    private FirebaseRecyclerAdapter<Event, EventHolder> myEventAdapter;

    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_events, container, false);
        initialiseScreen();
        return rootView;
    }

    private void initialiseScreen() {
        mMyEventRecycler = (RecyclerView) rootView.findViewById(R.id.myEventRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mMyEventRecycler.setLayoutManager(manager);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupAdapter();
    }

    private void setupAdapter() {


        myEventAdapter = new FirebaseRecyclerAdapter<Event, EventHolder>(
                Event.class,
                R.layout.item_layout_event,
                EventHolder.class,
                FirebaseDatabase.getInstance()
                        .getReference(Constants.MY_EVENTS_KEY)
                        .child(Utils.getUserEmail())
                        .orderByChild("startDate")
                        .startAt(System.currentTimeMillis())
        ) {
            @Override
            protected void populateViewHolder(final EventHolder viewHolder, final Event model, int position) {
                final String event_image_url = model.getEventImageUrl();
                Glide.with(getActivity())
                        .load(event_image_url)
                        .into(viewHolder.eventImage);
                viewHolder.setEventHeading(model.getTitle());
                viewHolder.setEventMembers(model.getNumOfPeople());

                viewHolder.setEventDate(model.getStartDate());

                final String eventID = model.getEventId();
                viewHolder.eventCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),
                                ViewEventActivity.class);
                        intent.putExtra(Constants.IMAGE_URL_KEY, event_image_url);
                        intent.putExtra(Constants.EVENT_ID_KEY, eventID);
                        intent.putExtra(Constants.GROUP_ID_KEY, model.getGroupId());
                        intent.putExtra(Constants.EMAIL_KEY, model.getOwner());
                        startActivity(intent);
                    }
                });
            }
        };
        mMyEventRecycler.setAdapter(myEventAdapter);
    }
}
