package main.api.query.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationQuery {

    @JsonProperty("request_id")
    private int requestId;
    private String decision;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
