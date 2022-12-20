package com.cmxz.snakesmartmemo.util;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
@Component
@Mapper
public class Tools {
    /**
     * 调用python工具
     * @param comm 要调用的api名称
     * @param data 调用api时传入的数据
     * @return 调用api返回的结果
     * @apiNote 形参comm与data格式:
     * <p>comm format: [module].[function]
     * <p>       module    ->    function
     * <p>       token     ->  verification, generate
     * <p>    recognition  ->
     * <p>        time     ->    parser
     * <p>        event    ->    parser
     * <p>
     * <p>data format: "["(String)", ...]"
     * <p>   --except: time.parser -> "["(String)", "{...}"]"
     */
    public static String CallPythonTools(String comm, String data) throws Exception{
        data = data.replace("\\", "\\\\");
        InetAddress addr = InetAddress.getLocalHost();
        String respText;
        try(DatagramSocket sock = new DatagramSocket(50310, addr)){
            sock.connect(addr, 11451);
            String requestData = "{\"api\":\"" + comm + "\",\"data\":" + data + "}";
            String requestLen = "{\"len\":" + requestData.getBytes(StandardCharsets.UTF_8).length + "}";
            DatagramPacket packet = new DatagramPacket(requestLen.getBytes(StandardCharsets.UTF_8),
                    requestLen.getBytes(StandardCharsets.UTF_8).length);
            sock.send(packet);
            packet = new DatagramPacket(requestData.getBytes(StandardCharsets.UTF_8),
                    requestData.getBytes(StandardCharsets.UTF_8).length);
            sock.send(packet);
            byte[] buffer = new byte[1024*1024];
            DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
            sock.receive(resp);
            byte[] respData = resp.getData();
            respText = new String(respData, 0, resp.getLength(), StandardCharsets.UTF_8);
        }
        return respText;
    }
}
