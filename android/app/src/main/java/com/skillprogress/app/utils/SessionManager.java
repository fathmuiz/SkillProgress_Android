package com.skillprogress.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Menyimpan sesi login sederhana (bukan basis data utama, hanya menyimpan status login di perangkat)
public class SessionManager {
    private static final String PREF_NAME = "SkillProgressSession";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUserSession(int userId, String nama, String email) {
        editor.putInt("user_id", userId);
        editor.putString("nama", nama);
        editor.putString("email", email);
        editor.putBoolean("is_login", true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean("is_login", false);
    }

    public int getUserId() {
        return pref.getInt("user_id", -1);
    }

    public String getNama() {
        return pref.getString("nama", "");
    }

    public String getEmail() {
        return pref.getString("email", "");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
