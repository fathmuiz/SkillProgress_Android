<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

include_once '../config/database.php';

$database = new Database();
$db = $database->getConnection();

$data = json_decode(file_get_contents("php://input"));

if (empty($data->email) || empty($data->password)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Email dan password wajib diisi."));
    exit();
}

$email = trim(strtolower($data->email));
$password = $data->password;

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Format email tidak valid."));
    exit();
}

$stmt = $db->prepare("SELECT id, nama, email, password FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    http_response_code(404);
    echo json_encode(array("success" => false, "message" => "Email tidak ditemukan."));
    exit();
}

$user = $result->fetch_assoc();

if (!password_verify($password, $user['password'])) {
    http_response_code(401);
    echo json_encode(array("success" => false, "message" => "Password salah."));
    exit();
}

echo json_encode(array(
    "success" => true,
    "message" => "Login berhasil.",
    "user" => array("id" => $user['id'], "nama" => $user['nama'], "email" => $user['email'])
));

$stmt->close();
$db->close();
