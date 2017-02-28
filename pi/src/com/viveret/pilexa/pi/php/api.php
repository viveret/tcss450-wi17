<?php
class PilexaApi
{
    CONST CONFIG_FILE = "pilexa-config.json";
    private $myConfig = null;

    public function __construct()
    {
        $this->myConfig = json_decode(file_get_contents(self::CONFIG_FILE), true);
        if (is_null($this->myConfig)) {
            echo json_last_error_msg();
        }
    }

    public function setConfig($key, $val)
    {
        $cur = $this->myConfig;
        $crumbs = explode('.', $key);
        for ($i = 0; $i < count($crumbs) - 1; $i++) {
            $cur = $cur[$crumbs[$i]];
        }
        $cur[$crumbs[count($crumbs) - 1]] = $val;
    }

    public function doThing($params)
    {
        $ret = array("msg" => "OK", "status" => 0);
        switch ($params['op']) {
            case 'canConnect':
                break;
//            case 'queryConfig':
//                if (isset($params['key'])) {
//                    $ret['val'] = $this->getConfig($params['key']);
//                } else {
//                    $ret['msg'] = 'Missing key';
//                    $ret['status'] = 1;
//                }
//                break;
//            case 'queryEntireConfig':
//                $ret['val'] = $this->getEntireConfig();
//                break;
//            case 'setConfig':
//                $this->flushConfig();
//                break;
//            case 'interpret':
//                $ret = $this->interpret($params['val']);
//                break;
            default:
                $ret = $this->sendToPilexa($params);
                break;
        }

        return $ret;
    }

    public function sendToPilexa($theJson)
    {
        $ret = array("msg" => "OK", "status" => 0);

        /* Get the IP address for the target host. */
        $address = gethostbyname('localhost');

        /* Create a TCP/IP socket. */
        $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
        if ($socket === false) {
            $ret["msg"] = "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
            $ret["status"] = 1;
        } else {
            socket_set_timeout($socket, 1);
            $result = socket_connect($socket, $address, $this->getConfig("server.port"));
            if ($result === false) {
                $ret['msg'] = "socket_connect() failed.\nReason: ($result) " . socket_strerror(socket_last_error($socket)) . "\n";
                $ret['status'] = 1;
            } else {
                $val = json_encode($theJson) . "\n";
                socket_write($socket, $val, strlen($val));
                $tmp = '';
                $out = '';
                while ($out = socket_read($socket, 2048)) {
                    $tmp .= $out;
                }
                socket_close($socket);
                $ret = json_decode($tmp);
            }
        }

        return $ret;
    }

    public function getConfig($key)
    {
        $cur = $this->myConfig;
        foreach (explode('.', $key) as $crumb) {
            $cur = $cur[$crumb];
        }
        return $cur;
    }

    public function getEntireConfig()
    {
        return $this->myConfig;
    }

    function flushConfig()
    {
        $myfile = fopen(self::CONFIG_FILE, "w") or die("Unable to open file!");
        fwrite($myfile, json_encode($this->myConfig));
        fclose($myfile);
    }
}
?>