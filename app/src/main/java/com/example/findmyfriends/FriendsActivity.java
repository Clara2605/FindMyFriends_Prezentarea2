package com.example.findmyfriends;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FriendsActivity extends AppCompatActivity {

    FirebaseRecyclerOptions<Users> options;
    FirebaseRecyclerAdapter<Users,FriendsViewHolder> adapter;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;

    RecyclerView recyclerView;

//    private RecyclerView friend_list_RV;
//
//    private DatabaseReference friendsDatabaseReference;
//    private DatabaseReference userDatabaseReference;
//    private FirebaseAuth mAuth;
//
//    String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = findViewById(R.id.friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUserRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        LoadUsers("");
//
//        mAuth = FirebaseAuth.getInstance();
//        current_user_id = mAuth.getCurrentUser().getUid();

//        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends").child(current_user_id);
//        friendsDatabaseReference.keepSynced(true); // for offline
//
//        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
//        userDatabaseReference.keepSynced(true); // for offline


//        // Setup recycler view
//        friend_list_RV = findViewById(R.id.friendList);
//        friend_list_RV.setHasFixedSize(true);
//        friend_list_RV.setLayoutManager(new LinearLayoutManager(this));

       // showPeopleList();
    }

    private void LoadUsers(String strUser) {
        Query query = mUserRef.orderByChild("username").startAt(strUser).endAt(strUser+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull Users model) {
                Picasso.get().load(model.getProfileImage()).into(holder.profileImg);
                holder.username.setText(model.getUsername());
                holder.name.setText(model.getName());

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friends,parent,false);
                return new FriendsViewHolder(view);
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
            Intent intent = new Intent(FriendsActivity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.maps) {
            Intent intent = new Intent(FriendsActivity.this,MapsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.friends) {
            Intent intent = new Intent(FriendsActivity.this,FriendsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(FriendsActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}