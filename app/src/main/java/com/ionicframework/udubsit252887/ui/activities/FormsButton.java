package com.ionicframework.udubsit252887.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ionicframework.udubsit252887.R;

import static com.ionicframework.udubsit252887.ui.activities.MainActivity.webViewURL;

/**
 * Created by Marco Bezuidenhout on 23/10/2017.
 */

public class FormsButton extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.button_forms);

        Button b1 = (Button) findViewById(R.id.buttonUrlRD);
        b1.setOnClickListener(this); // calling onClick() method
        Button b2 = (Button) findViewById(R.id.buttonUrlGAP);
        b2.setOnClickListener(this);
        Button b3 = (Button) findViewById(R.id.buttonUrlGA);
        b3.setOnClickListener(this);
        Button b4 = (Button) findViewById(R.id.buttonUrlRES);
        b4.setOnClickListener(this);
        Button b5 = (Button) findViewById(R.id.buttonUrlRC);
        b5.setOnClickListener(this);
        Button b6 = (Button) findViewById(R.id.buttonUrlCS);
        b6.setOnClickListener(this);
        Button b7 = (Button) findViewById(R.id.buttonUrlCA);
        b7.setOnClickListener(this);
        Button b8 = (Button) findViewById(R.id.buttonUrlAR);
        b8.setOnClickListener(this);
        Button b9 = (Button) findViewById(R.id.buttonUrlLA);
        b9.setOnClickListener(this);
        Button b10 = (Button) findViewById(R.id.buttonUrlASS);
        b10.setOnClickListener(this);
        Button b11 = (Button) findViewById(R.id.buttonUrlPCR);
        b11.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUrlRD:
                webViewURL = "http://form.jotformpro.com/form/51512141759957";
                break;
            case R.id.buttonUrlGAP:
                webViewURL = "https://form.jotform.com/60542089838969";
                break;
            case R.id.buttonUrlGA:
                webViewURL = "https://form.jotform.com/60260684406958";
                break;
            case R.id.buttonUrlRES:
                webViewURL = "http://form.jotformpro.com/form/51512682517959";
                break;
            case R.id.buttonUrlRC:
                webViewURL = "http://form.myjotform.com/form/51322953322549";
                break;
            case R.id.buttonUrlCS:
                webViewURL = "https://form.jotform.com/61233413935956";
                break;
            case R.id.buttonUrlCA:
                webViewURL = "http://form.jotformpro.com/form/51512423251947";
                break;
            case R.id.buttonUrlAR:
                webViewURL = "https://www.uwc.ac.za/Students/Pages/Readmission.aspx";
                break;
            case R.id.buttonUrlLA:
                webViewURL = "https://www.uwc.ac.za/Students/Pages/Absence.aspx";
                break;
            case R.id.buttonUrlASS:
                webViewURL = "https://www.uwc.ac.za/Students/Pages/Associate-Student-Status.aspx";
                break;
            case R.id.buttonUrlPCR:
                webViewURL = "https://form.myjotform.com/uwc2/conductresearch";
                break;
            default:
                webViewURL = "https://www.uwc.ac.za/Students/Pages/Admin-Processes-and-Forms.aspx";
                break;
        }
        Intent intent = new Intent(FormsButton.this, FormsWebView.class);
        FormsButton.this.startActivity(intent);
    }
}