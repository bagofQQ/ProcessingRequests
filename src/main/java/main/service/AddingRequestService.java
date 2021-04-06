package main.service;

import main.api.query.addingrequest.AddingRequestQuery;
import main.api.response.addingrequest.AddingRequestResponse;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AddingRequestService {

    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_SENT = "sent";

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final UserLoginService userLoginService;

    @Autowired
    public AddingRequestService(RequestRepository requestRepository, UserRepository userRepository, HttpSession httpSession, UserLoginService userLoginService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.httpSession = httpSession;
        this.userLoginService = userLoginService;
    }


    public AddingRequestResponse addRequestMethod(AddingRequestQuery addingRequestQuery) {
        AddingRequestResponse addingRequestResponse = new AddingRequestResponse();

        Optional<User> user = userRepository.findById(userLoginService.getIdentifierMap().get(httpSession.getId()));

        Request request = new Request();

        if (addingRequestQuery.getStatus().equals(STATUS_DRAFT)) {
            request.setModerationStatus(ModerationStatus.DRAFT);
        } else if (addingRequestQuery.getStatus().equals(STATUS_SENT)) {
            request.setModerationStatus(ModerationStatus.SENT);
        }
        request.setModeratorId(0);
        request.setUser(user.get());
        request.setTime(checkDate(addingRequestQuery.getTimestamp()));
        request.setTitle(addingRequestQuery.getTitle());
        request.setText(addingRequestQuery.getText());
        requestRepository.save(request);
        addingRequestResponse.setResult(true);
        return addingRequestResponse;
    }

    private Date checkDate(long timestamp) {
        Date date = Calendar.getInstance().getTime();
        Date datePost = new Date(timestamp * 1000);
        if (datePost.before(date)) {
            return date;
        }
        return datePost;
    }

}
