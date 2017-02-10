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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Ads;
import com.ionicframework.udubsit252887.models.Users;
import com.ionicframework.udubsit252887.ui.activities.ChatActivity;
import com.ionicframework.udubsit252887.ui.activities.GroupDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdsFragment extends Fragment {


    private View rootView;
    private RecyclerView adsRecycler;
    private FirebaseRecyclerAdapter<Ads, GroupDetailActivity.PlaceHolderFragment.AdvertHolder> adsAdapter;

    public MyAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_ads, container, false);
        initialiseScreen();
        return rootView;
    }

    private void initialiseScreen() {
        adsRecycler = (RecyclerView) rootView.findViewById(R.id.ads_recycler);
        adsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter();
    }

    private void setupAdapter() {
        adsAdapter = new FirebaseRecyclerAdapter<Ads, GroupDetailActivity.PlaceHolderFragment.AdvertHolder>(
                Ads.class,
                R.layout.item_layout_advert,
                GroupDetailActivity.PlaceHolderFragment.AdvertHolder.class,
                FirebaseDatabase.getInstance().getReference(Constants.MY_ADS_KEY)
                        .child(Utils.getUserEmail())
        ) {
            @Override
            protected void populateViewHolder(final GroupDetailActivity.PlaceHolderFragment.AdvertHolder viewHolder, final Ads model, int position) {
                viewHolder.contactLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra(Constants.EMAIL_KEY, model.getAdvertOwner());
                        startActivity(intent);
                    }
                });
                viewHolder.setAdvertCost(model.getAdvertCost());
                viewHolder.setAdvertDescription(model.getAdvertDescription());
                viewHolder.setAdvertDue(Utils.getDate(model.getAdvertDue()));
                viewHolder.setAdvertOwnerEmail(model.getAdvertOwner());

                Glide.with(getActivity())
                        .load(model.getAdvertImageUrl())
                        .into(viewHolder.advertImage);

                if (model.getAdvertOwner() != null) {
                    FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                            .child(model.getAdvertOwner().replace(".", ","))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Users users = dataSnapshot.getValue(Users.class);
                                    viewHolder.setAdvertOwnerUsername(users.getUser());
                                    Glide.with(getActivity())
                                            .load(users.getPhotoUri())
                                            .into(viewHolder.advertOwnerDisplay);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

        };
        adsRecycler.setAdapter(adsAdapter);
    }

}
