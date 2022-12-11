package com.cmxz.snakesmartmemo.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Tools {
    public static String CallPythonTools(String comm, String data) throws Exception{
        InetAddress addr = InetAddress.getLocalHost();
        String respText;
        System.out.println(addr.getHostAddress());
        try(DatagramSocket sock = new DatagramSocket(50310, addr)){
            sock.connect(addr, 11451);
            System.out.println(sock.getLocalSocketAddress());
            System.out.println(sock.getRemoteSocketAddress());
            String requestData = "{'api':" + comm + ",'data':" + data + "}";
            System.out.println(requestData);
            DatagramPacket packet = new DatagramPacket(requestData.getBytes(StandardCharsets.UTF_8),
                    requestData.getBytes(StandardCharsets.UTF_8).length);
            sock.send(packet);
            System.out.println("The data was send to server.");
            System.out.print(packet.getAddress().getHostAddress());
            System.out.println("  " + packet.getPort());
            byte[] buffer = new byte[1024];
            DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
            sock.receive(resp);
            byte[] respData = resp.getData();
            respText = new String(respData, 0, resp.getLength(), StandardCharsets.UTF_8);
            System.out.println(respText);
        }

        return respText;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(CallPythonTools("time.parser", "[\"2015年10月3日去上学\", {}]"));
//        try{
//            System.out.println(CallPythonTools("time.parser", "[\"2015年10月3日去上学\", {}]"));
//        }
//        catch (Exception e){
//            System.out.println("Error. " + e.getMessage());
//        }

    }
}
