package com.blocktrekacademy.officialblocktrek;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView usernameText       = (TextView) findViewById(R.id.usernameText);
        TextView moneyMain          = (TextView) findViewById(R.id.moneyMain);
        TextView lastLogin          = (TextView) findViewById(R.id.lastLoginMain);
        TextView registrationMain   = (TextView) findViewById(R.id.registrationMain);

        Intent intent = getIntent();

        SharedPreferences blockSession      = this.getSharedPreferences("blockSession", 0);
        SharedPreferences.Editor blockEdit  = blockSession.edit();

        String username             = blockSession.getString("KEY_USERNAME", null);
        String money                = blockSession.getString("KEY_MONEY", null);
        String lastLoginVar         = blockSession.getString("KEY_LLOGIN", null);
        String registrationDate     = blockSession.getString("KEY_RDATE", null);

        usernameText.setText(username);
        moneyMain.setText(money);
        lastLogin.setText(lastLoginVar);
        registrationMain.setText(registrationDate);
    }

    void logOut(){
        SharedPreferences blockSession = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor blockEdit = blockSession.edit();
        blockEdit.clear();
        blockEdit.apply();
        //finish();       /****<-----commented out this line---****/
        Intent intent = new Intent(Mainpage.this, Authentication.class);
        startActivity(intent);

        finish();        /****<------Moved to here---****/
    }
}
