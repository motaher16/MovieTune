package com.example.motaher.movietune.webService;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import com.example.motaher.movietune.model.Genre;
import com.example.motaher.movietune.model.ProductionCompany;
import com.example.motaher.movietune.model.ProductionCountry;
import com.example.motaher.movietune.model.SpokenLanguage;

import java.util.List;

/**
 * Created by motaher on 5/4/2017.
 */

public class CommonService {
    Context context;
    Activity mActivity;

    public CommonService(){

    }

    public CommonService(Context context){
        this.context = context;
        mActivity = (Activity) context;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void popupAlertDialog(String message){

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setCancelable(true);

        dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    public String dateSeparator(String date){

        if(date != null){
            String[] dateSeparator = date.split("-");
            return dateSeparator[0];
        }
        return "";
    }

    public String getMergedNames(List<Genre> genres, List<ProductionCompany> productionCompanies, List<ProductionCountry> productionCountries, List<SpokenLanguage> spokenLanguages){
        String names = "";

        if (genres != null){
            for(int i = 0; i < genres.size(); i++){
                if (i == 0){
                    names = names + genres.get(i).getName();
                }
                else{
                    names = names + ", " + genres.get(i).getName();
                }
            }
        }

        if (productionCompanies != null){
            for(int i = 0; i < productionCompanies.size(); i++){
                if (i == 0){
                    names = names + productionCompanies.get(i).getName();
                }
                else{
                    names = names + ", " + productionCompanies.get(i).getName();
                }
            }
        }

        if (productionCountries != null){
            for(int i = 0; i < productionCountries.size(); i++){
                if (i == 0){
                    names = names + productionCountries.get(i).getName();
                }
                else{
                    names = names + ", " + productionCountries.get(i).getName();
                }
            }
        }

        if (spokenLanguages != null){
            for(int i = 0; i < spokenLanguages.size(); i++){
                if (i == 0){
                    names = names + spokenLanguages.get(i).getName();
                }
                else{
                    names = names + ", " + spokenLanguages.get(i).getName();
                }
            }
        }

        return names;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int convertDpToPx(int dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)((dp * displayMetrics.density) + 0.5);
    }
}


