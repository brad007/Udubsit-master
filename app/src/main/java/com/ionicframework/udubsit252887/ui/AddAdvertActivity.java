package com.ionicframework.udubsit252887.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.UploadImage;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Ads;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AddAdvertActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int IMAGE_PICKER = 2;
    private Ads ads;
    private Calendar now;
    private Bitmap image;
/*

Advertisements
Accommodation
Special offers
Menu
Jobs (Part time)
Jobs (Full time)
Books (new)
Books (2nd hand)
Class notes
Sporting goods
Tutoring
Miscellaneous

 */

    String[]ITEMS = {"Advertisements", "Accomodation", "Special offers", "Menu", "Jobs(Part time)", "Jobs(Full time)", "Books (new)", "Books (second hand)",
    "Class notes", "Sporting goods", "Tutoring", "Miscellaneous"};


    ArrayAdapter<String> adapter;
    private MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);
        initialiseScreen();
        now = Calendar.getInstance();
    }

    private void initialiseScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.adImage).setOnClickListener(this);
        findViewById(R.id.adPlace).setOnClickListener(this);
        findViewById(R.id.adDate).setOnClickListener(this);

        ads = new Ads();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void createAd() {
        ads.setAdvertId(Utils.getPushId());
        ads.setAdvertGroupId(getIntent().getStringExtra(Constants.PUSH_ID_KEY));

        EditText adTitle = (EditText) findViewById(R.id.adTitle);
        ads.setAdvertDescription(adTitle.getText().toString());

        EditText adCost = (EditText) findViewById(R.id.adCost);
        ads.setAdvertCost(Double.parseDouble(adCost.getText().toString()));
        ads.setAdvertOwner(Utils.getUserEmail());

        new UploadImage(ads).execute(image);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adImage:
                addImage();
                break;
            case R.id.adPlace:
                addPlace();
                break;
            case R.id.adDate:
                addDate();
                break;
            case R.id.fab:
                createAd();
                break;
        }
    }

    private void addDate() {
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog =
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        AddAdvertActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
        now.setTimeInMillis(System.currentTimeMillis());
        datePickerDialog.setMinDate(now);
        datePickerDialog.show(getFragmentManager(), "Start Date");
    }


    private void addPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void addImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoruPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoruPath);

        photoPickerIntent.setDataAndType(data, "image/*");
        photoPickerIntent.putExtra("crop", "true");

        photoPickerIntent.putExtra("aspectX", 3);
        photoPickerIntent.putExtra("aspectY", 2);

        photoPickerIntent.putExtra("outputX", 280);
        photoPickerIntent.putExtra("outputX", 280);

        startActivityForResult(photoPickerIntent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(data, this);
                    ads.setLatitude(place.getLatLng().latitude);
                    ads.setLongitude(place.getLatLng().longitude);
                    break;
                case IMAGE_PICKER:
                    Bundle extras = data.getExtras();
                    image = extras.getParcelable("data");
                    break;
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new Date();
        date.setYear(year);
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
        ads.setAdvertDue(date.getTime());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = (String) adapterView.getSelectedItem();
        ads.setAdvertCategory(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String item = (String) adapterView.getSelectedItem();
        ads.setAdvertCategory(item);
    }
}
