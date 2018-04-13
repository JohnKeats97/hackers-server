package seabattle.authorization.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import seabattle.authorization.views.TestView;

import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
@Service
public class JdbcTestService {

    private static final RowMapper<TestView> READ_TEST_MAPPER = (resultSet, rowNumber) ->
            new TestView(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("text"));

    private JdbcTemplate template;

    public JdbcTestService(JdbcTemplate template) {
        this.template = template;
    }

        public List<TestView> getTest() {
        String sql = "SELECT * FROM test";
        return template.query(sql, READ_TEST_MAPPER);
    }

}
