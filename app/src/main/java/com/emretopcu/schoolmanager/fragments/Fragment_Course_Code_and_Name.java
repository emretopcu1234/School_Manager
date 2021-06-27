package com.emretopcu.schoolmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.emretopcu.schoolmanager.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Fragment_Course_Code_and_Name extends Fragment {

    View view;
    TextView textViewCourseCode;
    TextView textViewCourseName;
    Button buttonPrevious;
    Button buttonNext;

    public Fragment_Course_Code_and_Name() {
        super(R.layout.fragment_course_code_and_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            view = inflater.inflate(R.layout.fragment_course_code_and_name, container, false);

            textViewCourseCode = (TextView) view.findViewById(R.id.textView_course_code);
            textViewCourseName = (TextView) view.findViewById(R.id.textView_course_name);
            buttonPrevious = (Button) view.findViewById(R.id.button_previous);
            buttonNext = (Button) view.findViewById(R.id.button_next);

            return view;
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Fragment_Course_Code_and_Name class' onCreateView method.");
            return null;
        }
    }
}
