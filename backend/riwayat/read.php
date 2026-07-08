<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

include_once '../config/database.php';
$database = new Database();
$db = $database->getConnection();

$skill_id = isset($_GET['skill_id']) ? intval($_GET['skill_id']) : 0;
if ($skill_id <= 0) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "skill_id wajib disertakan."));
    exit();
}

$stmt = $db->prepare("SELECT id, tanggal, materi, persentase FROM riwayat_materi WHERE skill_id = ? ORDER BY tanggal DESC");
$stmt->bind_param("i", $skill_id);
$stmt->execute();
$result = $stmt->get_result();

$riwayat = array();
while ($row = $result->fetch_assoc()) {
    $riwayat[] = $row;
}

echo json_encode(array("success" => true, "data" => $riwayat));
$stmt->close();
$db->close();
