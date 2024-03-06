 package com.example.orderfoodapplication;

import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

 public class LogInActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() & !validatePin()) {

                }else {
                    checkUser();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                }

            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(LogInActivity.this, SignupActivity.class);
                 startActivity(intent);

            }
        });
    }

    public boolean validateUsername() {
String val = loginUsername.getText().toString();

  if (val.isEmpty()){
      loginUsername.setError("UserName can't be empty");
      return false;
} else {
      loginUsername.setError(null);
      return true;
  }

    }
     public boolean validatePin() {
         String val = loginPassword.getText().toString();

         if (val.isEmpty()) {
             loginPassword.setError("Password can't be empty");
             return false;
         } else {
             loginPassword.setError(null);
             return true;
         }
     }
     public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
         Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

         checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (snapshot.exists()){
                     loginUsername.setError(null);
                     String passwordFromDB =snapshot.child(userUsername).child("password").getValue(String.class);
                     if (Objects.equals(passwordFromDB, userPassword)){

                         Intent intent =new Intent(LogInActivity.this, MainActivity.class);
                         startActivity(intent);
                     }else{
                         loginPassword.setError("Invalid Credentials");
                         loginPassword.requestFocus();
                     }


                 }else {
                     loginUsername.setError("User doesn't exist");
                     loginUsername.requestFocus();
                 }



             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }
 }

