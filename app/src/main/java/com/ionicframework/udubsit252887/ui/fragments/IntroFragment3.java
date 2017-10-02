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

public class IntroFragment3 extends Fragment {
    //    Intro Join Events
    public IntroFragment3(){
        //required empty public constructor

    }
    public static IntroFragment3 newInstance(){return new IntroFragment3();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Inlflate layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_intro_3,container,false);

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
