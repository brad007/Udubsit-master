package com.ionicframework.udubsit252887.ui.fragments;


import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ionicframework.udubsit252887.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IkamvaFragment extends Fragment {

    WebView ikamva;

    private View rootView;

    public IkamvaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ikamva, container, false);
        ikamva = (WebView) rootView.findViewById(R.id.ikamvaWebview);
        ikamva.loadUrl("https://ikamva.uwc.ac.za/");
        ikamva.getSettings().setJavaScriptEnabled(true);
        ikamva.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        ikamva.setWebViewClient(new WebViewClient());

        ikamva.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(i== KeyEvent.KEYCODE_BACK  )
                {
                    ikamva.goBack();

                }
                return true;


            }
        });
        return rootView;
    }



}
