package com.ionicframework.udubsit252887.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.Utils.managers.GroupManager;
import com.ionicframework.udubsit252887.models.Ads;
import com.ionicframework.udubsit252887.models.Event;
import com.ionicframework.udubsit252887.models.Person;
import com.ionicframework.udubsit252887.models.Users;
import com.ionicframework.udubsit252887.ui.dialogs.TaglineDialogs;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity {
    public static String pushID;
    private String imageUrl;
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private DatabaseReference groupUrl = FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY);
    private DatabaseReference groupList = FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBER_LIST_KEY);
    private DatabaseReference groupMananager = FirebaseDatabase.getInstance().getReference(Constants.GROUP_MANAGERS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        pushID = intent.getStringExtra(Constants.GROUP_ID_KEY);
        imageUrl = intent.getStringExtra(Constants.IMAGE_URL_KEY);

        ImageView groupImage = (ImageView) findViewById(R.id.group_toolbar_image);
        Glide.with(GroupDetailActivity.this)
                .load(imageUrl)
                .into(groupImage);


        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
        final MenuItem leave = menu.findItem(R.id.action_leave);
        final MenuItem join = menu.findItem(R.id.action_join);


        groupList.child(pushID)
                .addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        ArrayList arrayList = (ArrayList) dataSnapshot.getValue();
                        setJoinVisibility(leave, join, arrayList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return true;
    }

    private void setJoinVisibility(MenuItem leave, MenuItem join, ArrayList arrayList) {
        //If the array list is empty, set join to visible(leave to invisible)
        if (arrayList == null) {
            leave.setVisible(false);
            join.setVisible(true);
        } else {
            //If the array list contains your email, set join to invisible(leave to visible)
            if (arrayList.contains(Utils.getUserEmail().replace(".", ","))) {
                leave.setVisible(true);
                join.setVisible(false);
            } else {
                leave.setVisible(false);
                join.setVisible(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_join:
                return joinGroup();
            case R.id.action_leave:
                return leaveGroup();
            case R.id.action_create_event:
                return createEvent();
            case R.id.action_create_advert:
                return createAdvert();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean joinGroup() {

        //todo: create ref to user to get the UID(Unique User ID)
        //todo: display TaglineDialog to join group
        FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                .child(Utils.getUserEmail().replace(".", ","))
                .child("uid")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String UID = (String) dataSnapshot.getValue();
                        DialogFragment dialogFragment = new TaglineDialogs(pushID, UID);
                        dialogFragment.show(getSupportFragmentManager(), null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return true;
    }

    private boolean leaveGroup() {
        //descrementing the number of group members
        GroupManager.increaseGroupMembers(-1, pushID);
        //Fetch and remove my email from the list
        Utils.removeFromList(groupList.child(pushID), GroupDetailActivity.this, groupList.child(pushID));
        //Remove Person item from group member node
        GroupManager.removeMember(pushID);
        return true;
    }

    private boolean createEvent() {
        FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBER_LIST_KEY)
                .child(pushID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            ArrayList list = (ArrayList) dataSnapshot.getValue();

                            if (Utils.getUserEmail().equals("udubsit@myuwc.ac,za")) {
                                Toast.makeText(getApplicationContext(), "You are not allowed to create events :" + pushID, Toast.LENGTH_LONG);
                                Intent intent = new Intent(GroupDetailActivity.this, AddEventActivity.class);
                                intent.putExtra(Constants.GROUP_ID_KEY, pushID);
                                startActivity(intent);

                            } else {
                                groupMananager.child(pushID.replace("-","")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String managerEmail = dataSnapshot.getChildren().iterator().next().getValue().toString();
                                        Toast.makeText(GroupDetailActivity.this, managerEmail, Toast.LENGTH_LONG)
                                                .show();


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        } else {
                            Toast.makeText(GroupDetailActivity.this, "Not a member of this group", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return true;
    }

    private boolean createAdvert() {
        FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBER_LIST_KEY)
                .child(pushID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            ArrayList list = (ArrayList) dataSnapshot.getValue();
                            if (list.contains(Utils.getUserEmail())) {
                                Intent intent = new Intent(GroupDetailActivity.this, AddAdvertActivity.class);
                                intent.putExtra(Constants.PUSH_ID_KEY, pushID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(GroupDetailActivity.this, "Not a member of this group", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(GroupDetailActivity.this, "Not a member of this group", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return true;
    }


    public static class PlaceHolderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "setion_number";
        private RecyclerView mGroupRecycler;
        private View rootView;

        public PlaceHolderFragment() {
        }

        public static PlaceHolderFragment newInstance(int sectionNumber) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_group_detail, container, false);
            setupScreen();
            return rootView;
        }

        private void setupScreen() {
            mGroupRecycler = (RecyclerView) rootView.findViewById(R.id.group_recycler);
            mGroupRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            setupAdapter(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private void setupAdapter(int n) {
            switch (n) {
                case 1:
                    setupEventAdapter();
                    break;
                case 2:
                    setupAdAdapter();
                    break;
                case 3:
                    setupPeopleAdapter();
                    break;
            }
        }

        private void setupPeopleAdapter() {
            FirebaseRecyclerAdapter<Person, PersonHolder> adapter =
                    new FirebaseRecyclerAdapter<Person, PersonHolder>(
                            Person.class,
                            R.layout.item_layout_person,
                            PersonHolder.class,
                            FirebaseDatabase.getInstance()
                                    .getReference(Constants.GROUP_MEMBERS)
                                    .child(pushID)
                    ) {
                        @Override
                        protected void populateViewHolder(final PersonHolder viewHolder, final Person model, int position) {
                            viewHolder.setUserTagline(model.getTagline());
                            viewHolder.personItemLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra(Constants.EMAIL_KEY, model.getEmail());
                                    startActivity(intent);
                                }
                            });
                            FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                                    .child(model.getEmail().replace(".", ","))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Users users = dataSnapshot.getValue(Users.class);
                                            viewHolder.setUsername(users.getUser());
                                            Glide.with(getActivity())
                                                    .load(users.getPhotoUri())
                                                    .into(viewHolder.userDisply);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    };
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            mGroupRecycler.setLayoutManager(manager);
            mGroupRecycler.setAdapter(adapter);
        }

        private void setupAdAdapter() {
            FirebaseRecyclerAdapter<Ads, AdvertHolder> adapter = new FirebaseRecyclerAdapter<Ads, AdvertHolder>(
                    Ads.class,
                    R.layout.item_layout_advert,
                    AdvertHolder.class,
                    FirebaseDatabase.getInstance().getReference(Constants.ADS_KEY).child(pushID)
            ) {
                @Override
                protected void populateViewHolder(final AdvertHolder viewHolder, final Ads model, int position) {
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

                    Log.v("AdImage", model.getAdvertImageUrl());

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
            };
            mGroupRecycler.setAdapter(adapter);
        }

        private void setupEventAdapter() {
            FirebaseRecyclerAdapter<Event, EventHolder> adapter = new FirebaseRecyclerAdapter<Event, EventHolder>(
                    Event.class,
                    R.layout.item_layout_event,
                    EventHolder.class,
                    FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY).child(pushID)

            ) {
                @Override
                protected void populateViewHolder(EventHolder viewHolder, final Event model, int position) {
                    final String event_image_url = model.getEventImageUrl();
                    Glide.with(getActivity())
                            .load(event_image_url)
                            .into(viewHolder.eventImage);
                    viewHolder.setEventHeading(model.getTitle());
                    viewHolder.setEventMembers(model.getNumOfPeople());

                    viewHolder.setEventDate(model.getStartDate());

                    viewHolder.setEventLocation(model.getLocationDescription());
                    final String eventID = model.getEventId();
                    viewHolder.eventCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(),
                                    ViewEventActivity.class);
                            intent.putExtra(Constants.IMAGE_URL_KEY, event_image_url);
                            intent.putExtra(Constants.EVENT_ID_KEY, eventID);
                            intent.putExtra(Constants.GROUP_ID_KEY, pushID);
                            intent.putExtra(Constants.EMAIL_KEY, model.getOwner());
                            startActivity(intent);
                        }
                    });
                }
            };
            mGroupRecycler.setAdapter(adapter);
        }

        public static class PersonHolder extends RecyclerView.ViewHolder {
            CircleImageView userDisply;
            TextView username;
            TextView userTagline;
            LinearLayout personItemLayout;

            public PersonHolder(View itemView) {
                super(itemView);
                userDisply = (CircleImageView) itemView.findViewById(R.id.people_avatar);
                username = (TextView) itemView.findViewById(R.id.people_username);
                userTagline = (TextView) itemView.findViewById(R.id.people_tagline);
                personItemLayout = (LinearLayout) itemView.findViewById(R.id.person_item_layout);
            }

            public void setUsername(String username) {
                this.username.setText(username);
            }

            public void setUserTagline(String tagline) {
                userTagline.setText(tagline);
            }
        }


        public static class AdvertHolder extends RecyclerView.ViewHolder {

            public static ImageView advertImage;
            public static TextView advertDescription;
            public static TextView advertCost;
            public static TextView advertDue;
            public static TextView advertOwnerUsername;
            public static TextView advertOwnerEmail;
            public static CircleImageView advertOwnerDisplay;
            public static LinearLayout contactLayout;

            public AdvertHolder(View itemView) {
                super(itemView);
                advertImage = (ImageView) itemView.findViewById(R.id.advert_image);
                advertDescription = (TextView) itemView.findViewById(R.id.advert_description);
                advertCost = (TextView) itemView.findViewById(R.id.advert_cost);
                advertDue = (TextView) itemView.findViewById(R.id.advert_due);
                advertOwnerUsername = (TextView) itemView.findViewById(R.id.advert_owner_username);
                advertOwnerEmail = (TextView) itemView.findViewById(R.id.advert_owner_email);
                advertOwnerDisplay = (CircleImageView) itemView.findViewById(R.id.advert_owner_display);
                contactLayout = (LinearLayout) itemView.findViewById(R.id.contact_layout);
            }

            public void setAdvertDescription(String description) {
                advertDescription.setText(description);
            }

            public void setAdvertCost(double cost) {
                advertCost.setText("Cost: R" + cost);
            }

            public void setAdvertDue(String due) {
                advertDue.setText(due);
            }

            public void setAdvertOwnerUsername(String username) {
                advertOwnerUsername.setText(username);
            }

            public void setAdvertOwnerEmail(String email) {
                advertOwnerEmail.setText(email);
            }
        }


        public static class EventHolder extends RecyclerView.ViewHolder {

            public CardView eventCard;
            public TextView eventHeading;
            public TextView eventDate;
            public TextView eventMembers;
            public TextView eventLocation;
            public ImageView eventDirections;
            public ImageView eventImage;


            public EventHolder(View itemView) {
                super(itemView);


                eventCard = (CardView) itemView.findViewById(R.id.event_item);
                eventHeading = (TextView) itemView.findViewById(R.id.event_heading_text);
                eventDate = (TextView) itemView.findViewById(R.id.event_date_and_time);
                eventMembers = (TextView) itemView.findViewById(R.id.event_members);
                eventDirections = (ImageView) itemView.findViewById(R.id.event_directions);
                eventImage = (ImageView) itemView.findViewById(R.id.event_image);
                eventLocation = (TextView) itemView.findViewById(R.id.event_location_description);
            }


            public void setEventHeading(String eventHeading) {
                this.eventHeading.setText(eventHeading);
            }

            public void setEventDate(long date) {
                Date startDate = new Date(date);
                eventDate.setText(
                        Utils.getDay(startDate.getDay()) + ", " + Utils.getMonth(startDate.getMonth()) + " " + startDate.getDate() + ", " + startDate.getYear()
                );
            }


            public void setEventMembers(long eventMembers) {
                this.eventMembers.setText(eventMembers + " Members");
            }

            public void setEventLocation(String local) {
                eventLocation.setText(local);
            }

            public void setEventDirections(String eventDirections) {
            }

        }

    }


    public class SectionPageAdapter extends FragmentPagerAdapter {
        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceHolderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Events";
                case 1:
                    return "Adverts";
                case 2:
                    return "People";
            }
            return null;
        }
    }
}
