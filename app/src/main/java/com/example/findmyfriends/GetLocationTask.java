package com.example.findmyfriends;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.ExecutionException;

public class GetLocationTask extends AsyncTask<Void, Void, Location> {

    private final Context mContext;
    private final FusedLocationProviderClient mFusedClient;
    private final OnTaskCompleteListener mListener;

    public GetLocationTask(Context context, FusedLocationProviderClient fusedClient, OnTaskCompleteListener listener) {
        mContext = context;
        mFusedClient = fusedClient;
        mListener = listener;
    }

    @Override
    protected Location doInBackground(Void... voids) {
        try {
            // Check for location permissions
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //return null;
            }

            // Get last known location from fused location provider client
            Task<Location> task = mFusedClient.getLastLocation();

            return Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Location location) {
        super.onPostExecute(location);
        mListener.onTaskComplete(location);
    }

    public interface OnTaskCompleteListener {
        void onTaskComplete(Location location);
    }
}