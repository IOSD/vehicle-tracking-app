package com.rohan.vehicletrackingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button Signup;
    private Button Login;
    private EditText email;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Signup = findViewById(R.id.buttonsignup);
        Login = findViewById(R.id.buttonlogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                finish();
                startActivity(intent);

            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
                       @Override
            public void onClick(View v) {
                           attemptlogin();


                       }

        });


    }

    private void attemptlogin() {
        String emailId = email.getText().toString();
        String password1 = password.getText().toString();
        if(checkEmailPassword(emailId,password1)) {
            Login(emailId, password1);
        }

    }


    public boolean checkEmailPassword(String Email, String Password) {
        Log.d("Vehicle","email:"+email);
        Log.d("Vehicle","password:"+password);
        email.setError(null);
        password.setError(null);
        if(!Email.contains("@"))
        {
            email.requestFocus();
            email.setError("INVALID EMAIL");
        }
        else{

            if(Password.length()<=6)
            {
                password.requestFocus();
                password.setError("Incorrect Pasword");
            }
            else{
            return true;
            }

        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UpdateUI(currentUser);

    }

    private void UpdateUI(FirebaseUser currentUser) {
    }


    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("vehicle", "succesful sign in");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UpdateUI(firebaseUser);
                            Intent intent = new Intent(MainActivity.this, SpeedLimit.class);
                            finish();
                            startActivity(intent);
                            //AccesingUserInfo();
                        } else {
                            Log.d("vehicle", "UNsuccesful sign in");
                            Toast.makeText(getApplicationContext(), "Unsuccesful Sign in", Toast.LENGTH_SHORT)
                                    .show();
                            UpdateUI(null);


                        }
                    }
                });

    }

//    private void AccesingUserInfo()
//    {
//        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser!=null)
//        {
//            String name = firebaseUser.getDisplayName();
//            String email = firebaseUser.getEmail();
//            Uri photoUrl = firebaseUser.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = firebaseUser.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = firebaseUser.getUid();
//       }
//    }

}