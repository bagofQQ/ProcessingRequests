package main.api.response.users;

import java.util.List;

public class CountUsersResponse {

    private int count;
    private List<UsersResponse> users;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UsersResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UsersResponse> users) {
        this.users = users;
    }
}
