package com.skillprogress.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.skillprogress.app.api.CustomJsonObjectRequest;
import com.skillprogress.app.adapter.RiwayatAdapter;
import com.skillprogress.app.api.ApiConfig;
import com.skillprogress.app.api.VolleySingleton;
import com.skillprogress.app.model.RiwayatMateri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailSkillActivity extends AppCompatActivity {

    private TextView btnBack, btnEdit, btnHapus, tvNamaSkill, tvTanggalDibuat, tvPersenBesar,
            tvStatusLabel, tvStatusHint, tvMateriHariIni, tvCatatan, tvEmptyRiwayat;
    private View progressFill;
    private RecyclerView rvRiwayat;

    private int skillId;
    private String currentNamaSkill, currentMateriHariIni, currentCatatan;
    private int currentStatusPaham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_skill);

        skillId = getIntent().getIntExtra("skill_id", -1);
        if (skillId <= 0) {
            Toast.makeText(this, "Skill tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnHapus = findViewById(R.id.btnHapus);
        tvNamaSkill = findViewById(R.id.tvNamaSkill);
        tvTanggalDibuat = findViewById(R.id.tvTanggalDibuat);
        tvPersenBesar = findViewById(R.id.tvPersenBesar);
        tvStatusLabel = findViewById(R.id.tvStatusLabel);
        tvStatusHint = findViewById(R.id.tvStatusHint);
        tvMateriHariIni = findViewById(R.id.tvMateriHariIni);
        tvCatatan = findViewById(R.id.tvCatatan);
        tvEmptyRiwayat = findViewById(R.id.tvEmptyRiwayat);
        progressFill = findViewById(R.id.progressFill);
        rvRiwayat = findViewById(R.id.rvRiwayat);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(DetailSkillActivity.this, TambahSkillActivity.class);
            intent.putExtra("skill_id", skillId);
            intent.putExtra("nama_skill", currentNamaSkill);
            intent.putExtra("materi_hari_ini", currentMateriHariIni);
            intent.putExtra("status_paham", currentStatusPaham);
            intent.putExtra("catatan", currentCatatan);
            startActivity(intent);
        });

        btnHapus.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetail();
    }

    private void loadDetail() {
        String url = ApiConfig.URL_SKILL_READ_ONE + "?id=" + skillId;

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");
                            currentNamaSkill = data.getString("nama_skill");
                            currentMateriHariIni = data.optString("materi_hari_ini", "");
                            currentStatusPaham = data.getInt("status_paham");
                            currentCatatan = data.optString("catatan", "");
                            String createdAt = data.optString("created_at", "");

                            tvNamaSkill.setText(currentNamaSkill);
                            tvTanggalDibuat.setText("Sejak " + createdAt);
                            tvPersenBesar.setText(currentStatusPaham + "%");
                            tvMateriHariIni.setText(currentMateriHariIni.isEmpty() ? "-" : currentMateriHariIni);
                            tvCatatan.setText(currentCatatan.isEmpty() ? "-" : currentCatatan);

                            if (currentStatusPaham >= 80) {
                                tvStatusLabel.setText("Paham");
                                tvStatusHint.setText("Pemahaman sudah sangat baik");
                            } else if (currentStatusPaham >= 50) {
                                tvStatusLabel.setText("Cukup Paham");
                                tvStatusHint.setText("Butuh review di beberapa bagian");
                            } else {
                                tvStatusLabel.setText("Butuh Review");
                                tvStatusHint.setText("Perlu banyak latihan tambahan");
                            }

                            FrameLayout track = (FrameLayout) progressFill.getParent();
                            track.post(() -> {
                                int maxWidth = track.getWidth();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressFill.getLayoutParams();
                                params.width = (int) (maxWidth * (currentStatusPaham / 100f));
                                progressFill.setLayoutParams(params);
                            });

                            JSONArray riwayatArr = data.optJSONArray("riwayat");
                            List<RiwayatMateri> riwayatList = new ArrayList<>();
                            if (riwayatArr != null) {
                                for (int i = 0; i < riwayatArr.length(); i++) {
                                    JSONObject r = riwayatArr.getJSONObject(i);
                                    riwayatList.add(new RiwayatMateri(
                                            r.getInt("id"),
                                            r.getString("tanggal"),
                                            r.getString("materi"),
                                            r.getInt("persentase")
                                    ));
                                }
                            }
                            tvEmptyRiwayat.setVisibility(riwayatList.isEmpty() ? View.VISIBLE : View.GONE);
                            rvRiwayat.setAdapter(new RiwayatAdapter(DetailSkillActivity.this, riwayatList));
                        } else {
                            Toast.makeText(DetailSkillActivity.this, response.optString("message", "Gagal memuat detail."), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(DetailSkillActivity.this, "Terjadi kesalahan data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(DetailSkillActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Skill")
                .setMessage("Apakah kamu yakin ingin menghapus skill ini? Data yang dihapus tidak dapat dikembalikan.")
                .setPositiveButton("Hapus", (dialog, which) -> deleteSkill())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deleteSkill() {
        JSONObject body = new JSONObject();
        try {
            body.put("id", skillId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, ApiConfig.URL_SKILL_DELETE, body,
                response -> {
                    Toast.makeText(DetailSkillActivity.this, "Skill berhasil dihapus.", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(DetailSkillActivity.this, "Gagal menghapus skill.", Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
