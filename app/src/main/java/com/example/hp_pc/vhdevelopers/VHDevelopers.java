package com.example.hp_pc.vhdevelopers;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by HP-PC on 11/14/2016.
 */

public class VHDevelopers extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//For offline picasso capablities
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
