package com.simplified.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class FeedbackDetails extends Activity {

    EditText etname,etemail,etphone,etbirth,etanniv,etsuggest,etbill,mar,recom,exp,source;

    RatingBar rtcol,rtserv,rtamb,rtvfm,rtoverall,rtstaff;
    Button bt1;
    TextView tv1,tv2,t1,t2,t3,t4,t5,t6;
    ProgressDialog pDialog;
    int id;
    String type=BoxActivity.comp_type;
    JSONArray data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        etname = findViewById(R.id.et1);
        etemail = findViewById(R.id.et2);
        etphone = findViewById(R.id.et3);
        etbirth = findViewById(R.id.et4);
        etanniv = findViewById(R.id.et5);
        etsuggest = findViewById(R.id.et6);
        etbill=findViewById(R.id.et7);

        tv1=findViewById(R.id.tv123);

        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        rtcol = findViewById(R.id.rt1);
        rtstaff = findViewById(R.id.rt2);
        rtserv = findViewById(R.id.rt3);
        rtamb = findViewById(R.id.rt4);
        rtvfm = findViewById(R.id.rt5);
        rtoverall = findViewById(R.id.rt6);


        mar = findViewById(R.id.mar);
        recom = findViewById(R.id.reccomend);
        source = findViewById(R.id.source);
        exp = findViewById(R.id.experience);

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        id = bundle.getInt("ID");
       // type = bundle.getString("COMP_TYPE");


        questions();

        String tag_string_req = "req_login";
        pDialog.setMessage("Fetching Details...");
        showDialog();
        String url = http://suhrid1theinceptor.000webhostapp.com/userlogin/feedbackdetails.php?id="+id;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);

                    data1 = jObj.getJSONArray("respond");
                    for (int i = 0; i < data1.length(); i++) {
                        try {
                            JSONObject json = data1.getJSONObject(i);
                            etname.setText(json.getString("name"));
                            etemail.setText(json.getString("email"));
                            etphone.setText(json.getString("phone_no"));
                            etbirth.setText(json.getString("dob"));
                            etanniv.setText(json.getString("aniversary"));
                            etsuggest.setText(json.getString("suggestion"));
                            etbill.setText(json.getString("bill"));

                            mar.setText(json.getString("marital_status"));
                            if (json.getString("marital_status").equals("Married")){
                                etanniv.setVisibility(View.VISIBLE);
                                tv1.setVisibility(View.VISIBLE);
                            }

                            source.setText(json.getString("source"));
                            recom.setText(json.getString("reccomend"));
                            exp.setText(json.getString("experience"));

                            rtcol.setRating((float) json.getDouble("collection"));
                            rtstaff.setRating((float) json.getDouble("staff_behaviour"));
                            rtamb.setRating((float) json.getDouble("ambience"));
                            rtserv.setRating((float) json.getDouble("service"));
                            rtvfm.setRating((float) json.getDouble("value_for_money"));
                            rtoverall.setRating((float) json.getDouble("overall_experience"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


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

        if (mar.getText().equals("Married")){
            etanniv.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
        }

    }

    private void questions(){

        String tag_string_req = "req_login";
       // pDialog.setMessage("Fetching Feedbacks...");
       // showDialog();
        String url = "http://suhrid1theinceptor.000webhostapp.com/userlogin/company_type_details.php?type="+type;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
               // hideDialog();

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
