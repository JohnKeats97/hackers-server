package hackers_server.authorization.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import hackers_server.authorization.views.LeaderboardView;
import hackers_server.authorization.views.UserView;
import java.sql.Array;

import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
@Service
public class JdbcUserService implements UserService {

    private static final RowMapper<UserView> READ_USER_MAPPER = (resultSet, rowNumber) ->
            new UserView(resultSet.getString("email"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"),
                    resultSet.getInt("isEmail"));

    private static final RowMapper<LeaderboardView> READ_USER_LOGIN_SCORE_MAPPER = (resultSet, rowNumber) ->
            new LeaderboardView(null, resultSet.getString("login"),
                    resultSet.getInt("score"));

    private static final RowMapper<Integer> READ_POSITION_MAPPER = (resultSet, rowNumber) ->
            resultSet.getInt("position");

    private JdbcTemplate template;

    public JdbcUserService(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void addUser(UserView user) {
        String sql = "INSERT INTO users (email, login, password) VALUES (?, ?, ?)";
        template.update(sql, user.getEmail(), user.getLogin(), user.getPassword());
    }

    @Override
        public UserView getByLoginOrEmail(String loginOrEmail) {
        String sql = "SELECT DISTINCT email, login, password, score, isEmail FROM users WHERE email = ? OR login = ?";
        return template.queryForObject(sql, READ_USER_MAPPER, loginOrEmail, loginOrEmail);
    }

    @Override
    public void changeUser(String user) {
        String sql = "UPDATE users SET isEmail = 1 WHERE login = ?";
        template.update(sql,user);
    }

    @Override
    public List<LeaderboardView> getLeaderboard(Integer limit) {
        String sql = "SELECT login, score FROM users ORDER BY score DESC, last_answer DESC, login LIMIT ?";
        return template.query(sql, ps -> ps.setInt(1, limit), READ_USER_LOGIN_SCORE_MAPPER);
    }

    @Override
    public UserView setScore(UserView user) {
        String sql = "UPDATE users SET score = ? WHERE login = ?";
        if (template.update(sql, user.getScore(), user.getLogin()) != 0) {
            return user;
        }
        return null;
    }

    @Override
    public Integer getPosition(UserView user) {
        String sql = "SELECT rat.position FROM  "
                + "( SELECT row_number() OVER(ORDER BY score DESC, login) AS position, login "
                + "FROM users) AS rat "
                + "WHERE rat.login = ?";
        return template.query(sql, ps -> ps.setString(1, user.getLogin()),
                READ_POSITION_MAPPER).get(0) + 1;
    }

    @Override
    public Integer[] getTestByLoginOrEmail(String loginOrEmail) {
        Array sql = template.queryForObject("SELECT tests FROM users WHERE email = ? "
                + "OR login = ?", Array.class, loginOrEmail, loginOrEmail);
        try {
            Integer[] tests = (Integer[]) sql.getArray();
            return tests;
        }catch (Exception e) {
            e.printStackTrace();
            return new Integer[] {};
        }
    }
}
