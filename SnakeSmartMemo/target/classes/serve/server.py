import json
import socket
import logging
from time import strftime

from src.main.python.util.ParseConfig import ConfParser
from src.main.python.util.ApiController import control


def start():
    # 根据配置初始化
    conf = ConfParser()
    server_port = conf.getint("server", "ServerPort")
    client_port = conf.getint("client", "ClientPort")

    LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
    LOG_PATH = conf.get("logging", "LogFilePath")[1:-1]
    LOG_FILE = "{}/{}.log".format(LOG_PATH, strftime("%Y%m%d-%H%M%S"))
    logging.basicConfig(filename=LOG_FILE, level=logging.DEBUG, format=LOG_FORMAT)

    # 创建服务器套接字
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    addr = socket.gethostname()
    server_socket.bind((addr, server_port))

    print(f"Start to listen on port {server_port}...")
    logging.info(f"Start to listen on port {server_port}...")

    while True:
        _dataGetRaw = None
        try:
            _isGet = False
            while not _isGet:
                try:
                    _isGet = True
                    _dataGetRaw = server_socket.recvfrom(1024)[0].decode()
                    logging.info("Accept to {}".format(addr))
                except ConnectionError:
                    _isGet = False
            logging.debug(f"Get data: {_dataGetRaw}")
            _dataGet = json.loads(_dataGetRaw, strict=False)
            _dataGetRaw = server_socket.recvfrom(_dataGet['len'])[0].decode()
            logging.debug(f"Get data: {_dataGetRaw}")
            _dataGet = json.loads(_dataGetRaw, strict=False)
            logging.info("Get requests from {}".format(addr))
            api = _dataGet["api"]
            data = _dataGet["data"]
            logging.info("Get request data successful. Now start loop to controller.")
            result = control(api, data)
            server_socket.sendto(result.encode(), (addr, client_port))

        # except TypeError:
        #     logging.warning("Type Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        # except json.decoder.JSONDecodeError:
        #     logging.warning("JSON Decode Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        # except KeyError:
        #     logging.warning("Key Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        # except ApiNotExistError as e:
        #     logging.warning(e)
        except Exception as e:
            logging.error("Unknown Error Occurred. Request from {}, ErrorInfo: {}".format(addr, e))
        finally:
            server_socket.sendto(b"Error!", (addr, client_port))
            logging.info("Close connect to {}".format((addr, client_port)))
