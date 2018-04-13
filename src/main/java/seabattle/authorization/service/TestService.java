package seabattle.authorization.service;

import seabattle.authorization.views.TestView;

import java.util.List;

public interface TestService {

    void addTest(TestView user);

    TestView changeTest(TestView test);

    void deleteTest(Integer testID);

    List<TestView> getTest();
}