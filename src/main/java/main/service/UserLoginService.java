package main.service;

import main.api.response.login.UserLoginInfoResponse;
import main.api.response.login.UserLoginResponse;
import main.api.response.logout.LogoutResponse;
import main.model.User;
import main.model.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserLoginService {

    private HashMap<String, Integer> identifierMap = new HashMap<>();

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public UserLoginService(UserRepository userRepository, HttpSession httpSession) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
    }


    public UserLoginResponse getUserLoginInfo(String name, String password) {

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        String identifier = httpSession.getId();
        List<User> findUser = userRepository.findUser(name, password);
        if (findUser.size() != 1) {
            userLoginResponse.setResult(false);
            return userLoginResponse;
        }
        identifierMap.put(identifier, findUser.get(0).getId());
        return setUserInfo(findUser.get(0), userLoginResponse);

    }

    public UserLoginResponse checkUser() {
        UserLoginResponse checkUserLoginResponse = new UserLoginResponse();
        if (identifierMap.size() > 0) {
            for (Map.Entry<String, Integer> f : identifierMap.entrySet()) {
                if (f.getKey().equals(httpSession.getId())) {
                    Optional<User> user = userRepository.findById(identifierMap.get(httpSession.getId()));
                    return setUserInfo(user.get(), checkUserLoginResponse);
                }
            }
        }
        checkUserLoginResponse.setResult(false);
        return checkUserLoginResponse;
    }

    public LogoutResponse logoutUser() {
        LogoutResponse logoutResponse = new LogoutResponse();
        identifierMap.remove(httpSession.getId());
        logoutResponse.setResult(true);
        return logoutResponse;
    }


    private UserLoginResponse setUserInfo(User user, UserLoginResponse userLoginResponse) {
        UserLoginInfoResponse userLoginInfoResponse = new UserLoginInfoResponse();
        userLoginInfoResponse.setId(user.getId());
        userLoginInfoResponse.setName(user.getName());

        int isOperator = user.getIsOperator();
        int isAdministrator = user.getIsAdministrator();

        if (isOperator == 1) {
            userLoginInfoResponse.setOperator(true);
        }
        if (isAdministrator == 1) {
            userLoginInfoResponse.setAdministrator(true);
        }

        userLoginResponse.setUser(userLoginInfoResponse);
        userLoginResponse.setResult(true);
        return userLoginResponse;
    }

    public HashMap<String, Integer> getIdentifierMap() {
        return identifierMap;
    }

    public void setIdentifierMap(HashMap<String, Integer> identifierMap) {
        this.identifierMap = identifierMap;
    }

    public boolean idUserAuthorized() {
        return identifierMap.containsKey(httpSession.getId());
    }


}
