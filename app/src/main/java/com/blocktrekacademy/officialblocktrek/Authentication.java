// TODO: Make the button go to a link (Help Me page)

package com.blocktrekacademy.officialblocktrek;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;



public class Authentication extends AppCompatActivity {
    public static final String LOGIN_URL        = "http://blocktest.pe.hu/bta-api/api/v1/request.php";
    public static final String KEY_USERNAME     = "username";
    public static final String KEY_PASSWORD     = "password";
    public static final String KEY_UUID         = "UUID";
    public static final String KEY_LLOGIN       = "last_login";
    public static final String KEY_RDATE        = "registration_date";
    public static final String KEY_MONEY        = "money";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        TextView messageText = (TextView) findViewById(R.id.message_text);
                        messageText.setText("Logic is the cement of our civilization, with which we ascend from chaos, using reason as our guide");
                        return true;
                    }
                }
        );
        SharedPreferences blockSession  = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn  = blockSession.getBoolean("logged_in", false);
        if(!isLoggedIn){
            Intent intent       = new Intent(this, Mainpage.class);
            startActivity(intent);
            finish();
        }
    }

    // Display a Message (FOR TESTING PURPOSES ONLY)
    public void displayMessage(View v) {

        final TextView messageText = (TextView) findViewById(R.id.message_text);
        EditText usernameField = (EditText) findViewById(R.id.usernameField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);

        // Fetch Username and Password
        final String strUsername = usernameField.getText().toString();
        final String strPassword = passwordField.getText().toString();

        // Fetch Length of Strings
        int intUsername = usernameField.getText().length();
        int intPassword = passwordField.getText().length();

        if (strUsername.matches("")) {
            messageText.setText("Please put a valid username!");

        } else if (intUsername < 3) {
            messageText.setText("Username length is not enough!");

        } else if (intUsername > 16) {
            messageText.setText("Username length exceeded to maximum value!");

        } else if (strPassword.matches("")) {
            messageText.setText("Please put a valid password!");

        } else if (intPassword < 6) {
            messageText.setText("Password length is not enough!");

        } else if (intPassword > 32) {
            messageText.setText("Password length exceeded to maximum value!");

        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    try {
                        JSONObject volleyResponse   = new JSONObject(response);

                        boolean success       = volleyResponse.getBoolean("success");
                        String message        = volleyResponse.getString("message");

                        String UUID     = volleyResponse.getString("unique_user_id");
                        String LLOGIN   = volleyResponse.getString("last_login");
                        String RDATE    = volleyResponse.getString("registration_date");
                        String MONEY    = volleyResponse.getString("money");

                        if(success){
                            SharedPreferences blockSession = PreferenceManager.getDefaultSharedPreferences(Authentication.this);
                            SharedPreferences.Editor blockEdit = blockSession.edit();

                            blockEdit.putBoolean("logged_in", true);
                            blockEdit.putString("KEY_USERNAME", strUsername);
                            blockEdit.putString("KEY_UUID", UUID);
                            blockEdit.putString("KEY_LLOGIN", LLOGIN);
                            blockEdit.putString("KEY_RDATE", RDATE);
                            blockEdit.putString("KEY_MONEY", MONEY);
                            blockEdit.apply();

                            Intent intent = new Intent(Authentication.this, Mainpage.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch(JSONException e) {
                        response = response.replace("\"", "");
                        response = response.replace("status:false,message:", "");
                        response = response.replace("{", "");
                        response = response.replace("}", "");
                        messageText.setText(response);
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(Authentication.this,error.toString(),Toast.LENGTH_LONG ).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(KEY_USERNAME, strUsername);
                    map.put(KEY_PASSWORD, strPassword);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }


    // Open a URL
    public void openURL(View v) {
        String url = "http://www.blocktrekacademy.tk";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
