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

    _data: list[str, ...]
       --except: time.parser -> list[str, dict]
    
    '''
    _comm = _api.split('.')
    if _comm[0] == "token":
        if _comm[1] == "verification" and len(_comm) == 2:
            from src.main.python.util.ssmToken import token_verification
            return str(token_verification(_data[0]))
        elif _comm[1] == "generate" and len(_comm) == 2:
            from src.main.python.util.ssmToken import token_generate
            return token_generate(_data[0], _data[1])
    elif _comm[0] == "recognition":
        if len(_comm) == 1:
            from src.main.python.util.SentenceRecognize import recognize
            return recognize(_data[0], _data[1])
    elif _comm[0] == "time":
        if _comm[1] == "parser" and len(_comm) == 2:
            from src.main.python.util.TimeParser import TimeParser
            TP = TimeParser(_data[0])
            return TP.parse_time(start_flag=_data[1].pop("start_flag", "<"),
                                 end_flag=_data[1].pop("end_flag", ">"),
                                 start_ignore_flag=_data[1].pop("start_ignore_flag", None),
                                 end_ignore_flag=_data[1].pop("end_ignore_flag", None))
    raise ApiNotExistError(_api)