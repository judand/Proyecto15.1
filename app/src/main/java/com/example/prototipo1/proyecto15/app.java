package com.example.prototipo1.proyecto15;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        //conecta conparse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if desired
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com/")
                .build()
        );
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "829404824092");
        installation.saveInBackground();
    }


}
