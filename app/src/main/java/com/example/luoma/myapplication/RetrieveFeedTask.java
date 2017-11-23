package com.example.luoma.myapplication;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.luoma.myapplication.MainActivity.appToken;

/**
 * Created by LuoMa on 10/19/2017.
 */

public class RetrieveFeedTask extends AsyncTask<String, Void, String> {
    private Exception exception;
    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", appToken)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected String doInBackground(String... urls) {

        try {
            String response = run(urls[0]);
            return response;

        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }
    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
