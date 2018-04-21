package hackers_server.authorization.service;

import hackers_server.authorization.views.TestView;
import hackers_server.authorization.views.TimeView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
@Service
public class JdbcTimeService implements TimeService {

    private static final RowMapper<TimeView> READ_TIME_MAPPER = (resultSet, rowNumber) ->
            new TimeView(resultSet.getString("start"),
                    resultSet.getString("stop"));


    private JdbcTemplate template;


    public JdbcTimeService(JdbcTemplate template) {
        this.template = template;
        try {
            template.query("SELECT * FROM time", READ_TIME_MAPPER);
        }catch (Exception e) {
            template.update("INSERT INTO time (start, stop) VALUES (?, ?)", "2018-04-21T08:38:28.232Z", "2019-04-21T08:38:28.232Z");
        }
    }

    @Override
    public List<TimeView> getTime() {
        String sql = "SELECT start, end FROM time";
        return template.query(sql, READ_TIME_MAPPER);
    }

    @Override
    public void setTime(TimeView time) {
        String sql = "UPDATE time SET start = ?, stop = ?";
        template.update(sql, time.getStart(), time.getStop());
    }
}
