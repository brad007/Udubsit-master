package com.ionicframework.udubsit252887.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.BaseActivity;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Users;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FirebaseUser FIREBASE_USER;
    private FragmentTransaction fragmentTransaction;
    private ImageView userDisplay;
    private TextView userEmail;
    private TextView username;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FIREBASE_USER = mFirebaseUser;
        //Keeping track of which screen was last shown and displaying it on startup
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int position = sp.getInt(Constants.POSITION, 1);
        if (position == 1) {
            fragmentTransaction.replace(R.id.container, new GroupFragment()).commit();
        }

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

        userEmail.setText(Utils.getUserEmail().replace(",","."));

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
        getMenuInflater().inflate(R.menu.main, menu);
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
            fragmentTransaction.replace(R.id.container, new GroupFragment()).commit();
        } else if (id == R.id.nav_points_of_interest) {
            fragmentTransaction.replace(R.id.container, new POIFragment()).commit();
        } else if (id == R.id.nav_schedule) {
            fragmentTransaction.replace(R.id.container, new SchedualFragment()).commit();
        } else if (id == R.id.nav_events) {
            fragmentTransaction.replace(R.id.container, new MyEventsFragment()).commit();
        } else if (id == R.id.nav_logout) {
            signOut();
            //startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }else if(id == R.id.nav_adverts){
            fragmentTransaction.replace(R.id.container, new MyAdsFragment()).commit();
        }else if(id == R.id.nav_ikamva){
            fragmentTransaction.replace(R.id.container, new IkamvaFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
