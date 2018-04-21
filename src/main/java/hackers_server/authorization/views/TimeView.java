package hackers_server.authorization.views;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class TimeView {
    @NotNull
    private String start;

    @NotNull
    private String stop;

    public TimeView(@JsonProperty("start") String start, @JsonProperty("stop") String stop) {
        this.start = start;
        this.stop = stop;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

}
