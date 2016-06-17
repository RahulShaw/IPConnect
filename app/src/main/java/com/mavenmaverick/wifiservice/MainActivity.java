package com.mavenmaverick.wifiservice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText user, pass, wifiname, loginurl;

    String username, password, ssid, url;

    private static final String CREDENTIALS = "Credentials";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        wifiname = (EditText) findViewById(R.id.ssid);
        loginurl = (EditText) findViewById(R.id.url);

        SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS, 0);
        if(sharedPreferences.contains("username")) {
            username = sharedPreferences.getString("username", "UNDEFINED");
            user.setText(username);

        }
        if(sharedPreferences.contains("password")) {
            password = sharedPreferences.getString("password", "UNDEFINED");
            pass.setText(password);

        }
        if(sharedPreferences.contains("ssid")) {
            ssid = sharedPreferences.getString("ssid", "UNDEFINED");
            wifiname.setText(ssid);

        }
        if(sharedPreferences.contains("url")) {
            url = sharedPreferences.getString("url", "UNDEFINED");
            loginurl.setText(url);

        }


    }

    public void startTheService(View v) {

        if(!user.getText().toString().isEmpty()
                && !pass.getText().toString().isEmpty()
                && !wifiname.getText().toString().isEmpty()
                && !loginurl.getText().toString().isEmpty()) {

                SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("username", user.getText().toString());
                editor.putString("password", pass.getText().toString());
                editor.putString("ssid", wifiname.getText().toString());
                editor.putString("url", loginurl.getText().toString());

                editor.commit();


                Intent intent = new Intent(this, WLANService.class);
                intent.putExtra("message", "I got your message!");
                startService(intent);

            } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setCancelable(false);

            builder.setTitle("Attention Please!");

            builder.setIcon(R.mipmap.ic_launcher);

            builder.setMessage("All Fields Are Mandatory");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


            }
        }

    public void stopTheService(View v) {
        stopService(new Intent(this, WLANService.class));
    }
}
