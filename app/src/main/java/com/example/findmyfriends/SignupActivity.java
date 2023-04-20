package com.example.findmyfriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    ImageView profileImg;
    String imageURL;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog mLoadingBAr;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBAr = new ProgressDialog(this);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                //String profileImg = imageURL.getText().toString();
                //imageURL=getIntent().seti("profileImage");
                HelperClass helperClass = new HelperClass(name, email, username, password, imageURL);
                helperClass.setProfileImage("https://firebasestorage.googleapis.com/v0/b/findmyfriends-6cea2.appspot.com/o/users%2Fclara%2F232519087?alt=media&token=5968391c-5330-40f2-8b53-a6cd58c0e933");
                reference.child(username).setValue(helperClass);

                if(name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else{
//                    if(false==(email.contains(" @gmail"))||false==(email.contains(" @yahoo"))){
//                        showError(signupEmail,"Email is not Valid!");
//                    }
                    if (password.length()<5) {
                        showError(signupPassword, "Password must be greated then 5 letters!");
                    }
                    else{
//                        mLoadingBAr.setTitle("SignUp");
//                        mLoadingBAr.setMessage("Please Wait, While your Credentials");
//                        mLoadingBAr.setCanceledOnTouchOutside(false);
//                        mLoadingBAr.show();
//                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    //mLoadingBAr.dismiss();
//                                }
//                            }
//                        });

                    }

                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showError(EditText field, String text){
        field.setError(text);
        field.requestFocus();
    }
}