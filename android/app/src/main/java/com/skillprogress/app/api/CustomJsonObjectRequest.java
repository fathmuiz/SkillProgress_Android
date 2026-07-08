package com.skillprogress.app.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonObjectRequest extends JsonObjectRequest {

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        // Trik utama: Menyamar sebagai Browser Chrome agar tidak diblokir InfinityFree
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        headers.put("Accept", "application/json");
        return headers;
    }
}
