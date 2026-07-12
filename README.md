# 📘 SkillProgress — Jurnal Belajar Mandiri

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![PHP](https://img.shields.io/badge/PHP-777BB4?style=flat&logo=php&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

> Catat, pantau, dan tingkatkan skill kamu setiap hari.

SkillProgress adalah aplikasi Android jurnal belajar mandiri yang membantu pengguna mencatat, memantau, dan meningkatkan skill yang sedang mereka pelajari. Dibangun dengan **Java (Android Studio)**, terhubung ke **REST API berbasis PHP**, dengan data tersimpan di **MySQL/MariaDB**.

---

<p> 
    <img src="assets/Screenshot 2026-07-08 232140.png" width="150" align="left" alt="Tampilan utama aplikasi" > 
    </p>  

 ### ✨ Fitur Utama

- 🔐 **Registrasi & Login** — dengan validasi input dan password ter-hash
- 📚 **CRUD Skill** — tambah, lihat, ubah, dan hapus skill yang sedang dipelajari
- 📊 **Statistik Ringkas** — total skill, materi hari ini, rata-rata pemahaman
- 📈 **Progress Pemahaman** — visualisasi persentase paham per skill
- 🕒 **Riwayat Materi Otomatis** — tercatat setiap kali skill diperbarui
- ✅ **Validasi Input** — di setiap form (email, panjang password, rentang 0–100, dll)
- 🔄 **Pull to Refresh** — muat ulang daftar skill dengan menarik ke bawah

<br>

<br>

<br>

---
## 🛠️ Tech Stack

| Layer | Teknologi |
|---|---|
| Mobile App | Android Studio, Java |
| Networking | Volley (HTTP client untuk REST API) |
| Backend | PHP (native, mysqli) |
| Database | MySQL / MariaDB |
| Arsitektur | REST API (Create, Read, Update, Delete penuh via HTTP) |

> Aplikasi ini **tidak** menggunakan database lokal (SQLite/Room). Seluruh data disimpan di MySQL dan diakses murni melalui REST API PHP.

---

## 🗂️ Struktur Proyek

```
SkillProgress/
├── android/                     # Project Android Studio (Java)
│   └── app/src/main/
│       ├── java/com/skillprogress/app/
│       │   ├── LoginActivity.java
│       │   ├── RegisterActivity.java
│       │   ├── MainActivity.java
│       │   ├── DetailSkillActivity.java
│       │   ├── TambahSkillActivity.java
│       │   ├── adapter/         # SkillAdapter, RiwayatAdapter (RecyclerView)
│       │   ├── api/             # ApiConfig, VolleySingleton, request helpers
│       │   ├── model/           # Skill, User, RiwayatMateri
│       │   └── utils/           # SessionManager
│       └── res/                 # layout, drawable, values
│
└── backend/                      # REST API PHP + skema database
    ├── config/database.php
    ├── auth/                     # register.php, login.php
    ├── skills/                   # create, read, read_one, update, delete
    ├── riwayat/                  # read.php
    └── database.sql
```

---

## 🧩 Activity & Navigasi

| Activity | Fungsi |
|---|---|
| `LoginActivity` | Launcher activity, menangani login |
| `RegisterActivity` | Pendaftaran akun baru |
| `MainActivity` | Daftar skill + statistik ringkas |
| `DetailSkillActivity` | Detail skill + riwayat materi |
| `TambahSkillActivity` | Tambah skill baru / edit skill (mode ganda) |

Navigasi antar-Activity menggunakan **Intent** (termasuk pengiriman data lewat extra untuk mode edit), dan daftar data ditampilkan dengan **RecyclerView** (skill list & riwayat materi).

---

## 🗄️ Skema Database (ERD)

```
users (1) ──< skills (1) ──< riwayat_materi
```

- **users**: `id`, `nama`, `email`, `password`, `created_at`
- **skills**: `id`, `user_id (FK)`, `nama_skill`, `materi_hari_ini`, `status_paham`, `catatan`, `created_at`, `updated_at`
- **riwayat_materi**: `id`, `skill_id (FK)`, `tanggal`, `materi`, `persentase`

Skema lengkap ada di [`backend/database.sql`](backend/database.sql).

---

## 🔌 REST API Endpoints

| Method | Endpoint | Fungsi |
|---|---|---|
| POST | `/auth/register.php` | Registrasi akun baru |
| POST | `/auth/login.php` | Login pengguna |
| POST | `/skills/create.php` | Tambah skill |
| GET | `/skills/read.php?user_id=` | Daftar skill milik user |
| GET | `/skills/read_one.php?id=` | Detail skill + riwayat materi |
| POST | `/skills/update.php` | Perbarui skill |
| POST | `/skills/delete.php` | Hapus skill |
| GET | `/riwayat/read.php?skill_id=` | Riwayat materi per skill |

Contoh request/response lengkap untuk setiap endpoint ada di dokumentasi teknis [`docs/Dokumentasi_Teknis_SkillProgress.docx`](docs/Dokumentasi_Teknis_SkillProgress.docx).

**Contoh singkat — Login:**
```json
// Request
POST /auth/login.php
{ "email": "andi@email.com", "password": "andi123" }

// Response
{
  "success": true,
  "message": "Login berhasil.",
  "user": { "id": 1, "nama": "Andi Saputra", "email": "andi@email.com" }
}
```

---

## 🌐 Hosting & Deployment

Backend aplikasi ini di-deploy menggunakan **[GoogieHost](https://googiehost.com)** (free hosting, panel DirectAdmin, PHP 8 + MySQL).

| Item | Keterangan |
|---|---|
| Hosting Provider | GoogieHost (free plan) |
| Control Panel | DirectAdmin |
| Domain Backend | `http://aeby.whf.bz/backend/` |
| Database Host | `localhost` (PHP & MySQL satu server) |
| PHP Version | 8.0 / 8.1 |

### Kenapa tidak pakai InfinityFree?
Sempat dicoba menggunakan **InfinityFree**, namun request dari aplikasi Android (via Volley) diblokir oleh sistem proteksi anti-bot mereka (halaman "Suspected D)DoS attack"), sehingga tidak bisa dipakai untuk komunikasi REST API dari aplikasi mobile. Karena itu, backend dipindah ke **GoogieHost** yang tidak memiliki kendala serupa.

### Catatan Teknis Tambahan
Sebagai langkah antisipasi terhadap proteksi anti-bot serupa, seluruh request Volley pada aplikasi ini menggunakan class kustom [`AppJsonObjectRequest`](android/app/src/main/java/com/skillprogress/app/api/AppJsonObjectRequest.java) yang menambahkan header `User-Agent` menyerupai browser normal, serta [`VolleyErrorHelper`](android/app/src/main/java/com/skillprogress/app/api/VolleyErrorHelper.java) untuk menampilkan pesan error asli dari server (bukan sekadar pesan generik) saat REST API mengembalikan kode selain 2xx (contoh: 409 saat email sudah terdaftar).

### Langkah Deploy Singkat
1. Daftar akun gratis di GoogieHost → dapat subdomain otomatis.
2. Buat database MySQL lewat menu **MySQL Management** di DirectAdmin.
3. Import `backend/database.sql` di dasboard account manager pilih database scroll ke bawah import (hapus baris `CREATE DATABASE`/`USE` di file sebelum import, karena database sudah dibuat lewat panel).
4. Sesuaikan `backend/config/database.php` dengan kredensial database dari DirectAdmin.
5. Upload folder `backend/` ke `public_html/` lewat File Manager.
6. Update `BASE_URL` di `ApiConfig.java`, lalu build APK release.

---



### 1. Setup Backend (PHP + MySQL)

1. Upload folder `backend/` ke hosting PHP+MySQL (atau XAMPP/Laragon untuk lokal).
2. Import `backend/database.sql` ke database MySQL/MariaDB kamu.
3. Edit `backend/config/database.php`, sesuaikan dengan kredensial database:
   ```php
   private $host = "localhost";
   private $db_name = "nama_database_kamu";
   private $username = "username_database_kamu";
   private $password = "password_database_kamu";
   ```

### 2. Setup Android App

1. Buka folder `android/` di Android Studio.
2. Tunggu Gradle sync selesai.
3. Edit `app/src/main/java/com/skillprogress/app/api/ApiConfig.java`:
   ```java
   public static final String BASE_URL = "http://domain-backend-kamu.whf.bz/backend/";
   ```
4. Jalankan aplikasi (**Run 'app'**).

### 3. Build APK Release

```
Build → Generate Signed Bundle / APK → APK → release

```

## 🧪 Validasi Input

Setiap form pada aplikasi ini menerapkan validasi sebelum data dikirim ke server:

- Field wajib tidak boleh kosong
- Format email harus valid (`Patterns.EMAIL_ADDRESS`)
- Password minimal 6 karakter
- Status paham harus berupa angka 0–100

---

## ⚠️ Catatan Penting — Gunakan Data Seluler, Bukan WiFi

Saat menguji coba aplikasi ini, **gunakan jaringan data seluler (mobile data)**, bukan WiFi. Beberapa jaringan WiFi (terutama WiFi kampus/kantor/publik) memiliki firewall atau pembatasan yang bisa memblokir koneksi ke domain hosting gratis seperti GoogieHost, sehingga aplikasi akan menampilkan pesan **"Gagal terhubung ke server"** meskipun backend sebenarnya online dan berfungsi normal.

---


## 📄 Lisensi

Proyek ini dibuat untuk keperluan tugas akademik dan bebas digunakan untuk pembelajaran.

