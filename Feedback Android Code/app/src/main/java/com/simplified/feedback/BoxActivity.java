package com.simplified.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class BoxActivity extends Activity {
    Button b1,b2,b3,b4,b5;
    String id,admin;
    public static String comp_type;
    SessionManager sessionManager;

    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_activity);

        b1=findViewById(R.id.btf);
        b2=findViewById(R.id.btv);
        b3=findViewById(R.id.atf);
        b4=findViewById(R.id.rtv);
        b5=findViewById(R.id.ltv);

        Bundle bundle = getIntent().getExtras();
        sessionManager=new SessionManager(getApplicationContext());

        //Extract the data…
        id = bundle.getString("ID");
        admin = bundle.getString("IS_ADMIN");
        comp_type = bundle.getString("COMP_TYPE");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager


        HashMap<String, String> user = db.getUserDetails();

        if (admin.equals("1")){

            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b1.setEnabled(false);
            b2.setEnabled(false);
            b1.setClickable(false);
            b2.setClickable(false);
        }
        else{
            b3.setVisibility(View.INVISIBLE);
            b4.setVisibility(View.INVISIBLE);
            b3.setEnabled(false);
            b4.setEnabled(false);
            b3.setClickable(false);
            b4.setClickable(false);
        }


    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(BoxActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            //Add your data from getFactualResults method to bundle
            bundle.putString("ID",id);
            bundle.putString("COMP_TYPE",comp_type);
            i.putExtras(bundle);
            startActivity(i);
        }
    });



    b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(BoxActivity.this, ViewFeedback.class);
            Bundle bundle = new Bundle();
            //Add your data from getFactualResults method to bundle
            bundle.putString("ID",id);
            bundle.putString("COMP_TYPE",comp_type);
            i.putExtras(bundle);
            startActivity(i);
        }
    });


    b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BoxActivity.this, RegisterActivity.class);

                startActivity(i);
            }
        });

    /*b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BoxActivity.this, MainActivity.class);
                startActivity(i);
            }
        });*/


        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteUsers();
                sessionManager.setLogin(false);
                Intent i = new Intent(BoxActivity.this, LoginActivity.class);

                startActivity(i);
                finish();
            }
        });


    }
}
