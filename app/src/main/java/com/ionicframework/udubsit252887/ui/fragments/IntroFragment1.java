package com.ionicframework.udubsit252887.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ionicframework.udubsit252887.R;

/**
 * Created by Test on 14/03/2017.
 */

public class IntroFragment1 extends Fragment {

//    Intro Join Events
    public IntroFragment1(){
        //required empty public constructor

    }
    public static IntroFragment1 newInstance(){return new IntroFragment1();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Inlflate layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_intro_1,container,false);

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
