package com.ionicframework.udubsit252887.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.models.Groups;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    //Declaring variables;
    private View rootView;
    private RecyclerView mGroupRecycler;
    private FirebaseRecyclerAdapter<Groups, GroupHolder> mGroupAdapter;
    private DatabaseReference groupRef;

    //Default Constructor
    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_group, container, false);
        initialiseScreen();
        return rootView;
    }

    private void initialiseScreen() {
        mGroupRecycler = (RecyclerView) rootView.findViewById(R.id.group_recycler);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mGroupRecycler.setLayoutManager(manager);
        groupRef = FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupAdapter();
    }

    //Setting up Firebase RecyclerView Adapter & Reyclerview
    private void setupAdapter() {
        mGroupAdapter = new FirebaseRecyclerAdapter<Groups, GroupHolder>(
                Groups.class,
                R.layout.item_layout_group,
                GroupHolder.class,
                groupRef.orderByChild("name")
        ) {
            @Override
            protected void populateViewHolder(final GroupHolder viewHolder, final Groups model, int position) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                Glide.with(getActivity())
                        .load(model.getThumbnailUrl())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .centerCrop()
                        .into(viewHolder.groupImage);

                viewHolder.setGroupTitle(model.getName());
                viewHolder.setGroupNumber(model.getNumOfUsers());
                viewHolder.groupCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pushID = intent.getStringExtra("groupId");
                        //imageUrl = intent.getIntExtra("imageUrl", 0);
                        Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                        intent.putExtra(Constants.GROUP_ID_KEY, model.getGroupId());
                        intent.putExtra(Constants.IMAGE_URL_KEY, model.getImageUrl());
                        startActivity(intent);
                    }
                });
            }
        };
        mGroupRecycler.setAdapter(mGroupAdapter);
    }

    public static class GroupHolder extends RecyclerView.ViewHolder {
        public CardView groupCard;
        public ImageView groupImage;
        public TextView groupTitle;
        public TextView groupNumber;
        public ProgressBar progressBar;

        public GroupHolder(View itemView) {
            super(itemView);
            groupImage = (ImageView) itemView.findViewById(R.id.group_image);
            groupTitle = (TextView) itemView.findViewById(R.id.group_title_text);
            groupNumber = (TextView) itemView.findViewById(R.id.group_member_number_text);
            groupCard = (CardView) itemView.findViewById(R.id.group_card);
            progressBar = (ProgressBar) itemView.findViewById(R.id.groupProgress);
        }

        public void setImage(byte[] id) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(id, 0, id.length);
            groupImage.setImageBitmap(bitmap);
        }

        public void setGroupTitle(String title) {
            groupTitle.setText(title);
        }

        public void setGroupNumber(long num) {
            groupNumber.setText(num + " Members");
        }

    }
}
