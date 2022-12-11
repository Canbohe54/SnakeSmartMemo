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
    public static String CallPythonTools(String comm, String data) throws Exception{
        InetAddress addr = InetAddress.getLocalHost();
        String respText;
        try(DatagramSocket sock = new DatagramSocket(50310, addr)){
            sock.connect(addr, 11451);
            String requestData = "{\"api\":" + comm + ",\"data\":" + data + "}";
            String requestLen = "{\"len\":" + requestData.getBytes(StandardCharsets.UTF_8).length + "}";
            System.out.println(requestLen);
            System.out.println(requestData);
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

    public static void main(String[] args) throws Exception{
        System.out.println(CallPythonTools("\"time.parser\"", "[\"2015年10月3日去上学\", {}]"));
    }
}
