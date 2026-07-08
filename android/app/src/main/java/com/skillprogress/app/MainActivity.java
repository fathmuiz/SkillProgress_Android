package com.skillprogress.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.skillprogress.app.api.CustomJsonObjectRequest;
import com.skillprogress.app.adapter.SkillAdapter;
import com.skillprogress.app.api.ApiConfig;
import com.skillprogress.app.api.VolleySingleton;
import com.skillprogress.app.model.Skill;
import com.skillprogress.app.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvGreeting, tvTotalSkill, tvMateriHariIni, tvRataPaham, btnTambahSkill, btnLogout, tvEmptyState;
    private RecyclerView rvSkills;
    private SwipeRefreshLayout swipeRefresh;
    private SessionManager sessionManager;
    private List<Skill> skillList;
    private SkillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        tvGreeting = findViewById(R.id.tvGreeting);
        tvTotalSkill = findViewById(R.id.tvTotalSkill);
        tvMateriHariIni = findViewById(R.id.tvMateriHariIni);
        tvRataPaham = findViewById(R.id.tvRataPaham);
        btnTambahSkill = findViewById(R.id.btnTambahSkill);
        btnLogout = findViewById(R.id.btnLogout);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        rvSkills = findViewById(R.id.rvSkills);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        tvGreeting.setText("Halo, " + sessionManager.getNama() + " :)");

        skillList = new ArrayList<>();
        adapter = new SkillAdapter(this, skillList);
        rvSkills.setLayoutManager(new LinearLayoutManager(this));
        rvSkills.setAdapter(adapter);

        btnTambahSkill.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TambahSkillActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        swipeRefresh.setOnRefreshListener(this::loadSkills);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSkills();
    }

    private void loadSkills() {
        swipeRefresh.setRefreshing(true);
        String url = ApiConfig.URL_SKILL_READ + "?user_id=" + sessionManager.getUserId();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    swipeRefresh.setRefreshing(false);
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray data = response.getJSONArray("data");
                            skillList.clear();
                            int totalPaham = 0;
                            int materiHariIniCount = 0;

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                Skill skill = new Skill();
                                skill.setId(obj.getInt("id"));
                                skill.setNamaSkill(obj.getString("nama_skill"));
                                skill.setMateriHariIni(obj.optString("materi_hari_ini", ""));
                                skill.setStatusPaham(obj.getInt("status_paham"));
                                skill.setCatatan(obj.optString("catatan", ""));
                                skillList.add(skill);

                                totalPaham += skill.getStatusPaham();
                                if (skill.getMateriHariIni() != null && !skill.getMateriHariIni().isEmpty()) {
                                    materiHariIniCount++;
                                }
                            }

                            adapter.notifyDataSetChanged();

                            tvTotalSkill.setText(String.valueOf(skillList.size()));
                            tvMateriHariIni.setText(String.valueOf(materiHariIniCount));
                            int rata = skillList.isEmpty() ? 0 : (totalPaham / skillList.size());
                            tvRataPaham.setText(rata + "%");

                            tvEmptyState.setVisibility(skillList.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
                            rvSkills.setVisibility(skillList.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Gagal memuat data skill.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
