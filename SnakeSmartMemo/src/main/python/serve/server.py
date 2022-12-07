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
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    addr = socket.gethostname()
    server_socket.bind((addr, server_port))
    server_socket.listen(max_listen_num)

    while True:
        client_socket, addr = server_socket.accept()
        logging.debug("Accept to {}".format(addr))
        try:
            _dataGet = client_socket.recvfrom(16)[0].decode()
            logging.debug("Get data length from {}".format(addr))
            _dataGet = json.loads(_dataGet)
            _dataGet = json.loads(client_socket.recvfrom(_dataGet["len"])[0].decode())
            logging.debug("Get requests from {}".format(addr))
            api = _dataGet["api"]
            data = _dataGet["data"]
            logging.debug("Get request data successful. Now start loop to controller.")
            result = control(api, data)
            client_socket.sendto(result.encode(), addr)

        except TypeError as e:
            print(e)
            logging.warning("Type Error.")
        except json.decoder.JSONDecodeError as e:
            print(e)
            logging.warning("JSON Decode Error.")
        except KeyError as e:
            print(e)
            logging.warning("Key Error.")
        except ApiNotExistError as e:
            logging.warning(e)
        except Exception as e:
            logging.error("Unknown Error Occurred. ErrorInfo: {}".format(e))
        finally:
            client_socket.sendto(b"Error.", addr)
            logging.debug("Close connect to {}".format(addr))
            client_socket.close()
