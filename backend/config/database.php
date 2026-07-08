<?php
// Konfigurasi koneksi database MySQL/MariaDB
class Database {
    private $host = "localhost";
    private $db_name = "db_skillprogress";
    private $username = "root";
    private $password = "";
    public $conn;

    public function getConnection() {
        $this->conn = null;
        try {
            $this->conn = new mysqli($this->host, $this->username, $this->password, $this->db_name);
            if ($this->conn->connect_error) {
                throw new Exception($this->conn->connect_error);
            }
            $this->conn->set_charset("utf8mb4");
        } catch (Exception $e) {
            header('Content-Type: application/json');
            echo json_encode(array("success" => false, "message" => "Koneksi database gagal: " . $e->getMessage()));
            exit();
        }
        return $this->conn;
    }
}
