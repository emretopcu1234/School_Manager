package com.emretopcu.schoolmanager.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emretopcu.schoolmanager.R;
import com.emretopcu.schoolmanager.view.Common_Variables_View;
import com.emretopcu.schoolmanager.view.Helper_Dialog_Change_Password;
import com.emretopcu.schoolmanager.view.fragments.Fragment_User_and_Semester;
import com.emretopcu.schoolmanager.view.interfaces.Interface_Fragment_User_and_Semester;
import com.emretopcu.schoolmanager.view.interfaces.Interface_General_Activity;
import com.emretopcu.schoolmanager.view.recyclerviews.RecyclerViewAdapter_Filter_Department;
import com.emretopcu.schoolmanager.view.recyclerviews.RecyclerViewAdapter_Main_Admin_Departments;
import com.emretopcu.schoolmanager.view.recyclerviews.RecyclerViewAdapter_Main_Admin_Dept_Admins;
import com.emretopcu.schoolmanager.view.recyclerviews.RecyclerViewAdapter_Main_Admin_Lecturers;
import com.emretopcu.schoolmanager.viewmodel.enums.E_Successful_Unsuccessful_NoStatement;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Change_Password_State;
import com.emretopcu.schoolmanager.viewmodel.vm.VM_Login_Process;
import com.emretopcu.schoolmanager.viewmodel.vm.VM_Main_Admin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Activity_Main_Admin_Lecturers extends AppCompatActivity implements Interface_General_Activity {

    private Interface_Fragment_User_and_Semester fragmentUserAndSemester;

    private RecyclerViewAdapter_Main_Admin_Lecturers adapter;
    private RecyclerView recyclerViewMainAdminLecturers;
    private LinearLayoutManager layoutManager;

    private RecyclerViewAdapter_Filter_Department adapterFilter;
    private RecyclerView recyclerViewMainAdminLecturersFilter;
    private LinearLayoutManager layoutManagerFilter;

    private AlertDialog.Builder builderLecturer;
    private View viewDialogLecturer;
    private AlertDialog alertDialogLecturer;

    private AlertDialog.Builder builderFilter;
    private View viewDialogFilter;
    private AlertDialog alertDialogFilter;

    private Button buttonFilterClear;
    private Button buttonFilterOK;
    private Button buttonFilterCancel;

    private AlertDialog.Builder builderChangePassword;
    private View viewDialogChangePassword;
    private AlertDialog alertDialogChangePassword;

    private EditText editTextDialogChangePasswordOldPassword;
    private EditText editTextDialogChangePasswordNewPassword;
    private EditText editTextDialogChangePasswordNewPasswordConfirm;
    private TextView textViewDialogChangePassword;
    private Button buttonDialogChangePasswordOK;
    private Button buttonDialogChangePasswordCancel;
    private Helper_Dialog_Change_Password helperDialogChangePassword;

    private Button buttonAddDelete;
    private Button buttonSelectCancel;
    private Button buttonSearchId;
    private Button buttonCancelSearchId;
    private Button buttonSearchName;
    private Button buttonCancelSearchName;
    private Button buttonSearchSurname;
    private Button buttonCancelSearchSurname;
    private Button buttonFilterDeptName;
    private Button buttonCancelFilterDeptName;

    private TextView textViewId;
    private EditText editTextId;
    private TextView textViewName;
    private EditText editTextName;
    private TextView textViewSurname;
    private EditText editTextSurname;

    private BottomNavigationView bottomNavigationView;
    private Toast toastMessage;

    private VM_Login_Process vmLoginProcess;
    private VM_Main_Admin vmMainAdmin;

    private ProgressBar progressBarLecturer;
    private boolean progressBarIndicator_isSemesterActive;
    private boolean progressBarIndicator_setLecturers;
    private boolean progressBarIndicator_setDepartments;
    private String idFilter = "";
    private String nameFilter = "";
    private String surnameFilter = "";
    private ArrayList<String> deptFilter = new ArrayList<>();
    private ArrayList<Boolean> previousChecks = new ArrayList<>();

    private ProgressBar progressBarChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_main_admin_lecturers);

            fragmentUserAndSemester = new Fragment_User_and_Semester(this);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_main_admin_user_and_semester, (Fragment) fragmentUserAndSemester, null)
                    .commit();

            recyclerViewMainAdminLecturers = findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(this);
            recyclerViewMainAdminLecturers.setLayoutManager(layoutManager);

            builderLecturer = new AlertDialog.Builder(this);
            viewDialogLecturer = this.getLayoutInflater().inflate(R.layout.dialog_main_admin_lecturers, null);
            builderLecturer.setView(viewDialogLecturer);
            alertDialogLecturer = builderLecturer.create();
            alertDialogLecturer.setCancelable(false);
            alertDialogLecturer.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            builderFilter = new AlertDialog.Builder(this);
            viewDialogFilter = this.getLayoutInflater().inflate(R.layout.dialog_filter_department, null);
            builderFilter.setView(viewDialogFilter);
            alertDialogFilter = builderFilter.create();
            alertDialogFilter.setCancelable(false);
            alertDialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            recyclerViewMainAdminLecturersFilter = viewDialogFilter.findViewById(R.id.recyclerView);
            layoutManagerFilter = new LinearLayoutManager(this);
            recyclerViewMainAdminLecturersFilter.setLayoutManager(layoutManagerFilter);

            buttonFilterClear = viewDialogFilter.findViewById(R.id.button_clear);
            buttonFilterOK = viewDialogFilter.findViewById(R.id.button_ok);
            buttonFilterCancel = viewDialogFilter.findViewById(R.id.button_cancel);
            buttonFilterClear.setOnClickListener(v -> {
                try{
                    adapterFilter.resetChecks();
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonFilterClear setOnClickListener method.");
                }
            });
            buttonFilterOK.setOnClickListener(v -> {
                try{
                    previousChecks.clear();
                    for(int i=0;i<adapterFilter.getChecks().size();i++){
                        previousChecks.add(adapterFilter.getChecks().get(i));
                    }
                    deptFilter = adapterFilter.getFilteredDepartmentList();
                    if(deptFilter.size() > 0){
                        buttonFilterDeptName.setVisibility(View.INVISIBLE);
                        buttonCancelFilterDeptName.setVisibility(View.VISIBLE);
                    }
                    else{
                        buttonFilterDeptName.setVisibility(View.VISIBLE);
                        buttonCancelFilterDeptName.setVisibility(View.INVISIBLE);
                    }
                    vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                    alertDialogFilter.dismiss();
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonFilterOK setOnClickListener method.");
                }
            });
            buttonFilterCancel.setOnClickListener(v -> {
                try{
                    adapterFilter.setChecks(previousChecks);
                    alertDialogFilter.dismiss();
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonFilterCancel setOnClickListener method.");
                }
            });

            builderChangePassword = new AlertDialog.Builder(this);
            viewDialogChangePassword = this.getLayoutInflater().inflate(R.layout.dialog_change_password, null);
            builderChangePassword.setView(viewDialogChangePassword);
            alertDialogChangePassword = builderChangePassword.create();
            alertDialogChangePassword.setCancelable(false);
            alertDialogChangePassword.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            editTextDialogChangePasswordOldPassword = viewDialogChangePassword.findViewById(R.id.editText_old_password);
            editTextDialogChangePasswordNewPassword = viewDialogChangePassword.findViewById(R.id.editText_new_password);
            editTextDialogChangePasswordNewPasswordConfirm = viewDialogChangePassword.findViewById(R.id.editText_new_password_confirm);
            editTextDialogChangePasswordOldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        buttonDialogChangePasswordOK.setEnabled(helperDialogChangePassword.onFieldChanged(0,s.toString()));
                        if(buttonDialogChangePasswordOK.isEnabled()){
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        }
                        else{
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_black));
                        }
                    }
                    catch(Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextDialogChangePasswordOldPassword onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextDialogChangePasswordNewPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        buttonDialogChangePasswordOK.setEnabled(helperDialogChangePassword.onFieldChanged(1,s.toString()));
                        if(buttonDialogChangePasswordOK.isEnabled()){
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        }
                        else{
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_black));
                        }
                    }
                    catch(Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextDialogChangePasswordNewPassword onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextDialogChangePasswordNewPasswordConfirm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        buttonDialogChangePasswordOK.setEnabled(helperDialogChangePassword.onFieldChanged(2,s.toString()));
                        if(buttonDialogChangePasswordOK.isEnabled()){
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        }
                        else{
                            buttonDialogChangePasswordOK.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_black));
                        }
                    }
                    catch(Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextDialogChangePasswordNewPasswordConfirm onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            textViewDialogChangePassword = viewDialogChangePassword.findViewById(R.id.textView_warning);
            buttonDialogChangePasswordOK = viewDialogChangePassword.findViewById(R.id.button_ok);
            buttonDialogChangePasswordOK.setOnClickListener(v -> {
                try{
                    helperDialogChangePassword.onOKClicked();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonDialogChangePasswordOK setOnClickListener method.");
                }
            });
            buttonDialogChangePasswordCancel = viewDialogChangePassword.findViewById(R.id.button_cancel);
            buttonDialogChangePasswordCancel.setOnClickListener(v -> {
                try{
                    helperDialogChangePassword.onCancelClicked();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonDialogChangePasswordCancel setOnClickListener method.");
                }
            });
            progressBarChangePassword = viewDialogChangePassword.findViewById(R.id.progressBar);
            helperDialogChangePassword = new Helper_Dialog_Change_Password(this);

            buttonAddDelete = findViewById(R.id.button_add_delete);
            buttonAddDelete.setVisibility(View.INVISIBLE);
            buttonAddDelete.setOnClickListener(v -> {
                try{
                    alertDialogLecturer.show();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonAddDelete setOnClickListener method.");
                }
            });

            buttonSelectCancel = findViewById(R.id.button_select_cancel);
            buttonSelectCancel.setVisibility(View.INVISIBLE);
            buttonSelectCancel.setOnClickListener(v -> {
                try{
                    // TODO
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonSelectCancel setOnClickListener method.");
                }
            });

            buttonSearchId = findViewById(R.id.button_search_id);
            buttonSearchId.setOnClickListener(v -> {
                try{
                    buttonSearchId.setVisibility(View.INVISIBLE);
                    buttonCancelSearchId.setVisibility(View.VISIBLE);
                    textViewId.setVisibility(View.INVISIBLE);
                    editTextId.setVisibility(View.VISIBLE);
                    editTextId.requestFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonSearchId setOnClickListener method.");
                }
            });

            buttonCancelSearchId = findViewById(R.id.button_cancel_search_id);
            buttonCancelSearchId.setOnClickListener(v -> {
                try{
                    buttonSearchId.setVisibility(View.VISIBLE);
                    buttonCancelSearchId.setVisibility(View.INVISIBLE);
                    textViewId.setVisibility(View.VISIBLE);
                    editTextId.setText(null);
                    editTextId.setVisibility(View.INVISIBLE);
                    editTextId.clearFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonCancelSearchId setOnClickListener method.");
                }
            });

            buttonSearchName = findViewById(R.id.button_search_name);
            buttonSearchName.setOnClickListener(v -> {
                try{
                    buttonSearchName.setVisibility(View.INVISIBLE);
                    buttonCancelSearchName.setVisibility(View.VISIBLE);
                    textViewName.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                    editTextName.requestFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonSearchName setOnClickListener method.");
                }
            });

            buttonCancelSearchName = findViewById(R.id.button_cancel_search_name);
            buttonCancelSearchName.setOnClickListener(v -> {
                try{
                    buttonSearchName.setVisibility(View.VISIBLE);
                    buttonCancelSearchName.setVisibility(View.INVISIBLE);
                    textViewName.setVisibility(View.VISIBLE);
                    editTextName.setText(null);
                    editTextName.setVisibility(View.INVISIBLE);
                    editTextName.clearFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonCancelSearchName setOnClickListener method.");
                }
            });

            buttonSearchSurname = findViewById(R.id.button_search_surname);
            buttonSearchSurname.setOnClickListener(v -> {
                try{
                    buttonSearchSurname.setVisibility(View.INVISIBLE);
                    buttonCancelSearchSurname.setVisibility(View.VISIBLE);
                    textViewSurname.setVisibility(View.INVISIBLE);
                    editTextSurname.setVisibility(View.VISIBLE);
                    editTextSurname.requestFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonSearchSurname setOnClickListener method.");
                }
            });

            buttonCancelSearchSurname = findViewById(R.id.button_cancel_search_surname);
            buttonCancelSearchSurname.setOnClickListener(v -> {
                try{
                    buttonSearchSurname.setVisibility(View.VISIBLE);
                    buttonCancelSearchSurname.setVisibility(View.INVISIBLE);
                    textViewSurname.setVisibility(View.VISIBLE);
                    editTextSurname.setText(null);
                    editTextSurname.setVisibility(View.INVISIBLE);
                    editTextSurname.clearFocus();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonCancelSearchSurname setOnClickListener method.");
                }
            });

            buttonFilterDeptName = findViewById(R.id.button_filter_empty_closed);
            buttonFilterDeptName.setOnClickListener(v -> {
                try{
                    alertDialogFilter.show();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonFilterDeptName setOnClickListener method.");
                }
            });

            buttonCancelFilterDeptName = findViewById(R.id.button_filter_full_closed);
            buttonCancelFilterDeptName.setOnClickListener(v -> {
                try{
                    alertDialogFilter.show();
                }
                catch(Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' buttonCancelFilterDeptName setOnClickListener method.");
                }
            });

            textViewId = findViewById(R.id.textView_id);
            textViewName = findViewById(R.id.textView_name);
            textViewSurname = findViewById(R.id.textView_surname);
            editTextId = findViewById(R.id.editText_id);
            editTextName = findViewById(R.id.editText_name);
            editTextSurname = findViewById(R.id.editText_surname);
            editTextId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        idFilter = s.toString();
                        if(idFilter.length() == 0){
                            if (buttonSearchId.getVisibility() == View.INVISIBLE){
                                vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                            }
                        }
                        else{
                            progressBarLecturer.setVisibility(View.VISIBLE);
                            vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                        }
                    }
                    catch (Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextId onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        nameFilter = s.toString();
                        if(nameFilter.length() == 0){
                            if (buttonSearchName.getVisibility() == View.INVISIBLE){
                                vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                            }
                        }
                        else{
                            progressBarLecturer.setVisibility(View.VISIBLE);
                            vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                        }
                    }
                    catch (Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextName onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editTextSurname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        surnameFilter = s.toString();
                        if(surnameFilter.length() == 0){
                            if (buttonSearchSurname.getVisibility() == View.INVISIBLE){
                                vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                            }
                        }
                        else{
                            progressBarLecturer.setVisibility(View.VISIBLE);
                            vmMainAdmin.onFilteredLecturerListRequested(Common_Variables_View.SELECTED_SEMESTER,idFilter,nameFilter,surnameFilter,deptFilter);
                        }
                    }
                    catch (Exception e){
                        Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' editTextSurname onTextChanged method.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            bottomNavigationView = findViewById(R.id.bottom_navigation_main_admin);
            bottomNavigationView.getMenu().findItem(R.id.menu_main_admin_lecturers).setChecked(true);

            progressBarLecturer = findViewById(R.id.progressBar);

            vmLoginProcess = new ViewModelProvider(this).get(VM_Login_Process.class);
            vmLoginProcess.getChangePasswordSuccessful().observe(this, e_change_password_state -> {
                try{
                    if(e_change_password_state == E_Change_Password_State.SUCCESSFUL){
                        alertDialogChangePassword.dismiss();
                        showToastMessage(R.string.toast_change_password_successful);
                    }
                    else if(e_change_password_state == E_Change_Password_State.CANNOT_CHANGED){
                        setAndShowWarningOnDialogChangePassword(R.string.warning_change_password_cannot_changed, View.VISIBLE);
                    }
                    else if(e_change_password_state == E_Change_Password_State.WRONG_PASSWORD){
                        setAndShowWarningOnDialogChangePassword(R.string.warning_change_password_wrong_password, View.VISIBLE);
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' vmLoginProcess.getChangePasswordSuccessful().observe method.");
                }
            });

            vmMainAdmin = new ViewModelProvider(this).get(VM_Main_Admin.class);
            vmMainAdmin.getSetSemestersSuccessful().observe(this, e_successful_unsuccessful_noStatement -> {
                try{
                    if(e_successful_unsuccessful_noStatement == E_Successful_Unsuccessful_NoStatement.SUCCESSFUL){
                        fragmentUserAndSemester.setSpinnerList(vmMainAdmin.getSemesterList());
                        fragmentUserAndSemester.setSpinnerItem(Common_Variables_View.SEMESTER_SPINNER_POSITION);
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' vmMainAdmin.getSetSemestersSuccessful().observe method.");
                }
            });
            vmMainAdmin.getIsSemesterActiveSuccessful().observe(this, e_successful_unsuccessful_noStatement -> {
                try{
                    if(e_successful_unsuccessful_noStatement == E_Successful_Unsuccessful_NoStatement.SUCCESSFUL){
                        progressBarIndicator_isSemesterActive = true;
                        if(progressBarIndicator_setLecturers && progressBarIndicator_setDepartments){
                            progressBarLecturer.setVisibility(View.INVISIBLE);
                        }
                        if(vmMainAdmin.isSemesterActive()){
                            buttonAddDelete.setVisibility(View.VISIBLE);
                            buttonSelectCancel.setVisibility(View.VISIBLE);
                        }
                        else{
                            buttonAddDelete.setVisibility(View.INVISIBLE);
                            buttonSelectCancel.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' vmMainAdmin.getIsSemesterActiveSuccessful().observe method.");
                }
            });
            vmMainAdmin.getSetLecturersSuccessful().observe(this, e_successful_unsuccessful_noStatement -> {
                try{
                    if(e_successful_unsuccessful_noStatement == E_Successful_Unsuccessful_NoStatement.SUCCESSFUL){
                        progressBarIndicator_setLecturers = true;
                        if(progressBarIndicator_isSemesterActive && progressBarIndicator_setDepartments){
                            progressBarLecturer.setVisibility(View.INVISIBLE);
                        }
                        if(adapter == null){
                            adapter = new RecyclerViewAdapter_Main_Admin_Lecturers(this, vmMainAdmin.getLecturerList());
                            recyclerViewMainAdminLecturers.setAdapter(adapter);
                        }
                        else{
                            adapter.setLecturerList(vmMainAdmin.getLecturerList());
                        }
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' vmMainAdmin.getSetLecturersSuccessful().observe method.");
                }
            });
            vmMainAdmin.getSetDepartmentsSuccessful().observe(this, e_successful_unsuccessful_noStatement -> {
                try{
                    if(e_successful_unsuccessful_noStatement == E_Successful_Unsuccessful_NoStatement.SUCCESSFUL){
                        progressBarIndicator_setDepartments = true;
                        if(progressBarIndicator_isSemesterActive && progressBarIndicator_setLecturers){
                            progressBarLecturer.setVisibility(View.INVISIBLE);
                        }
                        if(adapterFilter == null){
                            adapterFilter = new RecyclerViewAdapter_Filter_Department(this, vmMainAdmin.getDepartmentList());
                            recyclerViewMainAdminLecturersFilter.setAdapter(adapterFilter);
                        }
                        else {
                            adapterFilter.setDepartmentList(vmMainAdmin.getDepartmentList());
                        }
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' vmMainAdmin.getSetDepartmentsSuccessful().observe method.");
                }
            });
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onCreate method.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            resetWidgets();
            progressBarIndicator_isSemesterActive = false;
            progressBarIndicator_setLecturers = false;
            if(toastMessage != null){
                toastMessage.cancel();
            }
            fragmentUserAndSemester.setName("ADMIN");
            vmMainAdmin.onLoadSemestersRequested();
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onResume method.");
        }
    }

    private void resetWidgets(){
        try{
            progressBarLecturer.setVisibility(View.VISIBLE);
            buttonSearchId.setVisibility(View.VISIBLE);
            buttonCancelSearchId.setVisibility(View.INVISIBLE);
            buttonSearchName.setVisibility(View.VISIBLE);
            buttonCancelSearchName.setVisibility(View.INVISIBLE);
            buttonSearchSurname.setVisibility(View.VISIBLE);
            buttonCancelSearchSurname.setVisibility(View.INVISIBLE);
            buttonFilterDeptName.setVisibility(View.VISIBLE);
            buttonCancelFilterDeptName.setVisibility(View.INVISIBLE);
            textViewId.setVisibility(View.VISIBLE);
            editTextId.setText(null);
            editTextId.setVisibility(View.INVISIBLE);
            editTextId.clearFocus();
            textViewName.setVisibility(View.VISIBLE);
            editTextName.setText(null);
            editTextName.setVisibility(View.INVISIBLE);
            editTextName.clearFocus();
            textViewSurname.setVisibility(View.VISIBLE);
            editTextSurname.setText(null);
            editTextSurname.setVisibility(View.INVISIBLE);
            editTextSurname.clearFocus();

            idFilter = "";
            nameFilter = "";
            surnameFilter = "";
            deptFilter.clear();
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' resetWidgets method.");
        }
    }

    private void showToastMessage(int message){
        try{
            toastMessage = Toast.makeText(this, getResources().getString(message), Toast.LENGTH_SHORT);
            toastMessage.show();
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' showToastMessage method.");
        }
    }

    public void goToDepartments(MenuItem item) {
        try{
            Intent i = new Intent(getApplicationContext(), Activity_Main_Admin_Departments.class);
            startActivity(i);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' goToDeptAdmins method.");
        }
    }

    public void goToDeptAdmins(MenuItem item) {
        try{
            Intent i = new Intent(getApplicationContext(), Activity_Main_Admin_Dept_Admins.class);
            startActivity(i);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' goToLecturers method.");
        }
    }

    public void goToLecturers(MenuItem item) {
        // nothing to do
    }

    public void goToStudents(MenuItem item) {
        try{
            Intent i = new Intent(getApplicationContext(), Activity_Main_Admin_Students.class);
            startActivity(i);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' goToStudents method.");
        }
    }

    public void goToSemesters(MenuItem item) {
        try{
            Intent i = new Intent(getApplicationContext(), Activity_Main_Admin_Semesters.class);
            startActivity(i);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' goToSemesters method.");
        }
    }

    @Override
    public void onSemesterChanged(String selectedSemester, int position) {
        try{
            resetWidgets();
            Common_Variables_View.SELECTED_SEMESTER = selectedSemester;
            Common_Variables_View.SEMESTER_SPINNER_POSITION = position;
            vmMainAdmin.onSemesterActiveRequested(selectedSemester);
            vmMainAdmin.onLecturerListRequested(selectedSemester);
            vmMainAdmin.onDepartmentListRequested(selectedSemester);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onSemesterChanged method.");
        }
    }

    @Override
    public void onChangePasswordClicked() {
        try{
            editTextDialogChangePasswordOldPassword.setText(null);
            editTextDialogChangePasswordNewPassword.setText(null);
            editTextDialogChangePasswordNewPasswordConfirm.setText(null);
            editTextDialogChangePasswordOldPassword.clearFocus();
            editTextDialogChangePasswordNewPassword.clearFocus();
            editTextDialogChangePasswordNewPasswordConfirm.clearFocus();
            textViewDialogChangePassword.setVisibility(View.INVISIBLE);
            buttonDialogChangePasswordOK.setEnabled(false);
            alertDialogChangePassword.show();
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onChangePasswordClicked method.");
        }
    }

    @Override
    public void onLogoutClicked() {
        try{
            fragmentUserAndSemester.setSpinnerItem(0);
            vmLoginProcess.onLogoutRequested();
            Intent i = new Intent(getApplicationContext(), Activity_Login_Page.class);
            startActivity(i);
        }
        catch(Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onLogoutClicked method.");
        }
    }

    @Override
    public void setAndShowWarningOnDialogChangePassword(int warning, int visibility) {
        try{
            progressBarChangePassword.setVisibility(View.INVISIBLE);
            textViewDialogChangePassword.setText(warning);
            textViewDialogChangePassword.setVisibility(visibility);
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' setAndShowWarningOnDialogChangePassword method.");
        }
    }

    @Override
    public void onChangePasswordRequested(String oldPassword, String newPassword) {
        try{
            progressBarChangePassword.setVisibility(View.VISIBLE);
            vmLoginProcess.onChangePasswordRequested(oldPassword, newPassword);
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onChangePasswordRequested method.");
        }
    }

    @Override
    public void onDismissDialogRequested() {
        try{
            progressBarChangePassword.setVisibility(View.INVISIBLE);
            alertDialogChangePassword.dismiss();
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Activity_Main_Admin_Lecturers class' onDismissDialogRequested method.");
        }
    }
}

