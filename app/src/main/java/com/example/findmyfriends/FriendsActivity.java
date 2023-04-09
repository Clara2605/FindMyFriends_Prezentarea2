package com.example.findmyfriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
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