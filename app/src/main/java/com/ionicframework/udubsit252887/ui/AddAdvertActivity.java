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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    String[] ITEMS = {"Advertisements", "Accomodation", "Special offers", "Menu", "Jobs(Part time)", "Jobs(Full time)", "Books (new)", "Books (second hand)",
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

        findViewById(R.id.end_date_text).setOnClickListener(this);

        findViewById(R.id.ad_image).setOnClickListener(this);
        findViewById(R.id.add_location_view).setOnClickListener(this);
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

        AutoCompleteTextView adTitle = (AutoCompleteTextView) findViewById(R.id.adTitle);
        ads.setAdvertDescription(adTitle.getText().toString());

        AutoCompleteTextView adCost = (AutoCompleteTextView) findViewById(R.id.adCost);
        ads.setAdvertCost(Double.parseDouble(adCost.getText().toString()));
        ads.setAdvertOwner(Utils.getUserEmail());

        if(ads.getAdvertDue() == 0){
            Toast.makeText(AddAdvertActivity.this, "No date set", Toast.LENGTH_SHORT).show();
            return;
        }

        if(image == null){
            Toast.makeText(AddAdvertActivity.this, "No image set", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ads.getAdvertCategory() == null){
            Toast.makeText(AddAdvertActivity.this, "No category set", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ads.getAdvertCost() == 0){
            Toast.makeText(AddAdvertActivity.this, "No cost set", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ads.getLatitude() == 0 && ads.getLongitude() == 0){
            Toast.makeText(AddAdvertActivity.this, "No location set", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ads.getAdvertDescription() == null){
            Toast.makeText(AddAdvertActivity.this, "No title set", Toast.LENGTH_SHORT).show();
            return;
        }

        new UploadImage(ads).execute(image);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.end_date_text:
                addDate();
                break;
            case R.id.add_location_view:
                addPlace();
                break;
            case R.id.ad_image:
                addImage();
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

                    /*
                    Log.v(TAG, "Place picker");
                    Place place = PlacePicker.getPlace(data, this);
                    event.setAddress((String) place.getAddress());
                    event.setLocationDescription((String) place.getName());

                    TextView locationText = (TextView) findViewById(R.id.location_text);
                    locationText.setText(event.getLocationDescription() + ", " + event.getAddress());

                    Log.v(TAG, place.getLatLng().longitude + ":" + place.getLatLng().longitude);
                    event.setLongitude(place.getLatLng().longitude);
                    event.setLatitude(place.getLatLng().latitude);
                     */
                    Place place = PlacePicker.getPlace(data, this);

                    String placeName = (String) place.getName();
                    placeName += (", " + place.getAddress());

                    TextView location = (TextView) findViewById(R.id.location_text);
                    location.setText(placeName);
                    ads.setLatitude(place.getLatLng().latitude);
                    ads.setLongitude(place.getLatLng().longitude);
                    break;
                case IMAGE_PICKER:
                    Bundle extras = data.getExtras();
                    image = extras.getParcelable("data");
                    ImageView imageView = (ImageView) findViewById(R.id.ad_image);
                    imageView.setImageBitmap(image);
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

        TextView dateText = (TextView) findViewById(R.id.end_date_text);
        dateText.setText(Utils.getDate(date.getTime()));
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
