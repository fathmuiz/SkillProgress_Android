package com.skillprogress.app.api;

public class ApiConfig {
    // Ganti 192.168.xx.x dengan alamat IP server PHP jika menggunakan device hp.
    // 10.0.2.2 adalah alamat localhost mesin host saat dijalankan di emulator Android Studio.
    public static final String BASE_URL = "http://aeby.whf.bz/backend/";

    public static final String URL_REGISTER = BASE_URL + "auth/register.php";
    public static final String URL_LOGIN = BASE_URL + "auth/login.php";

    public static final String URL_SKILL_CREATE = BASE_URL + "skills/create.php";
    public static final String URL_SKILL_READ = BASE_URL + "skills/read.php";
    public static final String URL_SKILL_READ_ONE = BASE_URL + "skills/read_one.php";
    public static final String URL_SKILL_UPDATE = BASE_URL + "skills/update.php";
    public static final String URL_SKILL_DELETE = BASE_URL + "skills/delete.php";
}
