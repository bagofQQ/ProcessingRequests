package main.controller;

import main.api.query.login.UserLoginQuery;
import main.api.response.login.UserLoginResponse;
import main.api.response.logout.LogoutResponse;
import main.service.UserLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {


    private final UserLoginService userLoginService;

    public ApiAuthController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }


    @GetMapping("/api/auth/check")
    public UserLoginResponse check() {
        return userLoginService.checkUser();
    }

    @PostMapping("/api/auth/login")
    public UserLoginResponse getUserLoginResponse(@RequestBody UserLoginQuery user) {
        return userLoginService.getUserLoginInfo(user.getName(), user.getPassword());
    }

    @GetMapping("/api/auth/logout")
    public ResponseEntity<LogoutResponse> logout() {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(userLoginService.logoutUser(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
