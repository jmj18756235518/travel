package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //首先获取验证码
            String check = req.getParameter("check");
            //获取会话中的验证码
            HttpSession session = req.getSession();
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
                //校验不通过，将错误信息反馈给页面
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("验证码错误");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resultInfo);
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().write(json);
                return;
            }
            //获取页面所有的参数
            Map<String, String[]> parameterMap = req.getParameterMap();
            User user = new User();
            BeanUtils.populate(user ,parameterMap);
            System.out.println(user);
            UserService userService = new UserServiceImpl();
            boolean flag = userService.register(user);
            ResultInfo resultInfo = new ResultInfo();
            resp.setContentType("text/html;charset=utf-8");
            if(flag == true){
                //用户名在数据库中不存在，注册成功
                resultInfo.setFlag(flag);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resultInfo);
                resp.getWriter().write(json);
            }else {
                resultInfo.setFlag(flag);
                resultInfo.setErrorMsg("用户名已经存在");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resultInfo);
                resp.getWriter().write(json);
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
