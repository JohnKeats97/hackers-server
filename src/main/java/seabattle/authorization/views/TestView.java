package seabattle.authorization.views;

public class TestView {

    private Integer id;

    private String name;

    private String text;

    public TestView(Integer id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
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