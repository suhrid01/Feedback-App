package com.simplified.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    EditText etname,etemail,etphone,etbirth,etanniv,etsuggest,etbill;
    RadioButton marstat,know,experience,recommend,rb1,rb2;
    RatingBar rtcol,rtserv,rtamb,rtvfm,rtoverall,rtstaff;
    Button bt1;
    RadioGroup mar,recom,exp,source;
    ProgressDialog pDialog;
    TextView tv1,tv2,tv3,t1,t2,t3,t4,t5,t6;
    CheckBox cb1;
    int subs;
    JSONArray data1;
    String id,comp_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etname=findViewById(R.id.et1);
        etemail=findViewById(R.id.et2);
        etphone=findViewById(R.id.et3);
        etbirth=findViewById(R.id.et4);
        etanniv=findViewById(R.id.et5);
        etsuggest=findViewById(R.id.et6);
        etbill=findViewById(R.id.et7);

        cb1=findViewById(R.id.cb1);

        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        tv1=findViewById(R.id.tv123);

        tv2=findViewById(R.id.tv1);
        tv3=findViewById(R.id.tv2);

        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        rtcol=findViewById(R.id.rt1);
        rtstaff=findViewById(R.id.rt2);
        rtserv=findViewById(R.id.rt3);
        rtamb=findViewById(R.id.rt4);
        rtvfm=findViewById(R.id.rt5);
        rtoverall=findViewById(R.id.rt6);

        bt1=findViewById(R.id.bt1);

        mar=findViewById(R.id.mar);
        recom=findViewById(R.id.reccomend);
        source=findViewById(R.id.source);
        exp=findViewById(R.id.experience);

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        id = bundle.getString("ID");
        comp_type = bundle.getString("COMP_TYPE");

        questions();


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int m = mar.getCheckedRadioButtonId();
                marstat = findViewById(m);
                final int r = recom.getCheckedRadioButtonId();
                recommend = findViewById(r);
                final int s = source.getCheckedRadioButtonId();
                know = findViewById(s);
                final int e = exp.getCheckedRadioButtonId();
                experience = findViewById(e);

                if (cb1.isChecked()) {
                    subs=1;
                }
                else{
                    subs=0;
                }

                String tag_string_req = "req_login";
                pDialog.setMessage("Submitting Form...");
                showDialog();
                String url = "http://smspl.in/feedback_app/feedbackform.php";
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "[" + response + "]");
                        Log.d(TAG, " Response: " + response);
                        hideDialog();
                        sendEmail();

                        Intent i=new Intent(MainActivity.this,Offer.class);
                        startActivity(i);
                        //finish();
                        Toast.makeText(getApplicationContext(), "Feedback form Submitted Successfully", Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, " Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Wrong Credentials!", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<>();
                        params.put("name", etname.getText().toString());
                        params.put("email", etemail.getText().toString());
                        params.put("phone_no", etphone.getText().toString());
                        params.put("dob", etbirth.getText().toString());
                        params.put("aniversary", etanniv.getText().toString());
                        params.put("suggest", etsuggest.getText().toString());
                        params.put("bill", etbill.getText().toString());
                        params.put("collection", String.valueOf(rtcol.getRating()));
                        params.put("service", String.valueOf(rtserv.getRating()));
                        params.put("ambience", String.valueOf(rtamb.getRating()));
                        params.put("valueformoney", String.valueOf(rtvfm.getRating()));
                        params.put("staff", String.valueOf(rtstaff.getRating()));
                        params.put("overall", String.valueOf(rtoverall.getRating()));
                        params.put("mar", String.valueOf(marstat.getText()));
                        params.put("recom", String.valueOf(recommend.getText()));
                        params.put("source", String.valueOf(know.getText()));
                        params.put("exp", String.valueOf(experience.getText()));
                        params.put("subscribe",String.valueOf(subs));
                        params.put("company_id",id);
                        //params.put("token", SharedPreference.getInstance(getApplicationContext()).getToken());
                        return params;
                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb1.isChecked()){
                    tv1.setVisibility(View.VISIBLE);
                    etanniv.setVisibility(View.VISIBLE);
                }
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb2.isChecked()){
                    tv1.setVisibility(View.INVISIBLE);
                    etanniv.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void questions(){

        String tag_string_req = "req_login";
       // pDialog.setMessage("Fetching Feedbacks...");
        //showDialog();
        String url = "http://suhrid1theinceptor.000webhostapp.com/userlogin/company_type_details.php?type="+comp_type;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
                //hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);

                    data1 = jObj.getJSONArray("respond");
                    for (int i = 0; i < data1.length(); i++) {
                        try {
                            JSONObject json = data1.getJSONObject(i);
                           // String name = json.getString("name");
                            t1.setText(json.getString("one"));
                            t2.setText(json.getString("two"));
                            t3.setText(json.getString("three"));
                            t4.setText(json.getString("four"));
                            t5.setText(json.getString("five"));
                            t6.setText(json.getString("six"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"Feedback form Submitted Successfully", Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Wrong Credentials!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }); /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }*/


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendEmail() {
        if ((rtcol.getRating() + rtserv.getRating() + rtstaff.getRating() + rtamb.getRating() + rtoverall.getRating() + rtvfm.getRating() <= 12)) {

            SendMail sm = new SendMail(this,

                    "sskjha2016@gmail.com",

                    "Critical Feedback Alert",

                    "Name of customer:    "+etname.getText().toString()
                            +"\nBill Amount:           "+etbill.getText().toString()
                            +"\nContact:                 "+etphone.getText().toString()
                            +"\nCollection:             "+ String.valueOf(rtcol.getRating())
                            +"\nService:                  "+ String.valueOf(rtserv.getRating())
                            +"\nAmbience:             "+ String.valueOf(rtamb.getRating())
                            +"\nValue for money:  "+ String.valueOf(rtvfm.getRating())
                            +"\nStaff:                      "+ String.valueOf(rtstaff.getRating())
                            +"\nOverall:                   "+ String.valueOf(rtoverall.getRating()));

            //Executing sendmail to send email
            sm.execute();

           // Toast.makeText(getApplicationContext(), "Feedback form Submitted Successfully", Toast.LENGTH_LONG).show();
        }


    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
