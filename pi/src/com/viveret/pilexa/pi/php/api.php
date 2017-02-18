<?php
class PilexaApi {
    CONST CONFIG_FILE = "pilexa-config.json";
    private $myConfig = null;

    function __construct() {
        $this->myConfig = json_decode(file_get_contents(self::CONFIG_FILE), true);
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

    function setConfig($key, $val) {
        $cur = $this->myConfig;
        $crumbs = explode('.', $key);
        for ($i = 0; $i < count($crumbs) - 1; $i++) {
            $cur = $cur[$crumbs[$i]];
        }
        $cur[$crumbs[count($crumbs) - 1]] = $val;
    }

    function getEntireConfig() {
        return $this->myConfig;
    }

    function flushConfig() {
        $myfile = fopen(self::CONFIG_FILE, "w") or die("Unable to open file!");
        fwrite($myfile, json_encode($this->myConfig));
        fclose($myfile);
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
            case 'queryEntireConfig':
                $ret['val'] = $this->getEntireConfig();
                break;
            case 'setConfig':
                $this->flushConfig();
                break;
            default:
                $ret['msg'] = 'Invalid op ' . $params['op'];
                $ret['status'] = 1;
                break;
        }

        return $ret;
    }
}
?>