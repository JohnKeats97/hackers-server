package hackers_server.authorization.service;

import hackers_server.authorization.views.TestView;
import hackers_server.authorization.views.TimeView;

import java.util.List;

public interface TimeService {

    void setTime(TimeView time);

    List<TimeView> getTime();
}