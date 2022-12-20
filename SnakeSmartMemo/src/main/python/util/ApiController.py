import logging


class ApiNotExistError(Exception):
    def __init__(self, apiName):
        if apiName is not None:
            super(ApiNotExistError, self).__init__("api \"{}\" is not existed.".format(apiName))
        else:
            super(ApiNotExistError, self).__init__("api is not existed.")


def control(_api, _data):
    """
    api controller

    ----------------------
    :type _api: str
    :type _data: list
    """
    '''
    _api format: [module].[function]
           module    ->    function
           token     ->  verification, generate
        recognition  ->     NONE
            time     ->    parser
            event    ->    parser

    _data: list[str, ...]
       --except: time.parser -> list[str, dict]
    
    '''
    _comm = _api.split('.')
    if _comm[0] == "token":
        if _comm[1] == "verification" and len(_comm) == 2:
            logging.info("token.verification")
            from src.main.python.util.ssmToken import token_verification
            return str(token_verification(_data[0]))
        elif _comm[1] == "generate" and len(_comm) == 2:
            logging.info("token.generate")
            from src.main.python.util.ssmToken import token_generate
            return token_generate(_data[0], _data[1])
    elif _comm[0] == "recognition":
        if len(_comm) == 1:
            logging.info("recognition")
            from src.main.python.util.SentenceRecognize import recognize
            _audio = open(_data[0], "rb").read()
            _format = _data[0].split("\\")[-1].split('.')[-1]
            return recognize(_audio, _format)
    elif _comm[0] == "time":
        if _comm[1] == "parser" and len(_comm) == 2:
            logging.info("time.parser")
            from src.main.python.util.TimeParser import TimeParser
            TP = TimeParser(_data[0], False)
            res = TP.parse_time(start_flag=_data[1].pop("start_flag", "<"),
                                end_flag=_data[1].pop("end_flag", ">"),
                                start_ignore_flag=_data[1].pop("start_ignore_flag", None),
                                end_ignore_flag=_data[1].pop("end_ignore_flag", None),
                                autoincrement=_data[1].pop("autoincrement", "False"),
                                start_id=_data[1].pop("start_id", 0))
            del TP
            return res
    elif _comm[0] == "event":
        if _comm[1] == "parser" and len(_comm) == 2:
            logging.info("event.parser")
            from src.main.python.util.EventParser import EventParser
            EP = EventParser(_data[0])
            res = EP.parse_event()
            del EP
            return res
    raise ApiNotExistError(_api)
