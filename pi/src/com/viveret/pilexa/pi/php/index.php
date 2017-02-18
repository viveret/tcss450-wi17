<?php
require_once 'api.php';

if (isset($_GET['op'])) {
    $api = new PilexaApi();
    echo json_encode($api->doThing($_GET));
} else {
    $dataPre = file_get_contents('php://input');
    $data = json_decode($dataPre, true);
    if (isset($data['op'])) {
        $api = new PilexaApi();
        echo json_encode($api->doThing($data));
    } else {
        echo json_encode(array("msg" => "Nothing done!!", "status" => 0, "test" => $dataPre));
    }
}
?>