package hackers_server.authorization.service;

import hackers_server.authorization.views.LeaderboardView;
import hackers_server.authorization.views.UserView;

import java.util.List;

public interface UserService {

    void addUser(UserView user);

    UserView getByLoginOrEmail(String loginOrEmail);

    Integer[] getTestByLoginOrEmail(String loginOrEmail);
    
    void changeUser(String user);

    List<LeaderboardView> getLeaderboard(Integer limit);

    UserView setScore(UserView user);

    Integer getPosition(UserView user);
}