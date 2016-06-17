package com.mavenmaverick.wifiservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginHelper {

    static String output;

    public static String doLogin(String username, String password, String URL) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("user", username)
                    .add("pass", password)
                    .add("login", "Login")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url(URL)
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            output = stringBuilder.toString();
            if(output.contains("Address")) {
                Log.i("Result", "Login Success!");
            }else {
                if(output.contains("n/a")) {
                    Log.i("Result", "Login Failed!");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(output != null) {
            return output;
        }else {
            return "";
        }

    }
}
