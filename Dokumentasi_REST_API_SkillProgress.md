# Dokumentasi REST API — SkillProgress

Base URL (contoh lokal via emulator Android Studio):
```
http://10.0.2.2/skillprogress/backend/
```
Jika menggunakan HP fisik, ganti `10.0.2.2` dengan alamat IP komputer server di jaringan yang sama, contoh `http://192.168.1.10/skillprogress/backend/`.

Semua response menggunakan format **JSON**. Field `success` (boolean) selalu ada untuk menandakan status request.

---

## Daftar Endpoint

| No | Endpoint | Method | Fungsi |
|----|----------|--------|--------|
| 1 | `/auth/register.php` | POST | Registrasi akun baru |
| 2 | `/auth/login.php` | POST | Login pengguna |
| 3 | `/skills/create.php` | POST | Tambah data skill baru (Create) |
| 4 | `/skills/read.php` | GET | Ambil seluruh daftar skill milik user (Read - list) |
| 5 | `/skills/read_one.php` | GET | Ambil detail satu skill + riwayat materi (Read - detail) |
| 6 | `/skills/update.php` | POST | Perbarui data skill (Update) |
| 7 | `/skills/delete.php` | POST | Hapus data skill (Delete) |
| 8 | `/riwayat/read.php` | GET | Ambil riwayat materi berdasarkan skill_id |

---

## 1. Register — `POST /auth/register.php`

Mendaftarkan akun pengguna baru. Password otomatis di-hash di server (`password_hash`).

**Request Body (JSON):**
```json
{
  "nama": "Andi Saputra",
  "email": "andi@email.com",
  "password": "andi123"
}
```

**Response Sukses (201 Created):**
```json
{
  "success": true,
  "message": "Registrasi berhasil.",
  "user": {
    "id": 1,
    "nama": "Andi Saputra",
    "email": "andi@email.com"
  }
}
```

**Response Gagal — email sudah terdaftar (409 Conflict):**
```json
{
  "success": false,
  "message": "Email sudah terdaftar."
}
```

**Response Gagal — validasi (400 Bad Request):**
```json
{
  "success": false,
  "message": "Format email tidak valid."
}
```

---

## 2. Login — `POST /auth/login.php`

**Request Body (JSON):**
```json
{
  "email": "andi@email.com",
  "password": "andi123"
}
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "message": "Login berhasil.",
  "user": {
    "id": 1,
    "nama": "Andi Saputra",
    "email": "andi@email.com"
  }
}
```

**Response Gagal — password salah (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Password salah."
}
```

**Response Gagal — email tidak ditemukan (404 Not Found):**
```json
{
  "success": false,
  "message": "Email tidak ditemukan."
}
```

---

## 3. Tambah Skill (Create) — `POST /skills/create.php`

**Request Body (JSON):**
```json
{
  "user_id": 1,
  "nama_skill": "Belajar Android Java",
  "materi_hari_ini": "Belajar Intent & RecyclerView",
  "status_paham": 80,
  "catatan": "Masih perlu review bagian passing data antar activity dan custom adapter."
}
```

**Response Sukses (201 Created):**
```json
{
  "success": true,
  "message": "Skill berhasil ditambahkan.",
  "id": 5
}
```

**Response Gagal — validasi (400 Bad Request):**
```json
{
  "success": false,
  "message": "Status paham harus antara 0-100."
}
```

---

## 4. Daftar Skill (Read - List) — `GET /skills/read.php?user_id=1`

**Contoh Request:**
```
GET /skillprogress/backend/skills/read.php?user_id=1
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 5,
      "nama_skill": "Belajar Android Java",
      "materi_hari_ini": "Belajar Intent & RecyclerView",
      "status_paham": 80,
      "catatan": "Masih perlu review bagian passing data antar activity dan custom adapter.",
      "created_at": "2024-05-10 09:12:00"
    },
    {
      "id": 6,
      "nama_skill": "UI/UX Design",
      "materi_hari_ini": "Membuat Wireframe",
      "status_paham": 60,
      "catatan": "",
      "created_at": "2024-05-12 14:20:00"
    }
  ]
}
```

**Response ketika belum ada data:**
```json
{
  "success": true,
  "data": []
}
```

---

## 5. Detail Skill (Read - Detail) — `GET /skills/read_one.php?id=5`

**Contoh Request:**
```
GET /skillprogress/backend/skills/read_one.php?id=5
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": 5,
    "user_id": 1,
    "nama_skill": "Belajar Android Java",
    "materi_hari_ini": "Belajar Intent & RecyclerView",
    "status_paham": 80,
    "catatan": "Masih perlu review bagian passing data antar activity dan custom adapter.",
    "created_at": "2024-05-10 09:12:00",
    "riwayat": [
      { "id": 12, "tanggal": "2024-05-19", "materi": "View dan ViewGroup", "persentase": 90 },
      { "id": 11, "tanggal": "2024-05-18", "materi": "Layout (ConstraintLayout)", "persentase": 80 },
      { "id": 10, "tanggal": "2024-05-17", "materi": "Activity Lifecycle", "persentase": 70 }
    ]
  }
}
```

**Response Gagal — skill tidak ditemukan (404 Not Found):**
```json
{
  "success": false,
  "message": "Skill tidak ditemukan."
}
```

---

## 6. Update Skill — `POST /skills/update.php`

Setiap kali update dengan `materi_hari_ini` terisi, sistem otomatis menambah satu baris baru ke tabel `riwayat_materi`.

**Request Body (JSON):**
```json
{
  "id": 5,
  "nama_skill": "Belajar Android Java",
  "materi_hari_ini": "Belajar RecyclerView Adapter Lanjutan",
  "status_paham": 90,
  "catatan": "Sudah lebih paham soal ViewHolder pattern."
}
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "message": "Skill berhasil diperbarui."
}
```

**Response Gagal — validasi (400 Bad Request):**
```json
{
  "success": false,
  "message": "id, nama_skill, dan status_paham wajib diisi."
}
```

---

## 7. Hapus Skill (Delete) — `POST /skills/delete.php`

**Request Body (JSON):**
```json
{
  "id": 5
}
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "message": "Skill berhasil dihapus."
}
```

**Response Gagal (400 Bad Request):**
```json
{
  "success": false,
  "message": "id wajib disertakan."
}
```

---

## 8. Riwayat Materi per Skill — `GET /riwayat/read.php?skill_id=5`

Endpoint tambahan jika ingin mengambil riwayat materi tanpa memanggil detail skill secara lengkap.

**Contoh Request:**
```
GET /skillprogress/backend/riwayat/read.php?skill_id=5
```

**Response Sukses (200 OK):**
```json
{
  "success": true,
  "data": [
    { "id": 12, "tanggal": "2024-05-19", "materi": "View dan ViewGroup", "persentase": 90 },
    { "id": 11, "tanggal": "2024-05-18", "materi": "Layout (ConstraintLayout)", "persentase": 80 }
  ]
}
```

---

## Ringkasan Kode Status HTTP

| Kode | Arti |
|------|------|
| 200 | Berhasil (GET / update / delete) |
| 201 | Berhasil, data baru dibuat (register / create skill) |
| 400 | Input tidak valid / field wajib kosong |
| 401 | Password salah |
| 404 | Data tidak ditemukan |
| 409 | Konflik (email sudah terdaftar) |
| 500 | Kesalahan server / query database gagal |
