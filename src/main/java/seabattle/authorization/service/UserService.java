package seabattle.authorization.service;

import seabattle.authorization.views.LeaderboardView;
import seabattle.authorization.views.UserView;
import java.sql.Array;

import java.util.List;

public interface UserService {

    void addUser(UserView user);

    UserView getByLoginOrEmail(String loginOrEmail);

    String[] getTestByLoginOrEmail(String loginOrEmail);
    
    UserView changeUser(UserView user);

    List<LeaderboardView> getLeaderboard(Integer limit);

    UserView setScore(UserView user);

    Integer getPosition(UserView user);
}