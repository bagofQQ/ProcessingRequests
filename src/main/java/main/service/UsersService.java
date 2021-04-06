package main.service;

import main.api.response.users.CountUsersResponse;
import main.api.response.users.UsersResponse;
import main.model.User;
import main.model.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private static final String ROLE_USER = "user";
    private static final String ROLE_OPERATOR = "operator";

    private final HttpSession httpSession;
    private final UserLoginService userLoginService;
    private final UserRepository userRepository;

    public UsersService(HttpSession httpSession, UserLoginService userLoginService, UserRepository userRepository) {
        this.httpSession = httpSession;
        this.userLoginService = userLoginService;
        this.userRepository = userRepository;
    }

    public CountUsersResponse getUsers(int offset, int limit) {
        CountUsersResponse countUsersResponse = new CountUsersResponse();

        List<UsersResponse> usersList = new ArrayList<>();
        int idAdmin = userLoginService.getIdentifierMap().get(httpSession.getId());
        int offsetPageable = offset / 10;

        Page<User> page = userRepository.findUsers(getPageable(offsetPageable, limit));
        addUserList(page, usersList);
        countUsersResponse.setCount(userRepository.countUsers());

        countUsersResponse.setUsers(usersList);

        return countUsersResponse;
    }

    private Pageable getPageable(int offsetPageable, int limit) {
        return PageRequest.of(offsetPageable, limit);
    }

    private void addUserList(Page<User> page, List<UsersResponse> usersList) {
        for (User user : page) {
            UsersResponse usersResponse = new UsersResponse();
            usersList.add(getUsersResponse(user, usersResponse));
        }
    }

    private UsersResponse getUsersResponse(User user, UsersResponse usersResponse) {
        usersResponse.setId(user.getId());
        usersResponse.setName(user.getName());
        if (user.getIsOperator() == 0) {
            usersResponse.setRole(ROLE_USER);
        }
        if (user.getIsOperator() == 1) {
            usersResponse.setRole(ROLE_OPERATOR);
        }
        return usersResponse;
    }
}
