package com.skillprogress.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.skillprogress.app.api.ApiConfig;
import com.skillprogress.app.api.CustomJsonObjectRequest;
import com.skillprogress.app.api.VolleySingleton;
import com.skillprogress.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        // Jika sudah login, langsung ke MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Memproses...");

        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, ApiConfig.URL_LOGIN, body,
                response -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Masuk");
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject user = response.getJSONObject("user");
                            sessionManager.saveUserSession(
                                    user.getInt("id"),
                                    user.getString("nama"),
                                    user.getString("email")
                            );
                            Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, response.optString("message", "Login gagal"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Terjadi kesalahan data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Masuk");
                    Toast.makeText(LoginActivity.this, "Gagal terhubung ke server. Periksa koneksi.", Toast.LENGTH_LONG).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
