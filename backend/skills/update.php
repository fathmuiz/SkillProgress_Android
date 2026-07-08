<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, PUT');

include_once '../config/database.php';
$database = new Database();
$db = $database->getConnection();

$data = json_decode(file_get_contents("php://input"));

if (empty($data->id) || empty($data->nama_skill) || !isset($data->status_paham)) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "id, nama_skill, dan status_paham wajib diisi."));
    exit();
}

$id = intval($data->id);
$nama_skill = trim($data->nama_skill);
$materi_hari_ini = isset($data->materi_hari_ini) ? trim($data->materi_hari_ini) : "";
$status_paham = intval($data->status_paham);
$catatan = isset($data->catatan) ? trim($data->catatan) : "";

if ($status_paham < 0 || $status_paham > 100) {
    http_response_code(400);
    echo json_encode(array("success" => false, "message" => "Status paham harus antara 0-100."));
    exit();
}

$stmt = $db->prepare("UPDATE skills SET nama_skill = ?, materi_hari_ini = ?, status_paham = ?, catatan = ? WHERE id = ?");
$stmt->bind_param("ssisi", $nama_skill, $materi_hari_ini, $status_paham, $catatan, $id);

if ($stmt->execute()) {
    if (!empty($materi_hari_ini)) {
        $rstmt = $db->prepare("INSERT INTO riwayat_materi (skill_id, tanggal, materi, persentase) VALUES (?, CURDATE(), ?, ?)");
        $rstmt->bind_param("isi", $id, $materi_hari_ini, $status_paham);
        $rstmt->execute();
        $rstmt->close();
    }
    echo json_encode(array("success" => true, "message" => "Skill berhasil diperbarui."));
} else {
    http_response_code(500);
    echo json_encode(array("success" => false, "message" => "Gagal memperbarui skill: " . $stmt->error));
}
$stmt->close();
$db->close();
