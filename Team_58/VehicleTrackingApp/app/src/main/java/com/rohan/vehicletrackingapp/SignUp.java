package com.rohan.vehicletrackingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

   private EditText musername;
    private EditText email_id;
    private EditText mpassword;
    private EditText mconfirmpassword;
    private Button mButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        musername=findViewById(R.id.eUsername);
        email_id=findViewById(R.id.eEmail);
        mpassword=findViewById(R.id.mpassword);
        mconfirmpassword=findViewById(R.id.mConfirmPassword);
        mAuth=FirebaseAuth.getInstance();
        mButton=findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPassword(email_id.getText().toString(),mpassword.getText().toString());
            }
        });



    }

    private void checkEmailPassword(String email, String password) {
        Log.d("Vehicle","email:"+email);
        Log.d("Vehicle","password:"+password);
        if(!email.contains("@"))
        {
            Log.d("Vehicle","in if email:"+email);
            Log.d("Vehicle","in if password:"+password);
            Toast.makeText(getApplicationContext(),"INVALID EMAIL",Toast.LENGTH_SHORT).show();
        }
        else{

            if(password.length()<=6)
            {
                Toast.makeText(getApplicationContext(),"Password should be atleast 6 characters",Toast.LENGTH_SHORT).show();
            }

            else{
                registerUser();
            }

        }

    }

    private void registerUser() {
        final ProgressDialog dialog = ProgressDialog.show(SignUp.this, "",
                "Loading. Please wait...", true);
        mAuth.createUserWithEmailAndPassword(email_id.toString(),mpassword.toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Log.d("vehicle", "succesful authentication");
                            Toast.makeText(getApplicationContext(), "Succesful registration", Toast.LENGTH_SHORT)
                                    .show();

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UpdateUI(firebaseUser);
                        } else {
                            dialog.dismiss();
                            Log.d("vehicle", "UNsuccesful registration");
                            Toast.makeText(getApplicationContext(), "Unsuccesful registration", Toast.LENGTH_SHORT)
                                    .show();
                            UpdateUI(null);
 }
                    }
                });

    }

    private void UpdateUI(Object o) {
    }
}
