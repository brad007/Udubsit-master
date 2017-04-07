package com.ionicframework.udubsit252887.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.BaseActivity;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Users;
import com.ionicframework.udubsit252887.ui.fragments.GroupFragment;
import com.ionicframework.udubsit252887.ui.fragments.IkamvaFragment;
import com.ionicframework.udubsit252887.ui.fragments.MyAdsFragment;
import com.ionicframework.udubsit252887.ui.fragments.MyEventsFragment;
import com.ionicframework.udubsit252887.ui.fragments.POIFragment;
import com.ionicframework.udubsit252887.ui.fragments.SchedualFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 1;
    public static FirebaseUser FIREBASE_USER;
    Bundle args = new Bundle();
    private FragmentTransaction fragmentTransaction;
    private ImageView userDisplay;
    private TextView userEmail;
    private TextView username;
    private NavigationView navigationView;
    private SharedPreferences sp;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference("groupManagers");




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Has No permissions. Requesting....");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_CALENDAR
                    },
                    REQUEST_PERMISSIONS);

        }




        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FIREBASE_USER = mFirebaseUser;


        if (args.getInt(ARG_PAGE_NUMBER) < 1) {
            sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            int page = sp.getInt(ARG_PAGE_NUMBER, 1);
            args.putInt(ARG_PAGE_NUMBER, page);
        }
        setPage(args.getInt(ARG_PAGE_NUMBER));

        /*Keeping track of which screen was last shown and displaying it on startup
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int position = sp.getInt(Constants.POSITION, 1);
        if (position == 1) {
            fragmentTransaction.replace(R.id.container, new GroupFragment()).commit();
        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Utils.getUserEmail() == null) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        } else {
            setupNav();
        }

    }

    private void setupNav() {
        View view = navigationView.getHeaderView(0);
        userDisplay = (ImageView) view.findViewById(R.id.user_display);
        username = (TextView) view.findViewById(R.id.username);
        userEmail = (TextView) view.findViewById(R.id.user_email);

        userEmail.setText(Utils.getUserEmail().replace(",", "."));

        FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                .child(Utils.getUserEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        username.setText(user.getUser());
                        Glide.with(MainActivity.this)
                                .load(user.getPhotoUri())
                                .into(userDisplay);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO: 2/03/2017  Fix Settings fragment
//        getMenuInflater().inflate(R.menu.main, menu);
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
        }//todo:  else if(id == R.id.action_logout){
        //todo:     signOut();
        //todo: startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        //todo:    }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_groups) {
            setPage(1);
        } else if (id == R.id.nav_points_of_interest) {
            setPage(2);
        } else if (id == R.id.nav_schedule) {
            setPage(3);
        } else if (id == R.id.nav_events) {
            setPage(4);
        } else if (id == R.id.nav_logout) {
            signOut();
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        } else if (id == R.id.nav_adverts) {
            setPage(5);
        } else if (id == R.id.nav_ikamva) {
            setPage(6);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setPage(int page) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (page) {
            case 1:
                inflateFragment(new GroupFragment(), page);
                break;
            case 2:
                inflateFragment(new POIFragment(), page);
                break;
            case 3:
                inflateFragment(new SchedualFragment(), page);
                break;
            case 4:
                inflateFragment(new MyEventsFragment(), page);
                break;
            case 5:
                inflateFragment(new MyAdsFragment(), page);
                break;
            case 6:
                inflateFragment(new IkamvaFragment(), page);
                break;
        }
    }

    private void inflateFragment(Fragment fragment, int page) {
        args.putInt(ARG_PAGE_NUMBER, page);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(ARG_PAGE_NUMBER, page).apply();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        sp.edit().putInt(ARG_PAGE_NUMBER, 1).apply();
        super.onDestroy();
    }
}
