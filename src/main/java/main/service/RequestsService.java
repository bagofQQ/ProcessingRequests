package main.service;

import main.api.response.requests.CountRequestsResponse;
import main.api.response.requests.RequestsResponse;
import main.api.response.requests.UserResponse;
import main.model.Request;
import main.model.RequestRepository;
import main.model.User;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class RequestsService {


    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_SENT = "sent";
    private static final String STATUS_ACCEPTED = "accepted";
    private static final String STATUS_DECLINED = "declined";

    private static final String MODERATION_DRAFT = "DRAFT";
    private static final String MODERATION_SENT = "SENT";
    private static final String MODERATION_ACCEPTED = "ACCEPTED";
    private static final String MODERATION_DECLINED = "DECLINED";


    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final UserLoginService userLoginService;


    @Autowired
    public RequestsService(RequestRepository requestRepository, UserRepository userRepository, HttpSession httpSession, UserLoginService userLoginService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.httpSession = httpSession;
        this.userLoginService = userLoginService;
    }


    public CountRequestsResponse getMyRequests(int offset, int limit, String status) {
        CountRequestsResponse countRequestsResponse = new CountRequestsResponse();

        List<RequestsResponse> requestList = new ArrayList<>();
        int idUser = userLoginService.getIdentifierMap().get(httpSession.getId());
        int offsetPageable = offset / 10;

        if (status.equals(STATUS_DRAFT)) {
            Page<Request> page = requestRepository.findMyRequests(getDateNow(), idUser, MODERATION_DRAFT, getPageable(offsetPageable, limit));
            addRequestList(page, requestList, idUser);
            countRequestsResponse.setCount(requestRepository.countModStatusUser(MODERATION_DRAFT, idUser));
        } else if (status.equals(STATUS_SENT)) {
            Page<Request> page = requestRepository.findMyRequests(getDateNow(), idUser, MODERATION_SENT, getPageable(offsetPageable, limit));
            addRequestList(page, requestList, idUser);
            countRequestsResponse.setCount(requestRepository.countModStatusUser(MODERATION_SENT, idUser));
        } else if (status.equals(STATUS_ACCEPTED)) {
            Page<Request> page = requestRepository.findMyRequests(getDateNow(), idUser, MODERATION_ACCEPTED, getPageable(offsetPageable, limit));
            addRequestList(page, requestList, idUser);
            countRequestsResponse.setCount(requestRepository.countModStatusUser(MODERATION_ACCEPTED, idUser));
        } else if (status.equals(STATUS_DECLINED)) {
            Page<Request> page = requestRepository.findMyRequests(getDateNow(), idUser, MODERATION_DECLINED, getPageable(offsetPageable, limit));
            addRequestList(page, requestList, idUser);
            countRequestsResponse.setCount(requestRepository.countModStatusUser(MODERATION_DECLINED, idUser));
        }
        countRequestsResponse.setRequests(requestList);
        return countRequestsResponse;
    }

    public CountRequestsResponse getModerationRequests(int offset, int limit) {
        CountRequestsResponse countRequestsResponse = new CountRequestsResponse();

        List<RequestsResponse> requestList = new ArrayList<>();
        int idOperator = userLoginService.getIdentifierMap().get(httpSession.getId());
        int offsetPageable = offset / 10;

        Page<Request> page = requestRepository.findModerationRequestsNew(getDateNow(), MODERATION_SENT, getPageable(offsetPageable, limit));
        addRequestList(page, requestList, idOperator);
        countRequestsResponse.setCount(requestRepository.countModStatus(MODERATION_SENT));

        countRequestsResponse.setRequests(requestList);
        return countRequestsResponse;
    }


    private Date getDateNow() {
        return Calendar.getInstance().getTime();
    }

    private Pageable getPageable(int offsetPageable, int limit) {
        return PageRequest.of(offsetPageable, limit);
    }

    private void addRequestList(Page<Request> page, List<RequestsResponse> requestsList, int idUserOnline) {
        for (Request request : page) {
            RequestsResponse requestsResponse = new RequestsResponse();
            requestsList.add(getRequestsResponse(request, requestsResponse, idUserOnline));
        }
    }

    private RequestsResponse getRequestsResponse(Request request, RequestsResponse requestsResponse, int idUserOnline) {

        requestsResponse.setId(request.getId());
        Date date = request.getTime();
        long timestamp = date.getTime() / 1000;
        requestsResponse.setTimestamp(timestamp);
        UserResponse userResponse = new UserResponse();
        Optional<User> user = userRepository.findById(request.getUser().getId());
        userResponse.setName(user.get().getName());
        userResponse.setId(user.get().getId());
        requestsResponse.setUser(userResponse);
        requestsResponse.setTitle(request.getTitle());

        Optional<User> userOnline = userRepository.findById(idUserOnline);
        if (userOnline.get().getIsOperator() == 0) {
            requestsResponse.setText(request.getText());
        }
        if (userOnline.get().getIsOperator() == 1) {
            String operatorText = request.getText().replaceAll(".", "$0-");
            requestsResponse.setText(operatorText);
        }
        return requestsResponse;
    }
}
