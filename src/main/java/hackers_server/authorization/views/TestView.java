package hackers_server.authorization.views;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestView {

    private Integer id;

    private String name;

    private String text;

    private String answer;

    public TestView(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                    @JsonProperty("text") String text, @JsonProperty("answer") String answer) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.answer = answer;
    }

    public TestView(Integer id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}