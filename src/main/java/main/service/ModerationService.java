package main.service;

import main.api.response.moderation.ModerationResponse;
import main.model.ModerationStatus;
import main.model.Request;
import main.model.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Optional;

@Service
public class ModerationService {

    private static final String ACCEPT = "accepted";
    private static final String DECLINE = "declined";

    private final RequestRepository requestRepository;
    private final HttpSession httpSession;
    private final UserLoginService userLoginService;

    @Autowired
    public ModerationService(RequestRepository requestRepository, HttpSession httpSession, UserLoginService userLoginService) {
        this.requestRepository = requestRepository;
        this.httpSession = httpSession;
        this.userLoginService = userLoginService;
    }

    public ModerationResponse moderationMethod(int requestId, String decision) {

        Optional<Request> requestOptional = requestRepository.findById(requestId);
        Request request = requestOptional.get();
        if (decision.equals(ACCEPT)) {
            return getModerationResponse(request, userLoginService.getIdentifierMap().get(httpSession.getId()), ModerationStatus.ACCEPTED);
        }
        if (decision.equals(DECLINE)) {
            return getModerationResponse(request, userLoginService.getIdentifierMap().get(httpSession.getId()), ModerationStatus.DECLINED);
        }
        return new ModerationResponse(false);
    }

    private ModerationResponse getModerationResponse(Request request, int idOperator, ModerationStatus status) {
        setPost(request, idOperator, status);
        return new ModerationResponse(true);
    }

    private void setPost(Request request, int idOperator, ModerationStatus status) {
        request.setModerationStatus(status);
        request.setModeratorId(idOperator);
        request.setTime(Calendar.getInstance().getTime());
        requestRepository.save(request);
    }
}
