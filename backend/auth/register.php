<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

$data = json_decode(file_get_contents("php://input"));

if (empty($data->nama) || empty($data->email) || empty($data->password)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Nama, email, dan password wajib diisi."));
    exit();
}

$nama = trim($data->nama);
$email = trim(strtolower($data->email));
$password = $data->password;

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Format email tidak valid."));
    exit();
}

if (strlen($password) < 6) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Password minimal 6 karakter."));
    exit();
}

// Cek email sudah terdaftar
$check = $db->prepare("SELECT id FROM users WHERE email = ?");
$check->bind_param("s", $email);
$check->execute();
$check->store_result();
if ($check->num_rows > 0) {
    http_response_code(409);
    echo json_encode(array("success" => false, "message" => "Email sudah terdaftar."));
    exit();
}
$check->close();

$hashedPassword = password_hash($password, PASSWORD_DEFAULT);

$stmt = $db->prepare("INSERT INTO users (nama, email, password) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $nama, $email, $hashedPassword);

if ($stmt->execute()) {
    http_response_code(201);
    echo json_encode(array(
        "success" => true,
        "message" => "Registrasi berhasil.",
        "user" => array("id" => $stmt->insert_id, "nama" => $nama, "email" => $email)
    ));
} else {
    http_response_code(500);
    echo json_encode(array("success" => false, "message" => "Registrasi gagal: " . $stmt->error));
}
$stmt->close();
$db->close();
