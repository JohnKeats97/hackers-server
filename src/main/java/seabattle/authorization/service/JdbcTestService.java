package seabattle.authorization.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import seabattle.authorization.views.TestView;

import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
@Service
public class JdbcTestService implements TestService {

    private static final RowMapper<TestView> READ_TEST_ADMIN_MAPPER = (resultSet, rowNumber) ->
            new TestView(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("text"),
                    resultSet.getString("answer"));

    private static final RowMapper<TestView> READ_TEST_MAPPER = (resultSet, rowNumber) ->
            new TestView(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("text"));

    private JdbcTemplate template;


    public JdbcTestService(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<TestView> getTest() {
        String sql = "SELECT test.id, test.name, test.text FROM test";
        return template.query(sql, READ_TEST_MAPPER);
    }

    @Override
    public List<TestView> getTestAdmin() {
        String sql = "SELECT * FROM test";
        return template.query(sql, READ_TEST_ADMIN_MAPPER);
    }

    @Override
    public void addTest(TestView test) {
        String sql = "INSERT INTO test (name, text, answer) VALUES (?, ?, ?)";
        template.update(sql, test.getName(), test.getText(), test.getAnswer());
    }

    @Override
    public TestView changeTest(TestView test) {
        TestView tv = new TestView(1, "2", "3", "4");
        return tv;
    }

    @Override
    public void deleteTest(Integer testID) {

    }

}
