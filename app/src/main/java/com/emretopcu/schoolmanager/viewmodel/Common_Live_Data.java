package com.emretopcu.schoolmanager.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Change_Password_Auth_Successful;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Change_Password_Successful;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Create_New_User_Successful;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Login_Successful;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Person_Type;
import com.emretopcu.schoolmanager.viewmodel.enums.loginProcess.E_Relogin_Main_Admin_Successful;

public class Common_Live_Data {

    private static Common_Live_Data INSTANCE;

    private MutableLiveData<E_Create_New_User_Successful> createNewUserSuccessful;
    private MutableLiveData<E_Login_Successful> loginSuccessful;
    private MutableLiveData<E_Relogin_Main_Admin_Successful> reloginMainAdminSuccessful;
    private MutableLiveData<E_Change_Password_Successful> changePasswordSuccessful;
    private MutableLiveData<E_Change_Password_Auth_Successful> changePasswordAuthSuccessful;
    private E_Person_Type personType;


    private Common_Live_Data(){
        try{
            createNewUserSuccessful = new MutableLiveData<>();
            createNewUserSuccessful.setValue(E_Create_New_User_Successful.NO_STATEMENT);
            loginSuccessful = new MutableLiveData<>();
            loginSuccessful.setValue(E_Login_Successful.NO_STATEMENT);
            reloginMainAdminSuccessful = new MutableLiveData<>();
            reloginMainAdminSuccessful.setValue(E_Relogin_Main_Admin_Successful.SUCCESSFUL);
            changePasswordSuccessful = new MutableLiveData<>();
            changePasswordSuccessful.setValue(E_Change_Password_Successful.NO_STATEMENT);
            changePasswordAuthSuccessful = new MutableLiveData<>();
            changePasswordAuthSuccessful.setValue(E_Change_Password_Auth_Successful.NO_STATEMENT);
            personType = E_Person_Type.NO_STATEMENT;
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Common_Live_Data class' constructor method.");
        }
    }

    public static Common_Live_Data getInstance(){
        try{
            if(INSTANCE == null){
                INSTANCE = new Common_Live_Data();
            }
            return INSTANCE;
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Common_Live_Data class' getInstance method.");
            return null;
        }
    }

    public MutableLiveData<E_Create_New_User_Successful> getCreateNewUserSuccessful() {
        return createNewUserSuccessful;
    }

    public MutableLiveData<E_Login_Successful> getLoginSuccessful() {
        return loginSuccessful;
    }

    public MutableLiveData<E_Relogin_Main_Admin_Successful> getReloginMainAdminSuccessful() {
        return reloginMainAdminSuccessful;
    }

    public MutableLiveData<E_Change_Password_Successful> getChangePasswordSuccessful() {
        return changePasswordSuccessful;
    }

    public MutableLiveData<E_Change_Password_Auth_Successful> getChangePasswordAuthSuccessful() {
        return changePasswordAuthSuccessful;
    }

    public E_Person_Type getPersonType() {
        return personType;
    }
}
