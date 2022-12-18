package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.bean.exceptions.*;
import com.cmxz.snakesmartmemo.bean.exceptions.FileNotFoundException;
import com.cmxz.snakesmartmemo.dao.IdAndPasswordDao;
import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.IdAndPassword;
import com.cmxz.snakesmartmemo.pojo.User;
import com.cmxz.snakesmartmemo.util.QiniuKodoUtil;
import com.cmxz.snakesmartmemo.util.Tools;
import org.apache.ibatis.annotations.Mapper;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import java.lang.RuntimeException;
import java.net.URLEncoder;
import java.util.*;

@Service
public interface UserService {
    String echo(String id);

    Map<String, Object> register(String id, String userName, String password);

    Map<String, Object> login(String id, String password);

    Map<String, Object> upload(String id, String token, MultipartFile file, String filename);

    Map<String, Object> share(String id, String token, String filename);

    Map<String, Object> event(String id, String token,String text);

    Map<String, Object> recognize(String id, String token, MultipartFile file);
}

@Service
@Mapper
class UserServerImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdAndPasswordDao idAndPasswordDao;

    @Autowired
    private QiniuKodoUtil qiniuKodoUtil;

    @Autowired
    private Tools tools;

    String generateToken(String id, String userName) throws Exception {
        //生成token,data:["{id}","{userName}"]
        String data = "[\"" + id + "\",\"" + userName + "\"]";
        return tools.CallPythonTools("token.generate", data);
    }

    boolean verifyToken(String token) throws Exception{
        String data = "[\""+token+"\"]";
        return "true".equals(tools.CallPythonTools("token.verification",data));
    }
    public String echo(String id) {
        return id;
    }

    /**
     * 注册，后期密码可进行加密提高安全性
     *
     * @param id       唯一id（自动生成）
     * @param userName 用户名
     * @param password 密码存储需要加密
     * @return 返回注册是否成功
     */
    public Map<String, Object> register(String id, String userName, String password) {
        Map<String, Object> res = new HashMap<>();
        //新建User对象，并查找是否已存在
        User newUser = new User(userName, id);
        User uExist = userDao.getUserInfoById(id);

        IdAndPassword newIdAndPassword = new IdAndPassword(id, password, null);
//        IdAndPassword ipExist = idAndPasswordDao.getByIdAndPwd(newIdAndPassword);
        try {
            if (id == null || id.equals("")) {
                throw new NullOrEmptyIdException();
            }
            if (uExist != null) {

                throw new RuntimeException();
            }
            //将信息插入user_info表
            userDao.insert(newUser);

            //生成token,data:["{id}","{userName}"]
            String token = generateToken(id,userName);
            newIdAndPassword.setToken(token);

            //将信息插入id_and_passwords表
            idAndPasswordDao.insert(newIdAndPassword);
            //更新uExist信息
            uExist = userDao.getUserInfoById(id);
            res.put("statusMsg", "success");
            res.put("userInfo", uExist);
            res.put("token",token);
        } catch (NullOrEmptyIdException e) {
            res.put("statusMsg", "NullOrEmptyIdException");
        } catch (RuntimeException e) {
            res.put("statusMsg", "UserHasRegisterException");
        } catch (Exception e) {
            res.put("statusMsg", e.toString());
        }
        return res;
    }

    public Map<String, Object> login(String id, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            User exist = userDao.getUserInfoById(id);
            if (exist == null) {
                throw new UserNotFoundException();
            }
            IdAndPassword IdAndPwd = idAndPasswordDao.getByIdAndPwd(id, password);

            if (IdAndPwd == null) {
                throw new PasswdErrorException();
            }
            //生成token,data:["{id}","{userName}"]
            String token = generateToken(id,exist.getUsername());
            idAndPasswordDao.updateToken(id,token);

            response.put("statusMsg", "success");
            response.put("userInfo", exist);
            response.put("token",token);
        } catch (UserNotFoundException e) {
            response.put("statusMsg", "UserNotFoundException");
            response.put("userInfo", "");
        } catch (PasswdErrorException e) {
            response.put("statusMsg", "PasswdErrorException");
        }catch (Exception e) {
            response.put("statusMsg", e.toString());
        }
        return response;

    }

    /**
     * 将前端传来的需要上传保存到云端的文件保存至七牛云oss桶中
     *
     * @param id       用户id,将文件保存到../notes/{id}/目录下，id唯一
     * @param file     前端传来的需要保存的文件
     * @param filename 保存到oss桶的文件名
     * @return 返回保存文件是否成功, 不成功返回对应异常
     */
    public Map<String, Object> upload(String id, String token, MultipartFile file, String filename) {
        Map<String, Object> response = new HashMap<>();
        try {
            //获取token，token为空或前后端token不同为则过期，后期可完善token的相关业务逻辑
            if (!hasLogin(id, token)) {
                throw new TokenExpirationTimeException();
            }
            //根据id获取判断user是否存在
            User user = userDao.getUserInfoById(id);
            if (user == null) {
                throw new UserNotFoundException();
            }
            //判断文件是否为空
            if (file.isEmpty()) {
                throw new FileNotFoundException();
            }
            File f = new File(file.getOriginalFilename());
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(f));
            //System.out.println(file.getName());
            out.write(file.getBytes());
            out.flush();
            out.close();


            qiniuKodoUtil.upload(f, "notes/" + id + "/" + filename);

            response.put("statusMsg", "success");
            //这时候，系统会在根目录下创建一个临时文件，这个临时文件并不是我们需要的，所以文件处理完成之后，需要将其删除。
            File tem = new File(f.toURI());
            if (!f.delete())
                System.out.println("删除失败");

        } catch (UserNotFoundException e) {
            response.put("statusMsg", "UserNotFoundException");
        } catch (FileNotFoundException | java.io.FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (IOException e) {
            response.put("statusMsg", "WriteFileException");
        } catch (TokenExpirationTimeException e) {
            response.put("statusMsg", "TokenExpirationTimeException");
        }catch (Exception e) {
            response.put("statusMsg", e.toString());
        }
        return response;
    }

    /**
     * 文件共享，
     * 先检查云端是否存在改文件，若存在返回访问链接
     *
     * @param id       用于构建url和查询服务器token
     * @param token    token用于检测登录是否到期
     * @param fileName 指定文件名
     * @return 下载文件的链接
     */
    public Map<String, Object> share(String id, String token, String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            //获取token，token为空或前后端token不同为则过期，后期可完善token的相关业务逻辑
            if (!hasLogin(id, token)) {
                throw new TokenExpirationTimeException();
            }
            //查询是否存在文件，不存在则让其先上传
            if (!qiniuKodoUtil.inList(id, fileName)) {
                throw new FileNotFoundException();
            }
            String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
            String finalUrl = String.format("%s/notes/%s/%s", "http://" + qiniuKodoUtil.getDomain(), id, encodedFileName);
            response.put("fileUrl", finalUrl);
            response.put("statusMsg", "success");
        } catch (UnsupportedEncodingException e) {
            response.put("statusMsg", "UnsupportedEncodingException");
        } catch (FileNotFoundException e) {
            response.put("statusMsg", "CloudFileNotFoundException");
        } catch (TokenExpirationTimeException e) {
            response.put("statusMsg", "TokenExpirationTimeException");
        }catch (Exception e) {
            response.put("statusMsg", e.toString());
        }
        return response;
    }

    public boolean hasLogin(String id, String token) throws Exception{
        //获取token，token为空或前后端token不同为则过期
        IdAndPassword iAndP = idAndPasswordDao.getById(id);
        String serverToken = iAndP.getToken();
        if (serverToken == null || !serverToken.equals(token)) {
            return false;
        }
        //token过期或无效则返回false，验证通过返回true
        return verifyToken(token);
    }

    /**
     * 笔记内容生成事件接口
     *
     * @param id    用于查询服务器token
     * @param token token用于检测登录是否到期
     * @param file  需要处理的笔记文件
     * @return
     */
    public Map<String, Object> event(String id, String token, String text) {
        Map<String, Object> response = new HashMap<>();
        String comm = "time.parser";
        try {
            //将前端发过来的文件转为File
//            File f = new File(file.getOriginalFilename());
//            System.out.println(f.getName());
//
//            BufferedOutputStream out = new BufferedOutputStream(
//                    new FileOutputStream(f));
//            out.write(file.getBytes());
//            out.flush();
//            out.close();
//
//            //将File转化为字节数组
//            byte[] bytesArray = new byte[(int) f.length()];
//            FileInputStream fis = new FileInputStream(f);
//            fis.read(bytesArray); //read file into bytes[]
//            fis.close();
            byte[] bytesArray = text.getBytes();
            //调用CallPythonTools处理
            String data = "[\"" + new String(bytesArray) + "\",{}]";
            String events = tools.CallPythonTools(comm, data);
            response.put("statusMsg", "success");
            response.put("events", events);

            //这时候，系统会在根目录下创建一个临时文件，这个临时文件并不是我们需要的，所以文件处理完成之后，需要将其删除。
            //File tem = new File(f.toURI());
//            if (!f.delete())
//                System.out.println("删除失败");

        } catch (TokenExpirationTimeException e) {
            response.put("statusMsg", "TokenExpirationTimeException");
        } catch (java.io.FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (IOException e) {
            response.put("statusMsg", e.toString());
        } catch (Exception e) {
            response.put("statusMsg", e.toString());
        }

        return response;
    }

    /**
     * 音频文件转文字接口
     *
     * @param id    用于查询服务器token
     * @param token token用于检测登录是否到期
     * @param file  需要处理的音频文件
     * @return 音频转文字后的文字
     */
    public Map<String, Object> recognize(String id, String token, MultipartFile file) {
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

            //调用CallPythonTools处理
            String events = tools.CallPythonTools(comm, f.getAbsolutePath());
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
        } catch (Exception e) {
            response.put("statusMsg", e.toString());
        }

        return response;
    }
}