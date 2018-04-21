package hackers_server.authorization.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import hackers_server.authorization.service.UserService;
import hackers_server.authorization.service.JdbcTestService;
import hackers_server.authorization.views.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.List;


import org.springframework.mail.SimpleMailMessage;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@RestController
@CrossOrigin(origins = {"http://localhost:8080", "http://hackers-contest.herokuapp.com",
                        "https://hackers-contest.herokuapp.com"})
@RequestMapping(path = "/api")
@Validated
public class Controller {

    @Autowired
    private UserService dbUsers;

    @Autowired
    private JdbcTestService dbTest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String CURRENT_USER_KEY = "currentUser";

    @Autowired
    private JavaMailSenderImpl javaMailSender;


    @RequestMapping(method = RequestMethod.GET, path = "info")
    public ResponseEntity info(HttpSession httpSession) {
        final String currentUser = (String) httpSession.getAttribute(CURRENT_USER_KEY);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseView.ERROR_NOT_LOGGED_IN);
        }
        try {
            UserView user = dbUsers.getByLoginOrEmail(currentUser);
            user.setPassword(null);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (DataAccessException ex) {
            httpSession.setAttribute(CURRENT_USER_KEY, null);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseView.ERROR_NOT_LOGGED_IN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@Valid @RequestBody AuthorisationView loggingData, HttpSession httpSession) {
        try {
            UserView currentUser = dbUsers.getByLoginOrEmail(loggingData.getLoginEmail());
            if (currentUser.getIsEmail() == 1) {
                if (passwordEncoder.matches(loggingData.getPassword(), currentUser.getPassword())) {
                    httpSession.setAttribute(CURRENT_USER_KEY, currentUser.getLogin());
                    currentUser.setPassword(null);
                    return ResponseEntity.status(HttpStatus.OK).body(currentUser);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_BAD_LOGIN_DATA);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseView.ERROR_BAD_LOGIN_DATA);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_BAD_LOGIN_DATA);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "logout")
    public ResponseEntity<ResponseView> logout(HttpSession httpSession) {
        httpSession.setAttribute(CURRENT_USER_KEY, null);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseView.SUCCESS_LOGOUT);
    }

    @RequestMapping(method = RequestMethod.POST, path = "users", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid @RequestBody UserView registerData) {
        try {
            String encodedPassword = passwordEncoder.encode(registerData.getPassword());
            registerData.setPassword(encodedPassword);

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(registerData.getEmail());
                message.setSubject("Подтверждение почты");
                message.setText("Для подтверждения регистрации перейдите по ссылке: " +
                        "https://hackers-back.herokuapp.com/api/users/" + registerData.getLogin() );
                javaMailSender.send(message);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseView.ERROR_EMAIL_USER);
            }

            dbUsers.addUser(registerData);

        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_USER_ALREADY_EXISTS);
        }

        registerData.setPassword(null);
        registerData.setScore(0);
        registerData.setIsEmail(0);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerData);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, path = "users/{login}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity change(@PathVariable(value = "login") String login) {
            try {
                dbUsers.changeUser(login);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseView.OK);
            } catch (DataAccessException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseView.ERROR_USER_NOT_FOUND);
            }
    }


    @RequestMapping(method = RequestMethod.GET, path = "leaderboard",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeaderboardView>> getLeaderboard(
            @Valid @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            HttpSession httpSession) {
        List<LeaderboardView> leaders = dbUsers.getLeaderboard(limit);
        final String currentUser = (String) httpSession.getAttribute(CURRENT_USER_KEY);
        int iter = 1;
        LeaderboardView addView = null;
        for (LeaderboardView leaderboardView: leaders) {
            leaderboardView.setPosition(iter);
            iter++;
            if (leaderboardView.getLogin().equals(currentUser)) {
                addView = leaderboardView;
            }
        }
        if (addView != null) {
            leaders.add(addView);
        }

        if (currentUser == null || addView != null) {
            return ResponseEntity.status(HttpStatus.OK).body(leaders);
        } else {
            final UserView currentUserView = dbUsers.getByLoginOrEmail(currentUser);
            if (currentUserView != null) {
                leaders.add(new LeaderboardView(dbUsers.getPosition(currentUserView),
                        currentUserView.getLogin(), currentUserView.getScore()));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(leaders);
    }

    @RequestMapping(method = RequestMethod.GET, path = "about",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AboutView> getAbout() {
        String about = "Created by John Buevich";
        return ResponseEntity.status(HttpStatus.OK).body(new AboutView(about));
    }

    @RequestMapping(method = RequestMethod.GET, path = "time",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AboutView> getTime() {
        StringBuilder sb = new StringBuilder();

        File file = new File("a.out");


        try {

            if(!file.exists()){
                file.createNewFile();
            }

            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
        //PrintWriter обеспечит возможности записи в файл
        PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст у файл
                out.print(sb.toString() + "1");
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        AboutView time = new AboutView(sb.toString());
        return ResponseEntity.status(HttpStatus.OK).body(time);
    }


    @RequestMapping(method = RequestMethod.GET, path = "test-admin",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestView>> getTestAdmin() {
        List<TestView> test = dbTest.getTestAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(test);
    }



    @RequestMapping(method = RequestMethod.GET, path = "test",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestView>> getTest() {
        List<TestView> test = dbTest.getTest();
        return ResponseEntity.status(HttpStatus.OK).body(test);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add-test",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTest(@RequestBody TestView test) {
        dbTest.addTest(test);
        return ResponseEntity.status(HttpStatus.OK).body("{\"response\": \"OK\"}");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/delete-test",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTest(@RequestBody TestView testView) {
        dbTest.deleteTest(testView.getId());
        return ResponseEntity.status(HttpStatus.OK).body("{\"response\": \"OK\"}");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/change-test",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeTest(@RequestBody TestView test) {
        dbTest.changeTest(test);
        return ResponseEntity.status(HttpStatus.OK).body("{\"response\": \"OK\"}");
    }


    @RequestMapping(method = RequestMethod.POST, path = "/test",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkTest(@RequestBody TestView test,
                                    HttpSession httpSession) {
        String currentUser = (String) httpSession.getAttribute(CURRENT_USER_KEY);
        if (currentUser != null) {
            try {
                dbTest.checkTest(test, currentUser);
                return ResponseEntity.status(HttpStatus.OK).body("{\"answer\": \"OK\"}");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"answer\": \"NOT\"}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"response\": \"NOT LOGIN\"}");
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/user-test")
    public ResponseEntity userTest(HttpSession httpSession) {
        final String currentUser = (String) httpSession.getAttribute(CURRENT_USER_KEY);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseView.ERROR_NOT_LOGGED_IN);
        }
        try {
            final Integer[] tests = dbUsers.getTestByLoginOrEmail(currentUser);
            return ResponseEntity.status(HttpStatus.OK).body(tests);
        } catch (DataAccessException ex) {
            httpSession.setAttribute(CURRENT_USER_KEY, null);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseView.ERROR_NOT_LOGGED_IN);
        }
    }

}
