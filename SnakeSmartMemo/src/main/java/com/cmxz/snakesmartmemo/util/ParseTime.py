import re
from functools import partial


def parse_time(string_contains_time, **kwargs):
    """
    find the time in the string and process
    :param string_contains_time: a string contains time
    :type string_contains_time: str
    :return: a specially processed string (like:  "ABC2020.1.1DEF"  -->  "ABC<a>2020.1.1</a>DEF")
    """
    '''
    regex compare pattern:
    # h: hour; m: minute; s: second; Y: year; M: month; D: day;
    # pattern 1  --> hh时 or hh点
    # pattern 2  --> hh:mm or hh时mm分 or hh点mm分
    # pattern 3  --> hh:mm:ss or hh时mm分ss秒
    # pattern 4  --> MM-DD or MM.DD or MM月DD日
    # pattern 5  --> YY:MM:DD or YY-MM-DD or YY.MM.DD or YYYY:MM:DD or YYYY-MM-DD or YYYY.MM.DD
    #                YY年MM月DD日 or YYYY年MM月DD日
    # pattern 6  --> pattern 4 + (-|space) + pattern 1
    # pattern 7  --> pattern 4 + (-|space) + pattern 2
    # pattern 8  --> pattern 4 + (-|space) + pattern 3
    # pattern 9  --> pattern 5 + (-|space) + pattern 1
    # pattern 10 --> pattern 5 + (-|space) + pattern 2
    # pattern 11 --> pattern 5 + (-|space) + pattern 3
    '''
    new_high_lighter = partial(time_high_light, start_flag=kwargs.pop("start_flag", "<"), end_flag=kwargs.pop("end_flag", ">"))
    pattern = "(?:\\d{2,4}(?:[.年]|-)){0,1}(?:\\d{1,2}(?:[.月]|-)){0,1}(?:\\d{1,2}(?:[.日号]|-)(?:凌晨|上午|下午|晚上){0,1}){0,1}(?:[ ]?\\d{1,2}[:：时点]){0,1}(?:\\d{1,2}[:：分]?){0,1}(?:\\d{1,2}[秒]){0,1}[ ]?(?:(?:[ap][.]{0,1}[m][.]{0,1})|(?:[AP][.]{0,1}[M][.]{0,1}){0,1})"
    processed_time_string = re.sub(pattern, new_high_lighter, string_contains_time)
    return processed_time_string


def time_high_light(matched_time_object, start_flag='<', end_flag='>'):
    """
    adding flags to the time string
    :param matched_time_object: a re.Match object ONLY contains time
    :type matched_time_object: re.Match
    :param start_flag: the flag at the FRONT of the time string
    :type start_flag: str
    :param end_flag: the flag at the END of the time string
    :type end_flag: str
    :return: a precessed string contains flags
    """
    # print(matched_time_object, end=':')
    matched_time_string = matched_time_object.group().rstrip(' ')
    # print(matched_time_string == '', end=';\n')
    if matched_time_string != '' and time_is_legal(matched_time_string):
        return start_flag + matched_time_string + end_flag + (len(matched_time_object.group()) - len(matched_time_string)) * ' '
    else:
        return matched_time_string


def time_is_legal(t_string):
    if t_string == ' ' or t_string.isnumeric() or isfloat(t_string):
        return False
    return True


def isfloat(num_string):
    if len(num_string.split(".")) == 2 and num_string.split(".")[0].isnumeric():
        return True
    else:
        return False
