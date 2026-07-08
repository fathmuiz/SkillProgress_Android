-- Database: db_skillprogress
CREATE DATABASE IF NOT EXISTS db_skillprogress;
USE db_skillprogress;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    nama_skill VARCHAR(150) NOT NULL,
    materi_hari_ini VARCHAR(255),
    status_paham INT NOT NULL DEFAULT 0,
    catatan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE riwayat_materi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    skill_id INT NOT NULL,
    tanggal DATE NOT NULL,
    materi VARCHAR(255) NOT NULL,
    persentase INT NOT NULL DEFAULT 0,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);
