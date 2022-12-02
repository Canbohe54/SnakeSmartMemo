package com.cmxz.snakesmartmemo.service;

import com.cmxz.snakesmartmemo.bean.exceptions.FileNotFoundException;
import com.cmxz.snakesmartmemo.bean.exceptions.PermissionException;
import com.cmxz.snakesmartmemo.bean.exceptions.TokenExpirationTimeException;
import com.cmxz.snakesmartmemo.bean.exceptions.UserNotFoundException;
import com.cmxz.snakesmartmemo.dao.IdAndPasswordDao;
import com.cmxz.snakesmartmemo.dao.UserDao;
import com.cmxz.snakesmartmemo.pojo.IdAndPassword;
import com.cmxz.snakesmartmemo.pojo.User;
import com.cmxz.snakesmartmemo.util.QiniuKodoUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public interface UserService {
    String echo(String id);

    String register(String id, String userName, String password);

    Map<String, Object> login(String id, String password);

    Map<String, Object> upload(String id, MultipartFile file, String filename);

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

    public String echo(String id) {
        return id;
    }

    /**
     * 注册
     *
     * @param id       唯一id（自动生成）
     * @param userName 用户名
     * @param password 密码存储需要加密
     * @return 返回注册是否成功
     */
    public String register(String id, String userName, String password) {
        //新建User对象，并查找是否已存在
        User newUser = new User(userName, id);
        User uExist = userDao.getUserInfoById(id);

        IdAndPassword newIdAndPassword = new IdAndPassword(id, password, null);
//        IdAndPassword ipExist = idAndPasswordDao.getByIdAndPwd(newIdAndPassword);
        if (uExist != null) {
            return "user has been exist";
        }

        //将信息插入user_info，id_and_passwords表
        userDao.insert(newUser);
        idAndPasswordDao.insert(newIdAndPassword);
        return "register successfully";
    }

    public Map<String, Object> login(String id, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            User exist = userDao.getUserInfoById(id);
            if (exist == null) {
                throw new UserNotFoundException();
            }
            response.put("statusMsg", "success");
            response.put("userInfo", exist);
            return response;
        } catch (UserNotFoundException e) {
            response.put("statusMsg", "UserNotFoundException");
            response.put("userInfo", "");
            return response;
        }
    }

    /**
     * 将前端传来的需要上传保存到云端的文件保存至七牛云oss桶中
     *
     * @param id       用户id,将文件保存到../notes/{id}/目录下，id唯一
     * @param file     前端传来的需要保存的文件
     * @param filename 保存到oss桶的文件名
     * @return 返回保存文件是否成功, 不成功返回对应异常
     */
    public Map<String, Object> upload(String id, MultipartFile file, String filename) {
        Map<String, Object> response = new HashMap<>();
        try {
            //获取token，token为空则过期，后期可完善token的相关业务逻辑
            IdAndPassword iAndP = idAndPasswordDao.getById(id);
            if (iAndP.getToken() == null) {
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

            response.put("statusMsg", "successfully upload " + filename);
            //这时候，系统会在根目录下创建一个临时文件，这个临时文件并不是我们需要的，所以文件处理完成之后，需要将其删除。
            File tem = new File(f.toURI());
            if (f.delete()){
                System.out.println("删除成功");
            }else {
                System.out.println("删除失败");
            }

        } catch (UserNotFoundException e) {
            response.put("statusMsg", "UserNotFoundException");
        } catch (FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (TokenExpirationTimeException e) {
            response.put("statusMsg", "TokenExpirationTimeException");
        } catch (java.io.FileNotFoundException e) {
            response.put("statusMsg", "FileNotFoundException");
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
        return response;
    }

}
