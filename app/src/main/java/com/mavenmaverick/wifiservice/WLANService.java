package com.mavenmaverick.wifiservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class WLANService extends Service {

    String username, password, ssid, url;

    private static final String CREDENTIALS = "Credentials";

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS, 0);
            if(sharedPreferences.contains("username")) {
                username = sharedPreferences.getString("username", "UNDEFINED");

            }
            if(sharedPreferences.contains("password")) {
                password = sharedPreferences.getString("password", "UNDEFINED");

            }
            if(sharedPreferences.contains("ssid")) {
                ssid = sharedPreferences.getString("ssid", "UNDEFINED");

            }
            if(sharedPreferences.contains("url")) {
                url = sharedPreferences.getString("url", "UNDEFINED");

            }

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            boolean connected = info.isConnected();
            if(connected) {
                Toast.makeText(context, "WIFI CONNECTED!", Toast.LENGTH_LONG).show();
                Log.i("Wi-Fi-State", "Wi-Fi is On!");
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                Log.i("SSID", String.valueOf(wifiInfo.getSSID().contains("Rahul")));

                if(wifiInfo.getSSID().contains(ssid) == true) {
                    try {
                        String output = new Connection().execute().get().toString();
                        Log.i("LoginState", new Connection().execute().get().toString());

                        if(output.contains("Address")) {
                            Toast.makeText(WLANService.this, "Login Success!", Toast.LENGTH_SHORT).show();
                        }else {
                            if(output.contains("n/a")) {
                                Toast.makeText(WLANService.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(context, "WIFI DISCONNECTED!", Toast.LENGTH_SHORT).show();
                Log.i("Wi-Fi-State", "Wi-Fi is Off!");
            }
        }
    };

    public WLANService() {
        // why do you need this, by the way?
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Auto-Login Enabled!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // registering your receiver
        registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Auto-Login Disabled!", Toast.LENGTH_SHORT).show();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return LoginHelper.doLogin(username, password, "http://".concat(url));
        }
    }

}

