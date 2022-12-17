from configparser import ConfigParser


class ConfParser(ConfigParser):
    def __init__(self):
        super().__init__()
        self.read("config.ini", encoding="utf-8")
