package com.example.findmyfriends;

import static com.example.findmyfriends.ProfileActivity.profileUsername;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FriendActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends,FriendMyViewHolder>adapter;
    DatabaseReference mRef;
    String userUsername,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        username = getIntent().getStringExtra("username");

        recyclerView = findViewById(R.id.myfriendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        userUsername = profileUsername.getText().toString().trim();
        LoadFriends(username);
    }
//.child(userUsername).child("friend")
    //            ref.child(userUsername).child("friend").child(username)
    private void LoadFriends(String username) {
        Query query = mRef.child(userUsername).child("friend").orderByChild("username").startAt(username).endAt(username+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull Friends model) {
                Picasso.get().load(model.getProfileImageUrl()).into(holder.profileImg);
                holder.username.setText(model.getUsername());
                holder.name.setText(model.getName());


            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend,parent,false);
                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

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
            Intent intent = new Intent(FriendActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.maps) {
            Intent intent = new Intent(FriendActivity.this,MapsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.friends) {
            Intent intent = new Intent(FriendActivity.this, FindFriendsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.myfriends) {
            Intent intent = new Intent(FriendActivity.this, FriendActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(FriendActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

