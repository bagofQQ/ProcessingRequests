package main.controller;

import main.api.query.addingrequest.AddingRequestQuery;
import main.api.query.moderation.ModerationQuery;
import main.api.response.addingrequest.AddingRequestResponse;
import main.api.response.administration.AdministrationResponse;
import main.api.response.moderation.ModerationResponse;
import main.api.response.requests.CountRequestsResponse;
import main.api.response.users.CountUsersResponse;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiRequestController {

    private final RequestsService requestsService;
    private final UserLoginService userLoginService;
    private final AddingRequestService addingRequestService;
    private final EditingRequestService editingRequestService;
    private final ModerationService moderationService;
    private final UsersService usersService;
    private final AdministrationService administrationService;

    public ApiRequestController(RequestsService requestsService, UserLoginService userLoginService, AddingRequestService addingRequestService, EditingRequestService editingRequestService, ModerationService moderationService, UsersService usersService, AdministrationService administrationService) {
        this.requestsService = requestsService;
        this.userLoginService = userLoginService;
        this.addingRequestService = addingRequestService;
        this.editingRequestService = editingRequestService;
        this.moderationService = moderationService;
        this.usersService = usersService;
        this.administrationService = administrationService;
    }

    @GetMapping("/api/request/my")
    public ResponseEntity<CountRequestsResponse> userRequest(@RequestParam int offset, @RequestParam int limit, @RequestParam String status) {

        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(requestsService.getMyRequests(offset, limit, status), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    }

    @PostMapping("/api/request")
    public ResponseEntity<AddingRequestResponse> addRequest(@RequestBody AddingRequestQuery addingRequestQuery) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(addingRequestService.addRequestMethod(addingRequestQuery), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PutMapping("/api/request/{id}")
    public ResponseEntity<AddingRequestResponse> editRequest(@RequestBody AddingRequestQuery editRequestQuery, @PathVariable int id) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(editingRequestService.editRequestMethod(editRequestQuery, id), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/api/request/moderation")
    public ResponseEntity<CountRequestsResponse> getPostsForModeration(@RequestParam int offset, @RequestParam int limit) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(requestsService.getModerationRequests(offset, limit), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


    @PostMapping("/api/moderation")
    public ResponseEntity<ModerationResponse> moderation(@RequestBody ModerationQuery query) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(moderationService.moderationMethod(query.getRequestId(), query.getDecision()), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/api/users")
    public ResponseEntity<CountUsersResponse> users(@RequestParam int offset, @RequestParam int limit) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(usersService.getUsers(offset, limit), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<AdministrationResponse> editRequest(@PathVariable int id) {
        if (userLoginService.idUserAuthorized()) {
            return new ResponseEntity(administrationService.administrationMethod(id), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
