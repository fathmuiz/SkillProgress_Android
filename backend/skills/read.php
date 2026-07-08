<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

include_once '../config/database.php';
$database = new Database();
$db = $database->getConnection();

$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if ($user_id <= 0) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "user_id wajib disertakan."));
    exit();
}

$stmt = $db->prepare("SELECT id, nama_skill, materi_hari_ini, status_paham, catatan, created_at FROM skills WHERE user_id = ? ORDER BY created_at DESC");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$skills = array();
while ($row = $result->fetch_assoc()) {
    $skills[] = $row;
}

echo json_encode(array("success" => true, "data" => $skills));
$stmt->close();
$db->close();
