import json
import socket


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = socket.gethostname()
# s.bind((host, 9738))
port = 11451
s.connect((host, port))
dataDic = {"api": "time.parser", "data": ["2015年10月3日去上学", {}]}
# dataDic = {"api": "token.verification", "data": ["eyJ0eXAiOiAiSldUIiwgImFsZyI6ICJIUzI1NiJ9.eyJpZCI6ICIxMTQ1MTQiLCAidXNyIjogIlRpYW5zdW8gTGkifQ.cQT_W9XYfx9BBIeBjxfOB_RM-h1qRGeQOKICnZzzVw"]}
m = json.dumps(dataDic).encode()
print(len(m))
lenData = {"len": len(m)}
s.sendto(json.dumps(lenData).encode(), (host, port))
s.sendto(m, (host, port))
msg = s.recv(1024)
s.close()
print("Close.")
print(msg.decode('utf-8'))
