import json
import socket
import logging
from time import strftime

from src.main.python.util.ParseConfig import ConfParser
from src.main.python.util.ApiController import control, ApiNotExistError


def start():
    # 根据配置初始化
    conf = ConfParser()
    server_port = conf.getint("server", "ServerPort")
    max_listen_num = conf.getint("server", "MaxListenNum")

    LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
    LOG_PATH = conf.get("logging", "LogFilePath")[1:-1]
    LOG_FILE = "{}/{}.log".format(LOG_PATH, strftime("%Y%m%d-%H%M%S"))
    logging.basicConfig(filename=LOG_FILE, level=logging.DEBUG, format=LOG_FORMAT)

    # 创建服务器套接字
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    addr = socket.gethostname()
    server_socket.bind((addr, server_port))

    print("Start to listen on port 11451...")

    while True:
        logging.debug("Accept to {}".format(addr))
        _dataGetRaw = server_socket.recvfrom(1024)[0].decode()
        try:
            _dataGet = json.loads(_dataGetRaw)
            _dataGetRaw = server_socket.recvfrom(_dataGet['len'])[0].decode()
            _dataGet = json.loads(_dataGetRaw)
            logging.debug("Get requests from {}".format(addr))
            api = _dataGet["api"]
            data = _dataGet["data"]
            logging.debug("Get request data successful. Now start loop to controller.")
            result = control(api, data)
            server_socket.sendto(result.encode(), (addr, 50310))

        except TypeError as e:
            logging.warning("Type Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        except json.decoder.JSONDecodeError as e:
            logging.warning("JSON Decode Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        except KeyError as e:
            logging.warning("Key Error. Request from {}. RequestInfo: {}".format(addr, _dataGetRaw))
        except ApiNotExistError as e:
            logging.warning(e)
        except Exception as e:
            logging.error("Unknown Error Occurred. Request from {}, ErrorInfo: {}".format(addr, e))
        finally:
            server_socket.sendto(b"Error!", (addr, 50310))
            logging.debug("Close connect to {}".format((addr, 50310)))
