package com.example.doctor;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public SharedPreferencesUtils(Context context) {
        this.context = context;
        try {
            sh = this.context.getSharedPreferences("DATASH", Context.MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLoginFlag(boolean flag) {
        SharedPreferences.Editor editor = sh.edit();
        editor.putBoolean("loginFlag", flag);
        editor.commit();
    }

    public boolean getLoginFlag() {
        if (sh.contains("loginFlag"))
            return sh.getBoolean("loginFlag", false);
        else return false;
    }

    public Context context;
    SharedPreferences sh;



}
