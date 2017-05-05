package com.example.motaher.movietune.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.motaher.movietune.global.CommonData;

/**
 * Created by motaher on 5/4/2017.
 */

public class PreferenceHelper {

    private Context context;
    public PreferenceHelper(){

    }

    public PreferenceHelper(Context context){
        this.context = context;
    }


       public void saveInfo(String key, String value){

        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(CommonData.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
        }

}