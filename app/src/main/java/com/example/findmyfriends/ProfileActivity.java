package com.example.findmyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName,profileEmail,profilePassword;
    static TextView profileUsername;
    TextView titleName, titleUsername;
    Button editProfile;
    ImageView profileImg;
    String imageURL,nameUser,emailUser,usernameUser,passwordUser,userUsername;
    String nameFromDB,emailFromDB,usernameFromDB,passwordFromDB;
    Uri uri;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameUser = getIntent().getStringExtra("name");
        emailUser = getIntent().getStringExtra("email");
        usernameUser = getIntent().getStringExtra("username");
        passwordUser = getIntent().getStringExtra("password");
        imageURL = getIntent().getStringExtra("profileImage");

        reference = FirebaseDatabase.getInstance().getReference().child("users");

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        profileImg = findViewById(R.id.profileImg);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.profileMenu){
            Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.maps) {
            Intent intent = new Intent(ProfileActivity.this,MapsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.friends) {
            Intent intent = new Intent(ProfileActivity.this,FriendsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAllUserData(){
        reference.child(usernameUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    nameUser = snapshot.child("name").getValue().toString();
                    emailUser = snapshot.child("email").getValue().toString();
                    usernameUser = snapshot.child("username").getValue().toString();
                    passwordUser = snapshot.child("password").getValue().toString();
                    imageURL = snapshot.child("profileImage").getValue().toString();

                    Picasso.get().load(imageURL).into(profileImg);
                    titleName.setText(nameUser);
                    titleUsername.setText(usernameUser);
                    profileName.setText(nameUser);
                    profileEmail.setText(emailUser);
                    profileUsername.setText(usernameUser);
                    profilePassword.setText(passwordUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void passUserData(){
        userUsername = profileUsername.getText().toString().trim();

        //reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    nameFromDB = snapshot.child(userUsername).child("name").getValue().toString();
                    emailFromDB = snapshot.child(userUsername).child("email").getValue().toString();
                    usernameFromDB = snapshot.child(userUsername).child("username").getValue().toString();
                    passwordFromDB = snapshot.child(userUsername).child("password").getValue().toString();
                    //imageURL = snapshot.child(userUsername).child("profileImage").getValue().toString();

                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);

                    intent.putExtra("name", nameUser);
                    intent.putExtra("email", emailUser);
                    intent.putExtra("username", usernameUser);
                    intent.putExtra("password", passwordUser);

                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}