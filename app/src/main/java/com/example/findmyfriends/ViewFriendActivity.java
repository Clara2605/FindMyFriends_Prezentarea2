package com.example.findmyfriends;

import static com.example.findmyfriends.ProfileActivity.profileUsername;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
    double Longitude;
    double Latitude;
    String profileImageUrl,username,name,userUsername,currentLocation;
    String myprofileImageUrl,myusername,myname,mycurrentLocation;
    ImageView profileImg;
    TextView friendUsername, friendName;
    Button btnSend,btnDecline;
    String currentState = "nothing_happen";
    LocationHelper helper;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    Location location ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_iew_friend);

        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        profileImageUrl = getIntent().getStringExtra("profileImage");

//        currentLocation = getIntent().getStringExtra("Current Location");
//        helper = new LocationHelper(location.getLongitude(),location.getLatitude());
//        Longitude = Double.parseDouble(getIntent().getStringExtra("Current Location/longitude"));
//        Latitude = Double.parseDouble(getIntent().getStringExtra("Current Location/latitude"));


        Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
        ref = FirebaseDatabase.getInstance().getReference().child("users");

        userUsername = profileUsername.getText().toString().trim();
       // userName = profileName.getText().toString().trim();

        //Query checkUserDatabase = ref.orderByChild("username").equalTo(userUsername);

        profileImg = findViewById(R.id.profileImg);
        friendName = findViewById(R.id.friendName);
        friendUsername = findViewById(R.id.friendUsername);
        btnSend = findViewById(R.id.sendFriendRequest);
        btnDecline = findViewById(R.id.declineFriendRequest);
        createNotificationChannel();
        LoadUser();
        LoadMyProfile();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(username);
            }
        });

        CheckUserExistance(username);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Unfirend(username);
            }
        });
    }


    private void Unfirend(String username) {
        if(currentState.equals("friend")){
            userRef.child("friend/"+userUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            ref.child(userUsername).child("friend").child(username).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You arr Unfriend", Toast.LENGTH_SHORT).show();
                        currentState = "nothing_happen";
                        btnSend.setText("Send Friend Request");
                        btnDecline.setVisibility(View.GONE);
//                        userRef.child("friend/"+userUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        ref.child(userUsername).child("friend").child(username).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ViewFriendActivity.this, "You arr Unfriend", Toast.LENGTH_SHORT).show();
                                    currentState = "nothing_happen";
                                    btnSend.setText("Send Friend Request");
                                    btnDecline.setVisibility(View.GONE);
                                }

                            }
                        });
                    }
                }
            });
        }
        if(currentState.equals("he_sent_pending")){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status","decline");
            userRef.child("friend/"+userUsername).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendActivity.this, "You have Decline Friend Request", Toast.LENGTH_SHORT).show();
                        currentState = "I_sent_decline";
                        btnSend.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);

                        ref.child(userUsername).child("friend").child(username).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ViewFriendActivity.this, "Your Friend Declined the friend request", Toast.LENGTH_SHORT).show();
                                    currentState = "he_sent_decline";
                                    btnSend.setVisibility(View.GONE);
                                    btnDecline.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });


        }
    }

    public void sendNotification()
    {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
//        setNotificationButtonState(false, true, true);
//
//        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
//        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
//                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
//        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);
    }
    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder()
    {
 //      Intent notificationIntent = new Intent(this, FindFriendsActivity.class);
        Intent notificationIntent = new Intent(this, ViewFriendActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You received a new friend request!")
                .setContentText(username+" wants you to be friends. Respond to the friend request.")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    private void CheckUserExistance(String username) {
        userRef.child("friend/"+userUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    currentState="friend";
                    btnSend.setText("Send Current Location");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(userUsername).child("friend").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    currentState="friend";
                    btnSend.setText("Send Current Location");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.child("request/"+userUsername).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentState = "I_sent_pending";
                        btnSend.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        currentState = "I_sent_decline";
                        btnSend.setText("Cancel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(userUsername).child("request").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        sendNotification();
                        currentState = "he_sent_pending";
                        btnSend.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend Request");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(currentState.equals("nothing_happen")){
            currentState = "nothing_happen";
            btnSend.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String username) {
        if(currentState.equals("nothing_happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","pending");
            userRef.child("request/"+userUsername).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
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
            userRef.child("request/"+userUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
            userRef.child("request/"+userUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        HashMap hashMap = new HashMap();
                        hashMap.put("status","friend");
                        hashMap.put("username", username);
                        hashMap.put("profileImage", profileImageUrl);
                        hashMap.put("name", name);
                        hashMap.put("Current Location", currentLocation);
//                        hashMap.put("latitude", Latitude);
//                        hashMap.put("longitude", Longitude);

                        HashMap hashMap1 = new HashMap();
                        hashMap1.put("status","friend");
                        hashMap1.put("username", myusername);
                        hashMap1.put("profileImage", myprofileImageUrl);
                        hashMap1.put("name", myname);
                        hashMap1.put("Current Location", mycurrentLocation);

                        ref.child(userUsername).child("friend").child(username).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {

                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful() ){

//
//                                    hashMap.put("Current Location", helper);
//                                    hashMap.put("latitude", Latitude);
//                                    hashMap.put("longitude", Longitude);

                                    userRef.child("friend/"+userUsername).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {

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

            //////////////////////////////////////////////


        }
        if(currentState.equals("he_sent_decline")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","decline");
            ref.child(userUsername).child("friend").child(username).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task) {
                    Toast.makeText(ViewFriendActivity.this, "Your Friend decline friend request", Toast.LENGTH_SHORT).show();
                    currentState="friend";
                    btnSend.setText("Send Friend Request");
                    btnDecline.setVisibility(View.GONE);
                }
            });
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
                    currentLocation = snapshot.child("Current Location").getValue().toString();

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

    private void LoadMyProfile() {
        ref.child(userUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myprofileImageUrl = snapshot.child("profileImage").getValue().toString();
                    myusername = snapshot.child("username").getValue().toString();
                    myname = snapshot.child("name").getValue().toString();
                    mycurrentLocation = snapshot.child("Current Location").getValue().toString();
                } else {
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}