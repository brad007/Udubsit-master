package com.ionicframework.udubsit252887.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.dialogs.EventOptionsDialog;
import com.ionicframework.udubsit252887.dialogs.RSVPDialog;
import com.ionicframework.udubsit252887.models.Comment;
import com.ionicframework.udubsit252887.models.Event;
import com.ionicframework.udubsit252887.models.EventImageUrl;
import com.ionicframework.udubsit252887.models.Users;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewEventActivity extends AppCompatActivity {

    private static FloatingActionButton fab;
    private static String imageUrl;
    private static String groupID;
    private static String eventID;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        groupID = getIntent().getStringExtra(Constants.GROUP_ID_KEY);
        eventID = getIntent().getStringExtra(Constants.EVENT_ID_KEY);
        imageUrl = getIntent().getStringExtra(Constants.IMAGE_URL_KEY);
        email = getIntent().getStringExtra(Constants.EMAIL_KEY);
        ImageView eventToolbarImage = (ImageView) findViewById(R.id.event_toolbar_image);
        Glide.with(this)
                .load(imageUrl)
                .into(eventToolbarImage);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_event, menu);
        final MenuItem rsvp = menu.findItem(R.id.action_rsvp);
        final MenuItem update = menu.findItem(R.id.action_update);
        MenuItem updateEvent = menu.findItem(R.id.action_update_event);
        MenuItem removeEvent = menu.findItem(R.id.action_remove_event);

        if (email.equals(Utils.getUserEmail())) {
            updateEvent.setVisible(true);
            removeEvent.setVisible(true);
        }

        FirebaseDatabase.getInstance().getReference(Constants.EVENT_ATTENDING_KEY)
                .child(eventID)
                .child(Utils.getUserEmail().replace(".", ","))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            rsvp.setVisible(true);
                            update.setVisible(false);
                        } else {
                            rsvp.setVisible(false);
                            update.setVisible(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_rsvp) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.EVENT_ATTENDING_KEY)
                    .child(eventID)
                    .child(Utils.getUserEmail().replace(".", ",")
                            /*FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",")*/);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RSVPDialog dialogFragment;
                    if (dataSnapshot.getValue() != null) {
                        long num = (long) dataSnapshot.getValue();
                        dialogFragment = new RSVPDialog(num, eventID, groupID, false);
                    } else {
                        dialogFragment = new RSVPDialog(0, eventID, groupID, false);
                    }
                    dialogFragment.show(getSupportFragmentManager(), null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        } else if (id == R.id.action_update) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.EVENT_ATTENDING_KEY)
                    .child(eventID)
                    .child(Utils.getUserEmail().replace(".", ",")
                            /*FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",")*/);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RSVPDialog dialogFragment;
                    if (dataSnapshot.getValue() != null) {
                        long num = (long) dataSnapshot.getValue();
                        dialogFragment = new RSVPDialog(num, eventID, groupID, true);
                    } else {
                        dialogFragment = new RSVPDialog(0, eventID, groupID, true);
                    }
                    dialogFragment.show(getSupportFragmentManager(), null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        } else if (id == R.id.action_update_event) {
            Intent intent = new Intent(ViewEventActivity.this, AddEventActivity.class);
            intent.putExtra(Constants.PUSH_ID_KEY, groupID);
            intent.putExtra(Constants.EVENT_ID_KEY, eventID);
            intent.putExtra(Constants.UPDATE, true);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_remove_event) {
            //todo:remove database reference
            //todo:remove storage reference
            //todo:events && eventDetailImages

            final ProgressDialog pd = new ProgressDialog(ViewEventActivity.this);
            pd.setMessage("Removing event");
            pd.setCancelable(false);
            pd.show();

            FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY).child(groupID).child(eventID).removeValue();
            FirebaseDatabase.getInstance().getReference(Constants.EVENTS_DETAIL_IMAGES_KEY).child(eventID).removeValue();

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://udubsit-firebase.appspot.com");
            StorageReference eventStorageRef = storageReference.child(Constants.EVENT_IMAGES).child(eventID);
            eventStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.dismiss();
                    finish();
                }
            });
        } else if (id == R.id.action_report) {
            FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                    .child(groupID).child(eventID).child("reportCounter")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long num = (long) dataSnapshot.getValue();
                            num++;
                            FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                                    .child(groupID).child(eventID).child("reportCounter")
                                    .setValue(num);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final int IMAGE_PICK = 1;
        private View rootView;

        private FirebaseRecyclerAdapter<EventImageUrl, ImageHolder> photoAdapter;
        private FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter;
        private RecyclerView photoRecycler;
        private RecyclerView commentRecycler;
        private TextView startDateView;
        private TextView startTimeView;
        private TextView endDateView;
        private TextView endTimeView;
        private TextView eventLocationView;
        private TextView eventAddressView;
        private TextView eventGroupView;
        private TextView eventCategoryView;
        private CircleImageView eventOwnerDisplayView;
        private TextView eventOwnerView;
        private TextView eventOwnerEmail;
        private CardView locationCard;
        private CardView dateCard;
        private CardView eventContactCard;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_view_event, container, false);
            Log.v("POSITION", getArguments().getInt(ARG_SECTION_NUMBER) + "");
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.view_event_linear_layout);

            photoRecycler = (RecyclerView) rootView.findViewById(R.id.photo_recycler);
            commentRecycler = (RecyclerView) rootView.findViewById(R.id.comment_recycler);


            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            if (position == 1) {
                initialiseScreen();
                /**
                 DETAIL: VISIBLE
                 PHOTOS: GONE
                 COMMMENTS: GONE
                 FAB: GONE
                 */

                linearLayout.setVisibility(View.VISIBLE);
                photoRecycler.setVisibility(View.GONE);
                commentRecycler.setVisibility(View.GONE);
                // TabbedViewEventActivity.fab.setVisibility(View.GONE);
            } else if (position == 2) {
                /**
                 DETAIL: GONE
                 PHOTOS: VISIBLE
                 COMMMENTS: GONE
                 FAB: VISIBLE
                 */

                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                photoRecycler.setLayoutManager(manager);
                setupImageAdapter();

                linearLayout.setVisibility(View.GONE);
                photoRecycler.setVisibility(View.VISIBLE);
                commentRecycler.setVisibility(View.GONE);
                //   TabbedViewEventActivity.fab.setVisibility(View.VISIBLE);
            } else {
                /**
                 DETAIL: GONE
                 PHOTOS: GONE
                 COMMMENTS: VISIBLE
                 FAB: VISIBLE
                 */

                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                commentRecycler.setLayoutManager(manager);
                setupCommentAdapter();

                linearLayout.setVisibility(View.GONE);
                photoRecycler.setVisibility(View.GONE);
                commentRecycler.setVisibility(View.VISIBLE);
                //  TabbedViewEventActivity.fab.setVisibility(View.VISIBLE);
            }
            ViewEventActivity.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogFragment = new EventOptionsDialog(groupID, eventID);
                    todo:
                    dialogFragment.show(getFragmentManager(), null);
                }
            });
            return rootView;
        }

        private void initialiseScreen() {
            startDateView = (TextView) rootView.findViewById(R.id.start_date_text);
            startTimeView = (TextView) rootView.findViewById(R.id.start_time_text);
            endDateView = (TextView) rootView.findViewById(R.id.end_date_text);
            endTimeView = (TextView) rootView.findViewById(R.id.end_time_text);
            dateCard = (CardView) rootView.findViewById(R.id.date_card);

            eventLocationView = (TextView) rootView.findViewById(R.id.event_location);
            eventAddressView = (TextView) rootView.findViewById(R.id.event_address);
            locationCard = (CardView) rootView.findViewById(R.id.location_card);

            eventGroupView = (TextView) rootView.findViewById(R.id.event_group);
            eventCategoryView = (TextView) rootView.findViewById(R.id.event_category);

            eventOwnerDisplayView = (CircleImageView) rootView.findViewById(R.id.event_owner_image);
            eventOwnerView = (TextView) rootView.findViewById(R.id.event_owner);
            eventOwnerEmail = (TextView) rootView.findViewById(R.id.event_owner_email);

            eventContactCard = (CardView) rootView.findViewById(R.id.contact_card);
            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                    .child(groupID)
                    .child(eventID);
            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Event event = dataSnapshot.getValue(Event.class);

                    Date startDate = new Date(event.getStartDate());
                    startDateView.setText(
                            Utils.getDay(startDate.getDay()) + ", " + Utils.getMonth(startDate.getMonth()) + " " + startDate.getDate() + ", " + startDate.getYear()
                    );
                    Date endDate = new Date(event.getEndDate());
                    endDateView.setText(
                            Utils.getDay(endDate.getDay()) + ", " + Utils.getMonth(endDate.getMonth()) + " " + endDate.getDate() + ", " + endDate   .getYear()
                    );

                    Date d = new Date(event.getStartDate());
                    startTimeView.setText("From: " + d.getHours() + ":" + d.getMinutes());
                    d.setTime(-1 * event.getEndDate());
                    endTimeView.setText("To: " + d.getHours() + ":" + d.getMinutes());


                    eventAddressView.setText(event.getAddress());
                    eventCategoryView.setText("Category: " + event.getCategory());
                    eventGroupView.setText("Group: " + event.getGroup());
                    eventLocationView.setText(event.getLocationDescription());
                    eventAddressView.setText(event.getAddress());

                    eventOwnerEmail.setText(event.getOwner().replace(",", "."));

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                            .child(event.getOwner().replace(".", ","));
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            eventOwnerView.setText(users.getUser());
                            Glide.with(getActivity())
                                    .load(users.getPhotoUri())
                                    .into(eventOwnerDisplayView);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    locationCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.getLatitude() + "," + event.getLongitude() + "(" + event.getLocationDescription() + ")=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });

                    eventContactCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra(Constants.EMAIL_KEY, event.getOwner());
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private String getTimeFormat(String time) {
            String[] A = time.split(":");

            if (Integer.parseInt(A[0]) > 12) {
                return time + " PM";
            } else {
                return time + " AM";
            }
        }

        private String getDateFormat(String date) {
            String[] A = date.split("/");
            Date d = new Date();
            d.setDate(Integer.parseInt(A[0]));
            d.setMonth(Integer.parseInt(A[1]));
            d.setYear(Integer.parseInt(A[2]));

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            String day = "";
            String month = "";
            switch (dayOfWeek) {
                case 1:
                    day = "Monday";
                    break;
                case 2:
                    day = "Tuesday";
                    break;
                case 3:
                    day = "Wednesday";
                    break;
                case 4:
                    day = "Thursday";
                    break;
                case 5:
                    day = "Friday";
                    break;
                case 6:
                    day = "Saturday";
                    break;
                default:
                    day = "Sunday";
                    break;
            }

            switch (c.get(Calendar.MONTH)) {
                case 1:
                    month = "Jan";
                    break;
                case 2:
                    month = "Feb";
                    break;
                case 3:
                    month = "March";
                    break;
                case 4:
                    month = "April";
                    break;
                case 5:
                    month = "May";
                    break;
                case 6:
                    month = "June";
                    break;
                case 7:
                    month = "July";
                    break;
                case 8:
                    month = "Aug";
                    break;
                case 9:
                    month = "Sept";
                    break;
                case 10:
                    month = "Oct";
                    break;
                case 11:
                    month = "Nov";
                    break;
                default:
                    month = "Dec";
            }

            return day + ", " + month + " " + A[0];
        }


        private void setupCommentAdapter() {
            commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                    Comment.class,
                    R.layout.item_layout_comment,
                    CommentHolder.class,
                    FirebaseDatabase.getInstance().getReference(Constants.COMMENTS_KEY).child(eventID)
            ) {
                @Override
                protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {

                    viewHolder.commentText.setText(model.getText());


                    viewHolder.commmentDisplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra(Constants.EMAIL_KEY, model.getOwner());
                            startActivity(intent);
                        }
                    });
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(
                            Constants.USERS_KEY
                    ).child(model.getOwner().replace(".", ","));
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            viewHolder.setUsernameText(users.getUser());
                            if (users.getPhotoUri() != null) {
                                Glide.with(getActivity())
                                        .load(users.getPhotoUri())
                                        .into(viewHolder.commmentDisplay);

                            } else {
                                viewHolder.commmentDisplay.setImageResource(R.drawable.facebook_avatar);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            };
            commentRecycler.setAdapter(commentAdapter);
        }

        private void setupImageAdapter() {
            Log.v("Event ID", eventID);
            photoAdapter = new FirebaseRecyclerAdapter<EventImageUrl, ImageHolder>(
                    EventImageUrl.class,
                    R.layout.item_layout_image,
                    ImageHolder.class,
                    FirebaseDatabase.getInstance().getReference(Constants.EVENT_IMAGES).child(eventID)
            ) {
                @Override
                protected void populateViewHolder(ImageHolder viewHolder, final EventImageUrl model, int position) {
                    Glide.with(getActivity())
                            .load(model.getThumbnail())
                            .into(viewHolder.eventImage);

                    viewHolder.eventImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ViewImageActivity.class);
                            intent.putExtra("ImageUrl", model.getOriginal());
                            startActivity(intent);
                        }
                    });
                }
            };

            photoRecycler.setAdapter(photoAdapter);
        }

        public static class ImageHolder extends RecyclerView.ViewHolder {
            ImageView eventImage;

            public ImageHolder(View itemView) {
                super(itemView);
                eventImage = (ImageView) itemView.findViewById(R.id.event_image);
            }
        }

        public static class CommentHolder extends RecyclerView.ViewHolder {

            public ImageView commmentDisplay;
            public TextView timeText;
            public TextView usernameText;
            public TextView commentText;

            public CommentHolder(View itemView) {
                super(itemView);

                commmentDisplay = (ImageView) itemView.findViewById(R.id.comment_image);
                timeText = (TextView) itemView.findViewById(R.id.time);
                usernameText = (TextView) itemView.findViewById(R.id.comment_username);
                commentText = (TextView) itemView.findViewById(R.id.comment_text);
            }

            public void setTime(long time) {
                String textTime = (String) DateUtils.getRelativeTimeSpanString(time);
                if (textTime.contains("0 minutes")) {
                    textTime = "a moment ago";
                } else if (textTime.contains("minutes ago")) {
                    textTime = textTime.replace("minutes ago", "mins");
                } else if (textTime.contains("hours ago")) {
                    textTime = textTime.replace("hours ago", "hrs");
                    Log.v("TIME", textTime);
                } else if (textTime.contains("days ago")) {
                    textTime = textTime.replace("days ago", "days");
                }
                timeText.setText(textTime);
            }

            public void setUsernameText(String username) {
                usernameText.setText(username);
            }

            public void setCommentText(String comment) {
                commentText.setText(comment);
            }
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "PHOTOS";
                case 2:
                    return "COMMENTS";
            }
            return null;
        }
    }
}
