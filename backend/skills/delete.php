<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, DELETE');

include_once '../config/database.php';
$database = new Database();
$db = $database->getConnection();

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "id wajib disertakan."));
    exit();
}

$id = intval($data->id);

$stmt = $db->prepare("DELETE FROM skills WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    echo json_encode(array("success" => true, "message" => "Skill berhasil dihapus."));
} else {
    http_response_code(500);
    echo json_encode(array("success" => false, "message" => "Gagal menghapus skill: " . $stmt->error));
}
$stmt->close();
$db->close();
