package com.ionicframework.udubsit252887.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.UploadImage;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Event;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AddEventActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    static final int PLACE_PICKER_REQUEST = 2;
    private static final String TAG = AddEventActivity.class.getSimpleName();
    private static final int IMAGE_PICKER = 1;
    String[] ITEMS = {"Academic", "Sports", "Culture", "Religion", "Social", "General", "Residence", "Academic Support"};
    ArrayAdapter<String> adapter;
    private EditText eventTitleView;
    private Switch allDaySwitch;
    private TextView startDateView;
    private TextView startTimeView;
    private TextView endDateView;
    private TextView endTimeView;
    private LinearLayout addLocationView;
    private ImageView addImageView;
    private boolean update;
    private String groupId;
    private String eventGroup;
    private String eventId;
    private Calendar now;
    private Event event;
    private Date startDate = new Date(System.currentTimeMillis());

    /*
    String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 spinner = (MaterialSpinner) findViewById(R.id.spinner);
 spinner.setAdapter(adapter);
     */
    private Date endDate = new Date(System.currentTimeMillis());
    private Bitmap image;
    private MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //set start & enddate default to date today
//        TextView startdate= (TextView) findViewById(R.id.start_date_text);
//        SimpleDateFormat dfDate_day= new SimpleDateFormat("dd/MM/yyyy");
//        Calendar c = Calendar.getInstance();
//        String data=dfDate_day.format(c.getTime());
//        Log.d("DateTime", data);
//        startdate.setText(data);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseScreen();
        Intent intent = getIntent();
        groupId = intent.getStringExtra(Constants.GROUP_ID_KEY);
        eventId = intent.getStringExtra(Constants.EVENT_ID_KEY);
        eventGroup = intent.getStringExtra(Constants.EVENT_GROUP);
        event = new Event();
        work();
        now = Calendar.getInstance();



        update = intent.getBooleanExtra(Constants.UPDATE, false);
        if (update) {

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendEvent() {
        event.setOwner(Utils.getUserEmail());
        event.setGroupId(groupId);

        event.setTitle(eventTitleView.getText().toString());

        event.setDescription(null);

        event.setStartDate(startDate.getTime());
        event.setEndDate(endDate.getTime());

        if (event.getTitle() == null) {
            event.setTitle("");
        } else if (event.getTitle().length() < 10) {
            Utils.showToast(AddEventActivity.this, "Title needs to be 10 characters long");
        } else {
            if (event.getDescription() == null) {
                event.setDescription("");
            }
            if (event.getLongitude() == 0) {
                Toast.makeText(this, "No location selected",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "No Location");
                return;
            }
            if (event.getEndDate() == 0) {
                Toast.makeText(this, "No end date",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "No end date");
                return;
            }
            if (event.getStartDate() == 0) {
                Toast.makeText(this, "No start date",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "No start date");
                return;
            }
            if (event.getTitle().length() <= 10 || event.getTitle().length() >= 30) {
                Toast.makeText(this, "invalid length",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "invalid length");
                return;
            }
            if (image == null) {
                Toast.makeText(this, "No image selected",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "No image");
                return;
            }
            if (event.getCategory() == null) {
                Toast.makeText(this, "No category selected",Toast.LENGTH_LONG ).show();
                Log.v(TAG, "No category");
                //      return;
            }
            event.setGroup("Academic Info");
            event.setNumOfPeople(0L);
            if (!update) {
                event.setEventId(Utils.getPushId());
                UploadImage uploadImage = new UploadImage(event);
                uploadImage.execute(image);

                finish();
                Toast.makeText(this,"Add Event Succesfull",Toast.LENGTH_LONG).show();
            } else {
                FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                        .child(groupId).child(eventId).setValue(event);
                finish();
                Toast.makeText(this,"Add Event Succesfull",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Log.v(TAG, "Place picker");
                    Place place = PlacePicker.getPlace(data, this);
                    event.setAddress((String) place.getAddress());
                    event.setLocationDescription((String) place.getName());

                    TextView locationText = (TextView) findViewById(R.id.location_text);
                    locationText.setText(event.getLocationDescription() + ", " + event.getAddress());

                    Log.v(TAG, place.getLatLng().longitude + ":" + place.getLatLng().longitude);
                    event.setLongitude(place.getLatLng().longitude);
                    event.setLatitude(place.getLatLng().latitude);
                    break;
                case IMAGE_PICKER:
                    Log.v(TAG, "image picker");
                    Uri imageURI = data.getData();
                    if (imageURI != null) {

                        try {
                            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        addImageView.setImageURI(imageURI);
                    }
                    break;
                default:
                    Log.v(TAG, "None");
                    break;
            }
        } else {

            Log.v(TAG, "Not ok");
        }
    }

    private void work() {
        allDaySwitch.setOnCheckedChangeListener(this);
        startDateView.setOnClickListener(this);
        startTimeView.setOnClickListener(this);
        endDateView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);
        addLocationView.setOnClickListener(this);
        addImageView.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_day_switch:
                setAllDay();
                break;
            case R.id.start_date_text:
                setStartDate();
                break;
            case R.id.start_time_text:
                setStartTime();
                break;
            case R.id.end_date_text:
                setEndDate();
                break;
            case R.id.end_time_text:
                setEndTime();
                break;
            case R.id.add_location_view:
                setLocation();
                break;
            case R.id.event_image:
                setEventImage();
                break;
        }
    }

    private void setEventImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoruPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoruPath);
        Log.v("ImagePath",data.toString());

        photoPickerIntent.setDataAndType(data, "image/*");
        photoPickerIntent.putExtra("crop", "true");

        photoPickerIntent.putExtra("aspectX", 3);
        photoPickerIntent.putExtra("aspectY", 2);

        photoPickerIntent.putExtra("outputX", 280);
        photoPickerIntent.putExtra("outputX", 280);
        Log.v("Photopicerintent",photoPickerIntent.getExtras().toString());
        startActivityForResult(photoPickerIntent, IMAGE_PICKER);
    }

    private void setLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void setEndTime() {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        endDate.setHours(hourOfDay);
                        endDate.setMinutes(minute);
                        endDate.setSeconds(second);

                        endTimeView.setText(hourOfDay + ":" + minute);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "End Time");
    }

    private void setEndDate() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setYear(year);
                        endDate.setMonth(monthOfYear);
                        endDate.setDate(dayOfMonth);

                        endDateView.setText(
                                Utils.getDay(endDate.getDay()) + ", " + Utils.getMonth(monthOfYear) + " " + dayOfMonth + ", " + year
                        );
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        now.setTimeInMillis(System.currentTimeMillis());
        datePickerDialog.setMinDate(now);
        datePickerDialog.show(getFragmentManager(), "End Date");
    }

    private void setStartTime() {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        startDate.setHours(hourOfDay);
                        startDate.setMinutes(minute);
                        startDate.setSeconds(second);

                        startTimeView.setText(hourOfDay + ":" + minute);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "Start Time");
    }

    private void setStartDate() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setYear(year);
                        startDate.setMonth(monthOfYear);
                        startDate.setDate(dayOfMonth);

                        startDateView.setText(
                                Utils.getDay(startDate.getDay()) + ", " + Utils.getMonth(monthOfYear) + " " + dayOfMonth + ", " + year
                        );
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        now.setTimeInMillis(System.currentTimeMillis());
        datePickerDialog.setMinDate(now);
        datePickerDialog.show(getFragmentManager(), "Start Date");
    }


    private void setAllDay() {

    }


    private void initialiseScreen() {

        allDaySwitch = (Switch) findViewById(R.id.all_day_switch);
        eventTitleView = (EditText) findViewById(R.id.event_title_view);
//        eventTitleView.setError("10 - 35 characters");
        eventTitleView.setError(Html.fromHtml("<font color='#ffffff'>10-35 characters</font>"));
//        eventTitleView.setTextColor(Color.WHITE);

        startDateView = (TextView) findViewById(R.id.start_date_text);
        startTimeView = (TextView) findViewById(R.id.start_time_text);

        endDateView = (TextView) findViewById(R.id.end_date_text);
        endTimeView = (TextView) findViewById(R.id.end_time_text);

        addLocationView = (LinearLayout) findViewById(R.id.add_location_view);
        addImageView = (ImageView) findViewById(R.id.event_image);

        Arrays.sort(ITEMS);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                Log.v(TAG, "add event");
                sendEvent();
                return true;
            case R.drawable.ic_arrow_back_black_24dp:
                Log.d("BackPressed","True");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            //todo:set time

        } else {
            //todo:set time
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = (String) adapterView.getSelectedItem();
        event.setCategory(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String item = (String) adapterView.getSelectedItem();
        event.setCategory(item);
    }
}
