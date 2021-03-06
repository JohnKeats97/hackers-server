package hackers_server.authorization.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@SuppressWarnings("unused")
@Validated
public final class UserView {

    @Email
    private String email;

    private String login;

    private String password;

    private Integer score;

    private Integer isEmail;

    public UserView(@JsonProperty("email") String email, @JsonProperty("login") String login,
             @JsonProperty("password") String password, @JsonProperty("score") Integer score, @JsonProperty("isEmail") Integer isEmail) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.score = score;
        this.isEmail = isEmail;
    }

    @Override
    public String toString() {
        return "login = " + login + " email = " + email + " password = " + password + " score = " + score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(Integer isEmail) {
        this.isEmail = isEmail;
    }


    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserView other = (UserView) obj;
        if (getEmail() == null) {
            if (other.getEmail() != null) {
                return false;
            }
        } else if (!getEmail().equals(other.getEmail())) {
            return false;
        }
        if (getLogin() == null) {
            if (other.getLogin() != null) {
                return false;
            }
        } else if (!getLogin().equals(other.getLogin())) {
            return false;
        }
        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (getEmail() == null) {
            result = prime * result;
        } else {
            result = prime * result + getEmail().hashCode();
        }
        if (getLogin() == null) {
            result = prime * result;
        } else {
            result = prime * result + getLogin().hashCode();
        }
        if (getPassword() == null) {
            result = prime * result;
        } else {
            result = prime * result + getPassword().hashCode();
        }
        if (getScore() == null) {
            result = prime * result;
        } else {
            result = prime * result + getScore().hashCode();
        }
        return result;
    }
}