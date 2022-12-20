import base64
import requests
import hashlib
import hmac
import json
import time
from datetime import datetime
import sys

from src.main.python.resources import tk

sys.path.append("../../../")

def get_authorization(params):
    """
    Calculate the authorization of tencent cloud api
    :param params: data needed to be posted
    :type params: dict
    :return: an authorization string
    """
    secret_id = tk.secret_id
    secret_key = tk.secret_key
    service = "asr"
    host = "asr.tencentcloudapi.com"
    algorithm = "TC3-HMAC-SHA256"
    timestamp = int(time.time())
    date = datetime.utcfromtimestamp(timestamp).strftime("%Y-%m-%d")
    http_request_method = "POST"
    canonical_uri = "/"
    canonical_querystring = ""
    ct = "application/json"
    payload = json.dumps(params)
    canonical_headers = "content-type:%s\nhost:%s\n" % (ct, host)
    signed_headers = "content-type;host"
    hashed_request_payload = hashlib.sha256(payload.encode("utf-8")).hexdigest()
    canonical_request = (http_request_method + "\n" +
                         canonical_uri + "\n" +
                         canonical_querystring + "\n" +
                         canonical_headers + "\n" +
                         signed_headers + "\n" +
                         hashed_request_payload)
    credential_scope = date + "/" + service + "/" + "tc3_request"
    hashed_canonical_request = hashlib.sha256(canonical_request.encode("utf-8")).hexdigest()
    string_to_sign = (algorithm + "\n" +
                      str(timestamp) + "\n" +
                      credential_scope + "\n" +
                      hashed_canonical_request)

    def sign(key, msg):
        return hmac.new(key, msg.encode("utf-8"), hashlib.sha256).digest()

    secret_date = sign(("TC3" + secret_key).encode("utf-8"), date)
    secret_service = sign(secret_date, service)
    secret_signing = sign(secret_service, "tc3_request")
    signature = hmac.new(secret_signing, string_to_sign.encode("utf-8"), hashlib.sha256).hexdigest()
    authorization = (algorithm + " " +
                     "Credential=" + secret_id + "/" + credential_scope + ", " +
                     "SignedHeaders=" + signed_headers + ", " +
                     "Signature=" + signature)
    return authorization


def recognize(original_data, voice_format, voice_language="16k_zh"):
    """
    Sentence Recognition
    :param original_data: voice bytes data
    :type original_data: bytes
    :param voice_format: format of the voice data
    :type voice_format: str
    :param voice_language: the language of the voice file
    :type voice_language: str
    :return: a response string contains recognition result
    """
    data_base64 = base64.b64encode(original_data).decode()
    post_url = "https://asr.tencentcloudapi.com"
    post_data = {
        "ProjectId": 0,
        "SubServiceType": 2,
        "EngSerViceType": voice_language,
        "SourceType": 1,
        "VoiceFormat": voice_format,
        "UsrAudioKey": "audio",
        "Data": data_base64
    }
    post_headers = {
        "Host": "asr.tencentcloudapi.com",
        "Content-Type": "application/json",
        "X-TC-Timestamp": str(int(time.time())),
        "X-TC-Action": "SentenceRecognition",
        "X-TC-Version": "2019-06-14",
        "Authorization": get_authorization(post_data)
    }
    recognition_res = requests.post(post_url, headers=post_headers, data=json.dumps(post_data))
    res = json.loads(recognition_res.text)
    return res['Response']['Result']

