import json
import re

from src.main.python.util.TimeParser import TimeParser


class EventParser(TimeParser):
    def __init__(self, string_contains_time):
        super().__init__(string_contains_time, True)

    def parse_event(self):
        sentences = re.split("[,，。]", super().parse_time(start_flag="<TIME>", end_flag="<\\TIME>"))
        parse_res = []
        for s in sentences:
            index = s.find("<\\TIME>")
            if index != -1:
                parse_res.append(s[index + len("<\\TIME>"):])
        request_result = []
        for i in range(len(parse_res)):
            request_result.append({"time": self._time_list[i], "event": parse_res[i]})
        return json.dumps(request_result, ensure_ascii=False)
