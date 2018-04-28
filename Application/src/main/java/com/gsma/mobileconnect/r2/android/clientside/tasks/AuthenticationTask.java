package com.gsma.mobileconnect.r2.android.clientside.tasks;


import android.os.AsyncTask;

import com.gsma.mobileconnect.r2.android.clientside.interfaces.ICallback;

public class AuthenticationTask extends AsyncTask<String, Void, String> {

    private static final String TAG = AuthenticationTask.class.getSimpleName();

    public static ICallback callback;

    @Override
    protected String doInBackground(String... strings) {
        String requestUrl = strings[0];
//        StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                callback.onComplete(s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e(TAG, "Got error response");
//            }
//        });
//        ClientSideAppFragment.requestQueue.add(request);
        return null;
    }
}
