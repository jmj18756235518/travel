package web.servlet;

import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取激活码
        String code = req.getParameter("code");
        //通过此激活码去找到service层
        UserService userService = new UserServiceImpl();
        boolean flag = userService.active(code);
        resp.setContentType("text/html;charset=utf-8");
        String msg = null;
        if(flag){
            //激活成功
            msg = "激活成功，您现在可以去<a href='login.html'>登录</a>";
        }else {
            msg = "激活失败，请联系管理员";
        }
        resp.getWriter().write(msg);

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
