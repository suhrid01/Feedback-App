package com.simplified.feedback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    EditText  password, name;
    ProgressDialog pDialog;
    SessionManager session;

    Button login;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.username);

        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager


        HashMap<String, String> user = db.getUserDetails();
        session = new SessionManager(getApplicationContext());

        String u_id = user.get("u_id");
        String emp = user.get("is_staff");
        String comp_type = user.get("emp_type");

        if (session.isLoggedIn()){

            Intent i = new Intent(LoginActivity.this, BoxActivity.class);
            Bundle bundle = new Bundle();

            //Add your data from getFactualResults method to bundle
            bundle.putString("ID",u_id);
            bundle.putString("IS_ADMIN",emp);
            bundle.putString("COMP_TYPE",comp_type);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }

              //emailId = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        // implement setOnClickListener event on sign up Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api
                if (validate(name) && validate(password)) {
                    login();
                }
            }
        });
    }


    private void login() {
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging In...");
        showDialog();
        String url = "http://smspl.in/feedback_app/login_feedback.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            public static final String TAG = "Volly Message" ;

            @Override
            public void onResponse(String response) {
                Log.i("tagconvertstr", "[" + response + "]");
                Log.d(TAG, " Response: " + response);
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    session.setLogin(true);

                        db.addUser(jObj.getString("id"), jObj.getString("isadmin"),jObj.getString("company_type"));

                        Intent i = new Intent(LoginActivity.this, BoxActivity.class);
                        Bundle bundle = new Bundle();

                        //Add your data from getFactualResults method to bundle
                        bundle.putString("ID",jObj.getString("id"));
                        bundle.putString("IS_ADMIN",jObj.getString("isadmin"));
                        bundle.putString("COMP_TYPE",jObj.getString("company_type"));
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();


                    //Toast.makeText(getApplicationContext(), "Feedback form Submitted Successfully", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


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
                params.put("username", name.getText().toString());
                params.put("password", password.getText().toString());
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


    private boolean validate(EditText editText) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() > 0) {
            return true; // returns true if field is not empty
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
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

