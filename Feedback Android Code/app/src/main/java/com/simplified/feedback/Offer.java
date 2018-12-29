package com.simplified.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Offer extends Activity {

    TextView tv1;
    int d=5+(int)(Math.random()*7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        tv1=findViewById(R.id.tv);

        tv1.setText("You recieved a discount of "+String.valueOf(d)+"%!!! Please show this page at the counter at the time of billing!! Thank you,Keep Shopping :)");


    }
}
