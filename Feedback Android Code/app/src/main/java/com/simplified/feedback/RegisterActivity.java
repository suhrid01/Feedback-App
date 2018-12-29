package com.simplified.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.simplified.feedback.AppController.TAG;

public class RegisterActivity extends Activity implements AdapterView.OnItemSelectedListener {

    EditText fname,lname,email,password,tag;
    ProgressDialog pDialog;
    Button register;
    SearchableSpinner spinner;
    private ArrayList<String> comptype;
    JSONArray data;
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.address);
        tag = (EditText) findViewById(R.id.tag);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(false);

        comptype = new ArrayList<String>();
        comptype.add("Select Company Type");

        spinner = (SearchableSpinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        companytype();
        // implement setOnClickListener event on sign up Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api

                register();

            }
        });
    }

    private void companytype(){

        String tag_string_req = "req_login";
        //pDialog.setMessage("Adding Order...");
        //showDialog();
        String url = "http://suhrid1theinceptor.000webhostapp.com/userlogin/company_type.php";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
                // hideDialog();
                //Toast.makeText(getApplicationContext(),"Order added", Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    data = jObj.getJSONArray("respond");

                    for (int i = 0; i < data.length(); i++) {
                        try {
                            //Getting json object
                            JSONObject json = data.getJSONObject(i);
                            String cmp_id = json.getString("company_typename");

                                comptype.add(cmp_id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RegisterActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, comptype) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter2);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        }) {

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void register() {
        String tag_string_req = "req_login";
        // pDialog.setMessage("lllll");
        pDialog.setMessage("Creating Company...");
        showDialog();
        String url = "http://suhrid1theinceptor.000webhostapp.com/userlogin/register_feedback.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            public static final String TAG = "Volly Message" ;

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
                hideDialog();
                Toast.makeText(getApplicationContext(), "Feedback form Submitted Successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);


                startActivity(i);

            }
        }, new Response.ErrorListener() {

            public static final String TAG = "Error Message";

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
                params.put("company_name", fname.getText().toString());
                params.put("username", lname.getText().toString());
                params.put("company_address", email.getText().toString());
                params.put("company_tag", tag.getText().toString());
                params.put("password", password.getText().toString());
                params.put("isadmin", "0");
                params.put("company_type", cid);

            /*params.put("email", etemail.getText().toString());
            params.put("phone_no", etphone.getText().toString());
            params.put("dob", etbirth.getText().toString());
            params.put("aniversary", etanniv.getText().toString());
            params.put("suggest", etsuggest.getText().toString());
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
            //params.put("token", SharedPreference.getInstance(getApplicationContext()).getToken());*/
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        cid=getid(position-1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private String getid(int position) {
        String desc="" ;

        try {
            //Getting json object
            //JSONObject json = data1.getJSONObject(i);

            JSONObject json1 = data.getJSONObject(position);

            desc = json1.getString("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return desc;
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
