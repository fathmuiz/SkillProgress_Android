package com.skillprogress.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.skillprogress.app.api.CustomJsonObjectRequest;
import com.skillprogress.app.api.ApiConfig;
import com.skillprogress.app.api.VolleySingleton;
import com.skillprogress.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class TambahSkillActivity extends AppCompatActivity {

    private EditText etNamaSkill, etMateriHariIni, etStatusPaham, etCatatan;
    private Button btnSimpan;
    private TextView btnBack, tvTitle;
    private SessionManager sessionManager;

    // Jika diisi (skill_id > 0), berarti mode edit
    private int skillId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_skill);

        sessionManager = new SessionManager(this);

        etNamaSkill = findViewById(R.id.etNamaSkill);
        etMateriHariIni = findViewById(R.id.etMateriHariIni);
        etStatusPaham = findViewById(R.id.etStatusPaham);
        etCatatan = findViewById(R.id.etCatatan);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);

        btnBack.setOnClickListener(v -> finish());

        // Cek apakah ini mode edit (menerima data dari DetailSkillActivity via Intent)
        Intent i = getIntent();
        if (i.hasExtra("skill_id")) {
            skillId = i.getIntExtra("skill_id", -1);
            tvTitle.setText("Edit Skill");
            etNamaSkill.setText(i.getStringExtra("nama_skill"));
            etMateriHariIni.setText(i.getStringExtra("materi_hari_ini"));
            etStatusPaham.setText(String.valueOf(i.getIntExtra("status_paham", 0)));
            etCatatan.setText(i.getStringExtra("catatan"));
        }

        btnSimpan.setOnClickListener(v -> handleSimpan());
    }

    private void handleSimpan() {
        String namaSkill = etNamaSkill.getText().toString().trim();
        String materiHariIni = etMateriHariIni.getText().toString().trim();
        String statusStr = etStatusPaham.getText().toString().trim();
        String catatan = etCatatan.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(namaSkill)) {
            etNamaSkill.setError("Nama skill wajib diisi");
            etNamaSkill.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(statusStr)) {
            etStatusPaham.setError("Status paham wajib diisi");
            etStatusPaham.requestFocus();
            return;
        }

        int statusPaham;
        try {
            statusPaham = Integer.parseInt(statusStr);
        } catch (NumberFormatException e) {
            etStatusPaham.setError("Status paham harus berupa angka");
            etStatusPaham.requestFocus();
            return;
        }

        if (statusPaham < 0 || statusPaham > 100) {
            etStatusPaham.setError("Status paham harus antara 0-100");
            etStatusPaham.requestFocus();
            return;
        }

        btnSimpan.setEnabled(false);
        btnSimpan.setText("Menyimpan...");

        JSONObject body = new JSONObject();
        try {
            if (skillId > 0) {
                body.put("id", skillId);
            } else {
                body.put("user_id", sessionManager.getUserId());
            }
            body.put("nama_skill", namaSkill);
            body.put("materi_hari_ini", materiHariIni);
            body.put("status_paham", statusPaham);
            body.put("catatan", catatan);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = (skillId > 0) ? ApiConfig.URL_SKILL_UPDATE : ApiConfig.URL_SKILL_CREATE;

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(TambahSkillActivity.this, response.optString("message", "Berhasil disimpan"), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(TambahSkillActivity.this, response.optString("message", "Gagal menyimpan"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(TambahSkillActivity.this, "Terjadi kesalahan data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");
                    Toast.makeText(TambahSkillActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_LONG).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
