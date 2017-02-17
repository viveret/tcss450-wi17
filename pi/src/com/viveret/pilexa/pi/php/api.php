<?php
class PilexaApi {
    private $myConfig = null;

    function __construct() {
        $this->myConfig = json_decode(file_get_contents("pilexa-config.json"), true);
        if (is_null($this->myConfig)) {
            echo json_last_error_msg();
        }
    }

    function getConfig($key) {
        $cur = $this->myConfig;
        foreach (explode('.', $key) as $crumb) {
            $cur = $cur[$crumb];
        }
        return $cur;
    }

    function getEntireConfig() {
        return $this->myConfig;
    }

    function doThing($params) {
        $ret = array("msg" => "OK", "status" => 0);
        switch ($params['op']) {
            case 'canConnect':
                break;
            case 'queryConfig':
                if (isset($params['key'])) {
                    $ret['val'] = $this->getConfig($params['key']);
                } else {
                    $ret['msg'] = 'Missing key';
                    $ret['status'] = 1;
                }
                break;
            case 'setConfig':
                break;
            default:
                $ret['msg'] = 'Invalid op ' + $params['op'];
                $ret['status'] = 1;
                break;
        }

        return $ret;
    }
}
?>