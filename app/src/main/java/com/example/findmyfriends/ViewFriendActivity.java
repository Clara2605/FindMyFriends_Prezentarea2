package com.example.findmyfriends;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewFriendActivity extends AppCompatActivity {

    DatabaseReference userRef,ref,requestRef,friendRef;
    FirebaseAuth auth;
    FirebaseUser user;
    String profileImageUrl,username,name;
    ImageView profileImg;
    TextView friendUsername, friendName;
    Button btnSend,btnDecline;
    String currentState = "nothing_happen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_iew_friend);
        
        username = getIntent().getStringExtra("username");
        Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
        requestRef = FirebaseDatabase.getInstance().getReference().child("requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("friends");
        ref = FirebaseDatabase.getInstance().getReference("users/"+ username);

        profileImg = findViewById(R.id.profileImg);
        friendName = findViewById(R.id.friendName);
        friendUsername = findViewById(R.id.friendUsername);
        btnSend = findViewById(R.id.sendFriendRequest);
        btnDecline = findViewById(R.id.declineFriendRequest);

        LoadUser();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(username);
            }
        });
    }

    private void PerformAction(String username) {
        if(currentState.equals("nothing_happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","pending");
            requestRef.child(String.valueOf(ref)).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You have sent Friend Request", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        currentState = "I_sent_pending";
                        btnSend.setText("Cancel Friend Request");
                    }else{
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        if(currentState.equals("I_sent_pending") || currentState.equals("I_sent_decline")){
            requestRef.child(String.valueOf(ref)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You have cancelled Friend Request", Toast.LENGTH_SHORT).show();
                        currentState = "nothing_happen";
                        btnSend.setText("Send Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(ViewFriendActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        if(currentState.equals("he_sent_pending")){
            requestRef.child(String.valueOf(userRef)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        HashMap hashMap = new HashMap();
                        hashMap.put("status","friend");
                        hashMap.put("username", username);
                        hashMap.put("profileImage", profileImageUrl);
                        friendRef.child(String.valueOf(userRef)).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    friendRef.child(String.valueOf(userRef)).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                            currentState="friend";
                                            btnSend.setText("Send Current Location");
                                            btnDecline.setText("Unfriend");
                                            btnDecline.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                            }
                        });

                    }

                }
            });
        }
        if(currentState.equals("friend")){
            //
        }

    }

    private void LoadUser() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profileImageUrl = snapshot.child("profileImage").getValue().toString();
                    username = snapshot.child("username").getValue().toString();
                    name = snapshot.child("name").getValue().toString();

                    Picasso.get().load(profileImageUrl).into(profileImg);
                    friendName.setText(name);
                    friendUsername.setText(username);
                }
                else{
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}