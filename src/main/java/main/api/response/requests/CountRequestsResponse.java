package main.api.response.requests;

import java.util.List;

public class CountRequestsResponse {

    private int count;
    private List<RequestsResponse> requests;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RequestsResponse> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestsResponse> requests) {
        this.requests = requests;
    }
}
