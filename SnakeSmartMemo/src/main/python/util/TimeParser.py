import re
import time
from functools import partial


class TimeParser:
    _string_contains_time = ""
    _time_list = []
    __now_id = 0

    def __init__(self, string_contains_time, default_time=True):
        """
        ------------------------------------
        :param string_contains_time: a string contains time
        :type string_contains_time: str
        :param default_time: True  -> use system time; False -> use ZERO
        :type default_time: bool
        """
        self._string_contains_time = string_contains_time
        if default_time:
            self._default_time = [int(ti) for ti in time.strftime("%Y-%m-%d-18-00").split('-')]
        else:
            self._default_time = [0, 0, 0, 0, 0]

    def set_string(self, new_string_contains_time):
        self._string_contains_time = new_string_contains_time

    def parse_time(self, **kwargs):
        """
        ------------------------------------
        return a specially processed string (like:  "ABC2020-1-1DEF"  -->  "ABC<a>2020-1-1</a>DEF")
        kwargs has keyword arguments: start_flag, end_flag, start_ignore_flag, end_ignore_flag

        """
        '''
         time symbol:
        # h: hour; m: minute; s: second; Y: year; M: month; D: day;
        ------------------------------------
         time format:
        # YYYY+MM+DD+hh+mm
        # YYYY+MM+DD
        # YYYY+MM
        # MM+DD
        # hh+mm
        # MM+DD+hh+mm
        ------------------------------------
         regex compare pattern:
        # pattern 1  --> YYYY年MM月DD日hh[时/点]mm分
        # pattern 2  --> YYYY-MM-DD-hh:mm
        
        '''
        autoincrement = kwargs.pop("autoincrement", False)
        if autoincrement == "True":
            autoincrement = True
            self.__now_id = kwargs.pop("start_id", 0)
        else:
            autoincrement = False

        pattern = "((\\d{4}-\\d{1,2}-\\d{1,2})?(\\d{4}-\\d{1,2})?(\\d{1,2}-\\d{1,2})?([- ]?\\d{1,2}:\\d{1," \
                  "2})?)?((\\d{4}年\\d{1,2}月\\d{1,2}日)?(\\d{4}年\\d{1,2}月)?(\\d{1,2}月\\d{1,2}日)?(\\d{1,2}" \
                  "[时点]\\d{1,2}分)?(\\d{1,2}[时点])?)?"
        if "start_ignore_flag" in kwargs.keys() and kwargs["start_ignore_flag"] is not None:
            pattern = "(?!{})".format(kwargs.pop("start_ignore_flag")) + pattern
        if "end_ignore_flag" in kwargs.keys() and kwargs["end_ignore_flag"] is not None:
            pattern = pattern + "(?!{})".format(kwargs.pop("end_ignore_flag"))

        new_high_lighter = partial(self.__time_high_light, start_flag=kwargs.pop("start_flag", "<"),
                                   end_flag=kwargs.pop("end_flag", ">"),
                                   autoincrement=autoincrement)
        processed_time_string = re.sub(pattern, new_high_lighter, self._string_contains_time)
        return processed_time_string

    def __time_high_light(self, matched_time_object, start_flag='<', end_flag='>', autoincrement=False):
        """
        adding flags to the time string
        :param matched_time_object: a re.Match object ONLY contains time
        :type matched_time_object: re.Match
        :param start_flag: the flag at the FRONT of the time string
        :type start_flag: str
        :param end_flag: the flag at the END of the time string
        :type end_flag: str
        :param autoincrement: is ID needed autoincrement
        :type autoincrement: bool
        :return: a precessed string contains flags
        """
        matched_time_string = matched_time_object.group()
        # 合法判断
        is_head_legal = False
        is_back_legal = False

        # 前后合法判断
        if matched_time_string == '':
            return ''
        else:
            head = matched_time_object.span()[0]
            back = matched_time_object.span()[1]
            # 头判断
            if head == 0 or (not self._string_contains_time[head - 1].isnumeric()
                             and self._string_contains_time[head - 1] != ':'
                             and self._string_contains_time[head - 1] != '-'
                             and self._string_contains_time[head - 1] != '年'
                             and self._string_contains_time[head - 1] != '月'
                             and self._string_contains_time[head - 1] != '日'
                             and self._string_contains_time[head - 1] != '时'
                             and self._string_contains_time[head - 1] != '点'
                             and self._string_contains_time[head - 1] != '分'):
                is_head_legal = True
            else:
                is_head_legal = False

            # 尾判断
            if back == len(self._string_contains_time) or (not self._string_contains_time[back].isnumeric()
                                                            and self._string_contains_time[back] != ':'
                                                            and self._string_contains_time[back] != '-'
                                                            and self._string_contains_time[back] != '年'
                                                            and self._string_contains_time[back] != '月'
                                                            and self._string_contains_time[back] != '日'
                                                            and self._string_contains_time[back] != '时'
                                                            and self._string_contains_time[back] != '点'
                                                            and self._string_contains_time[back] != '分'):
                is_back_legal = True
            else:
                is_back_legal = False

        # 时间合法判断
        is_time_legal = self.__time_is_legal(matched_time_string)
        if is_head_legal and is_back_legal and is_time_legal:
            if autoincrement:
                start_flag = start_flag.replace("%ID", str(self.__now_id))
                self.__now_id += 1
            return start_flag + matched_time_string + end_flag
        else:
            return matched_time_string

    def __time_is_legal(self, t_string):
        format_time_list = self._default_time[:]
        search_list = re.findall("(?:(\\d{4}-\\d{1,2}-\\d{1,2})?(\\d{4}-\\d{1,2})?(\\d{1,2}-\\d{1,2})?([- ]?\\d{1,2}:"
                                 "\\d{1,2})?)?(?:(\\d{4}年\\d{1,2}月\\d{1,2}日)?(\\d{4}年\\d{1,2}月)?(\\d{1,2}月\\d{1,2}"
                                 "日)?(\\d{1,2}[时点]\\d{1,2}分)?(\\d{1,2}[时点])?)?", t_string)[0]

        # time string --> time list
        # eg. "2022-10-11-12:00" --> [2022, 10, 11, 12, 0]
        if ':' in t_string or '-' in t_string:
            # EN type
            # list index: 0 ~ 3
            if search_list[0] != '':
                format_time_list[0] = int(search_list[0].split('-')[0])
                format_time_list[1] = int(search_list[0].split('-')[1])
                format_time_list[2] = int(search_list[0].split('-')[2])
            elif search_list[1] != '':
                format_time_list[0] = int(search_list[1].split('-')[0])
                format_time_list[1] = int(search_list[1].split('-')[1])
            elif search_list[2] != '':
                format_time_list[1] = int(search_list[2].split('-')[0])
                format_time_list[2] = int(search_list[2].split('-')[1])
            if search_list[3] != '':
                temp_str = search_list[3][1:]
                format_time_list[3] = int(temp_str.split(':')[0])
                format_time_list[4] = int(temp_str.split(':')[1])
            else:
                pass

        else:
            # ZH type
            # list index: 4 ~ 8
            if search_list[4] != '':
                temp_str = search_list[4]
                format_time_list[0] = int(temp_str.split('年')[0])
                temp_str = temp_str.split('年')[1]
                format_time_list[1] = int(temp_str.split('月')[0])
                temp_str = temp_str.split('月')[1]
                format_time_list[2] = int(temp_str.split('日')[0])
            elif search_list[5] != '':
                temp_str = search_list[5]
                format_time_list[0] = int(temp_str.split('年')[0])
                temp_str = temp_str.split('年')[1]
                format_time_list[1] = int(temp_str.split('月')[0])
            elif search_list[6] != '':
                temp_str = search_list[6]
                format_time_list[1] = int(temp_str.split('月')[0])
                temp_str = temp_str.split('月')[1]
                format_time_list[2] = int(temp_str.split('日')[0])
            if search_list[7] != '':
                temp_str = re.split("[时点分]", search_list[7])
                format_time_list[3] = int(temp_str[0])
                format_time_list[4] = int(temp_str[1])
            elif search_list[8] != '':
                format_time_list[3] = int(search_list[8][0:-1])

        # time judge
        # month
        if format_time_list[1] <= 0 or format_time_list[1] > 12:
            return False
        # day
        if format_time_list[2] <= 0:
            return False
        elif format_time_list[1] in [1, 3, 5, 7, 8, 10, 12] and format_time_list[2] > 31:
            return False
        elif format_time_list[1] in [4, 6, 9, 11] and format_time_list[2] > 30:
            return False
        elif format_time_list[1] == 2:
            if (format_time_list[1] % 100 != 0 and format_time_list[1] % 4 == 0 or format_time_list[1] % 400 == 0) \
                    and format_time_list[2] > 29:
                return False
            elif format_time_list[2] > 28:
                return False
            else:
                return False
        # hour
        if format_time_list[3] < 0 or format_time_list[3] > 24:
            return False
        # minute
        if format_time_list[4] < 0 or format_time_list[4] >= 60:
            return False
        self._time_list.append(format_time_list)
        return True
