import json
import re

from src.main.python.util.TimeParser import TimeParser


class EventParser:
    __TP = None

    def __init__(self, string_contains_time):
        self.__TP = TimeParser(string_contains_time, True)

    def parse_event(self):
        sentences = re.split("[,，。]", self.__TP.parse_time(start_flag="<TIME>", end_flag="<\\TIME>"))
        parse_res = []
        for s in sentences:
            index = s.find("<\\TIME>")
            if index != -1:
                parse_res.append(s[index + len("<\\TIME>"):])
        request_result = []
        for i in range(len(parse_res)):
            request_result.append({"time": self.__TP.time_list[i], "event": parse_res[i]})
        return json.dumps(request_result, ensure_ascii=False)
