package com.ionicframework.udubsit252887.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.ionicframework.udubsit252887.R;

import static com.ionicframework.udubsit252887.ui.activities.MainActivity.webViewURL;

/**
 * Created by Marco Bezuidenhout on 23/10/2017.
 */

public class FormsWebView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_forms);

        String url = webViewURL.toString();
        WebView forms = (WebView) findViewById(R.id.formsWebview1);
        forms.loadUrl(url);
    }
}
