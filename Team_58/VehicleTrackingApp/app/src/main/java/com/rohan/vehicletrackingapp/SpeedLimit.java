package com.rohan.vehicletrackingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SpeedLimit extends AppCompatActivity {

    EditText speedlimit;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed_limit);

        speedlimit=findViewById(R.id.SpeedLimit);
        mButton=findViewById(R.id.buttongo);





        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(speedlimit.getText().toString().equals(""))
                {
                    speedlimit.setError("Please Enter some Speed Limit to Proced");
                                        return;

                }

                if( Integer.parseInt(speedlimit.getText().toString())<=10
                        || Integer.parseInt(speedlimit.getText().toString())>=100)
                {

                    speedlimit.setError("Speed Limit must be in Specified Range");
                    return;

                }

                Intent intent = new Intent(SpeedLimit.this, MapsActivity.class);
                intent.putExtra("Speed",speedlimit.getText().toString());
                finish();
                startActivity(intent);

            }
        });




    }
}
