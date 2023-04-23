package com.example.findmyfriends;

import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsViewHolder extends RecyclerView.ViewHolder {

    Location currentLocation;
    double Longitude;
    double Latitude;
    ImageView profileImg;
    TextView username,name;
    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImg = itemView.findViewById(R.id.profileImg);
        username = itemView.findViewById(R.id.profileUsername);
        name = itemView.findViewById(R.id.profileName);
//        LocationHelper helper = new LocationHelper(currentLocation.getLongitude(),currentLocation.getLatitude());
    }
}
