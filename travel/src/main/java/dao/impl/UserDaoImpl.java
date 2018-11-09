package dao.impl;

import dao.UserDao;
import domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findUsername(String username) {
        String sql = "select  count(*) from tab_user where username=?";
        Integer count = template.queryForObject(sql, Integer.class, username);
        return count;
    }


    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //2.执行sql

        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        );
    }

    /**
     * 激活用户，一步到位
     * @param code
     * @return
     */
    @Override
    public boolean activeUser(String code) {
        String sql = "select * from tab_user where code = ?";
        User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        if(user == null){
            return false;
        }
        String sql2 = "update tab_user set status = 'Y' where uid = ?";
        template.update(sql2,user.getUid());
        return true;
    }
}
