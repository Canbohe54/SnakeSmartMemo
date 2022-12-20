package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.bean.exceptions.TokenExpirationTimeException;
import com.cmxz.snakesmartmemo.pojo.Event;
import com.cmxz.snakesmartmemo.util.Tools;
import com.google.gson.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.cmxz.snakesmartmemo.util.Tools.CallPythonTools;

@Service
public interface VisitorService {
    Map<String, Object> event(String text);

    Map<String, Object> recognize(MultipartFile file);
}
@Service
@Mapper
class VisitorServiceImpl implements VisitorService{

    public Map<String, Object> event(String text){
        Map<String, Object> response = new HashMap<>();
        String comm = "time.parser";
        try {
            byte[] bytesArray = text.getBytes();
            //调用CallPythonTools处理
            String data = "[\"" + new String(bytesArray) + "\",{\"start_flag\":\"<a herf='#events_%ID'>\",\"end_flag\":\"</a>\",\"autoincrement\":\"True\",\"start_id\":0}]";
            String eventHeightLight = Tools.CallPythonTools(comm, data);
            System.out.println("time.parser "+eventHeightLight);
            comm = "event.parser";
            String events = CallPythonTools(comm, data);
            System.out.println("event.parser "+events);
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(events).getAsJsonArray();
            System.out.println(Jarray);
            ArrayList<Event> resEvent = new ArrayList<>();
            long eventId = 0;
            for (JsonElement jsonElement : Jarray) {

                JsonObject object = jsonElement.getAsJsonObject();
                JsonArray timeArr = object.getAsJsonArray("time");
                Event newEvent = new Event();
                newEvent.setTime(new ArrayList<>());
                newEvent.setEventId(eventId++);
                for (JsonElement timeElement : timeArr) {
                    newEvent.getTime().add(Integer.valueOf(timeElement.getAsString()));
                }
                newEvent.setEvent(object.get("event").getAsString());
                resEvent.add(newEvent);
            }

            response.put("statusMsg", "success");
            //处理好的文本
            response.put("events", eventHeightLight);
            //事件列表
            response.put("eventList", resEvent);
            resEvent = null;
        } catch (TokenExpirationTimeException e) {
            response.put("statusMsg", "TokenExpirationTimeException");
        } catch (java.io.FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (IOException e) {
            response.put("statusMsg", e.toString());
        }catch (Exception e) {
            response.put("statusMsg", e.toString());
        }

        return response;
    }

    public Map<String, Object> recognize(MultipartFile file){
        Map<String, Object> response = new HashMap<>();
        String comm = "recognition";
        try {
            //将前端发过来的文件转为File
            File f = new File(file.getOriginalFilename());
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(f));
            out.write(file.getBytes());
            out.flush();
            out.close();

            //将File转化为字节数组
//            byte[] bytesArray = new byte[(int) f.length()];
//            FileInputStream fis = new FileInputStream(f);
//            fis.read(bytesArray); //read file into bytes[]
//            fis.close();

            //调用CallPythonTools处理
            String data = "[\"" + f.getAbsolutePath() + "\"]";
            String events = CallPythonTools(comm, data);
            response.put("statusMsg", "success");
            response.put("events", events);

            //这时候，系统会在根目录下创建一个临时文件，这个临时文件并不是我们需要的，所以文件处理完成之后，需要将其删除。
            if (!f.delete())
                System.out.println("删除失败");

        } catch (TokenExpirationTimeException e) {
            throw new RuntimeException(e);
        } catch (java.io.FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (IOException e) {
            response.put("statusMsg", e.toString());
        }catch (Exception e) {
            response.put("statusMsg", e.toString());
        }

        return response;
    }

}
