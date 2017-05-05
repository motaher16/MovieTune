package com.example.motaher.movietune.webService;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


/**
 * Created by motaher on 5/4/2017.
 */

    public class ApiCommunicator {

        Context context;
        private ApiResponse apiResponse;
        ProgressDialog mProgress;
        CommonService commonService;

        public ApiCommunicator(Context context, ApiResponse apiResponse){
            this.context = context;
            this.apiResponse = apiResponse;
            commonService = new CommonService(context);
        }

        public ApiCommunicator(Context context){
            this.context = context;
            apiResponse = (ApiResponse) context;
            commonService = new CommonService(context);
        }

        public void getDataByGET(String url, final int methodTag , boolean isLoaderCancelable){

            if(mProgress == null){
                showLoader(isLoaderCancelable);
            }

            AndroidNetworking.get(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if(mProgress != null && mProgress.isShowing()){
                                mProgress.dismiss();
                                mProgress = null;
                            }
                            apiResponse.taskCompleted(response.toString(), methodTag);
                        }
                        @Override
                        public void onError(ANError error) {

                            if(mProgress != null && mProgress.isShowing()){
                                mProgress.dismiss();
                                mProgress = null;
                            }
                            String msg = "Something Wrong!";
                            commonService.popupAlertDialog(msg);
                        }
                    });
        }

        public void showLoader(boolean isCancelable){
            mProgress = new ProgressDialog(context);
            mProgress.setMessage("Please wait...");
            if(!isCancelable){
                mProgress.setCancelable(false);
            }
            else{
                mProgress.setCancelable(true);
            }
            mProgress.show();
        }
    }
