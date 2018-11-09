package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.User;
import service.UserService;
import util.MailUtils;
import util.UuidUtil;
import web.servlet.RegisterUserServlet;

public class UserServiceImpl implements UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //首先去数据库中查询用户名是否存在
        UserDao userDao = new UserDaoImpl();
        int count = userDao.findUsername(user.getUsername());
        if(count == 1){
            //用户名已经存在
            return false;
        }
        //生成用户激活码
        user.setCode(UuidUtil.getUuid());
        //发送激活码到邮箱里面

        String content = "<a href='http://localhost/travel/activeUserServlet?code=" + user.getCode() + "'>点击激活【**旅游网】</a>";
        //发送邮件
        MailUtils.sendMail(user.getEmail(),content,"测试邮件");
        //设置账号激活状态
        user.setStatus("N");
        userDao.save(user);
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        UserDao userDao = new UserDaoImpl();
        boolean flag = userDao.activeUser(code);
        return flag;

    }
}
