<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

include_once '../config/database.php';
$database = new Database();
$db = $database->getConnection();

$id = isset($_GET['id']) ? intval($_GET['id']) : 0;
if ($id <= 0) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "id wajib disertakan."));
    exit();
}

$stmt = $db->prepare("SELECT id, user_id, nama_skill, materi_hari_ini, status_paham, catatan, created_at FROM skills WHERE id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    http_response_code(404);
    echo json_encode(array("success" => false, "message" => "Skill tidak ditemukan."));
    exit();
}

$skill = $result->fetch_assoc();

$rstmt = $db->prepare("SELECT id, tanggal, materi, persentase FROM riwayat_materi WHERE skill_id = ? ORDER BY tanggal DESC");
$rstmt->bind_param("i", $id);
$rstmt->execute();
$rresult = $rstmt->get_result();
$riwayat = array();
while ($row = $rresult->fetch_assoc()) {
    $riwayat[] = $row;
}
$skill['riwayat'] = $riwayat;

echo json_encode(array("success" => true, "data" => $skill));
$stmt->close();
$rstmt->close();
$db->close();
