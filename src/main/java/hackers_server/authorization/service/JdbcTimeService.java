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
            new TimeView(resultSet.getString("start_time"),
                    resultSet.getString("end_time"));


    private JdbcTemplate template;


    public JdbcTimeService(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public List<TimeView> getTime() {
        String sql = "SELECT start_time, end_time FROM time";
        return template.query(sql, READ_TIME_MAPPER);
    }

    @Override
    public void setTime(TimeView time) {
        String sql = "UPDATE time SET start_time = ?, end_time = ?";
        template.update(sql, time.getStart(), time.getStop());
    }
}
