import base64
import hmac
import json


def token_generate(_id, _usr):
    header = {'typ': 'JWT', 'alg': 'HS256'}
    h_s = base64.urlsafe_b64encode(json.dumps(header).encode()).replace(b'=', b'')
    payload = {'id': _id, 'usr': _usr}
    p_s = base64.urlsafe_b64encode(json.dumps(payload).encode()).replace(b'=', b'')

    temp = h_s + b'.' + p_s
    temp_hash = hmac.new(b"ssm", temp, digestmod="SHA256")
    signature = base64.urlsafe_b64encode(temp_hash.digest()).replace(b'=', b'')

    jwt_token = (h_s + b'.' + p_s + b'.' + signature).decode()
    return jwt_token


def token_verification(_token):
    t = _token.split('.')
    h_s = t[0].encode()
    p_s = t[1].encode()
    temp = h_s + b'.' + p_s
    temp_hash = hmac.new(b"ssm", temp, digestmod="SHA256")
    signature = base64.urlsafe_b64encode(temp_hash.digest()).replace(b'=', b'')
    return t[2].encode() == signature


if __name__ == '__main__':
    print("id: 114514")
    print("name: Tiansuo Li")
    token = token_generate("114514", "Tiansuo Li")
    print(token)
    print(token_verification(token))
    print(token_verification(token[:-1] + '0'))
