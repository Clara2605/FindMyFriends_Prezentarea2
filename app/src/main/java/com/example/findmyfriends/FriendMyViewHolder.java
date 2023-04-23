package com.example.findmyfriends;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {

    ImageView profileImg;
    TextView username, name;
    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImg = itemView.findViewById(R.id.profileImg);
        username = itemView.findViewById(R.id.profileUsername);
        name = itemView.findViewById(R.id.profileName);
    }
}
