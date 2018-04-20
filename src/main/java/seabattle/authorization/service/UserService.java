package seabattle.authorization.service;

import seabattle.authorization.views.LeaderboardView;
import seabattle.authorization.views.UserView;

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