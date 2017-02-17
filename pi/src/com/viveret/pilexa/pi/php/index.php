<?php
require_once 'api.php';

if (isset($_GET['op'])) {
    $api = new PilexaApi();
    echo json_encode($api->doThing($_GET));
}

?>