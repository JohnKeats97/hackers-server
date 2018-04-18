package seabattle.authorization.service;

import seabattle.authorization.views.TestView;

import java.util.List;

public interface TestService {

    void addTest(TestView user);

    void changeTest(TestView test);

    void deleteTest(Integer testID);

    List<TestView> getTest();

    List<TestView> getTestAdmin();

    void checkTest(TestView test, String username);
}