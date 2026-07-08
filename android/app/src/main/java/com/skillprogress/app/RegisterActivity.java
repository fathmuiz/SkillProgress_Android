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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoLogin = findViewById(R.id.tvGoLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String nama = etNama.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama wajib diisi");
            etNama.requestFocus();
            return;
        }
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
        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Memproses...");

        JSONObject body = new JSONObject();
        try {
            body.put("nama", nama);
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, ApiConfig.URL_REGISTER, body,
                response -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Daftar");
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(RegisterActivity.this, "Registrasi berhasil, silakan masuk.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, response.optString("message", "Registrasi gagal"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivity.this, "Terjadi kesalahan data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Daftar");
                    Toast.makeText(RegisterActivity.this, "Gagal terhubung ke server. Periksa koneksi.", Toast.LENGTH_LONG).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
