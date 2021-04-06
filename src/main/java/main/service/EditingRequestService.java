package main.service;

import main.api.query.addingrequest.AddingRequestQuery;
import main.api.response.addingrequest.AddingRequestResponse;
import main.model.ModerationStatus;
import main.model.Request;
import main.model.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class EditingRequestService {

    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_SENT = "sent";

    private final RequestRepository requestRepository;


    @Autowired
    public EditingRequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

    }

    public AddingRequestResponse editRequestMethod(AddingRequestQuery editRequestRequest, int requestId) {

        Optional<Request> requestOptional = requestRepository.findById(requestId);
        Request request = requestOptional.get();

        AddingRequestResponse addingRequestResponse = new AddingRequestResponse();

        if (editRequestRequest.getStatus().equals(STATUS_DRAFT)) {
            request.setModerationStatus(ModerationStatus.DRAFT);
        } else if (editRequestRequest.getStatus().equals(STATUS_SENT)) {
            request.setModerationStatus(ModerationStatus.SENT);
        }
        request.setTime(checkDate(editRequestRequest.getTimestamp()));
        request.setTime(checkDate(editRequestRequest.getTimestamp()));
        request.setTitle(editRequestRequest.getTitle());
        request.setText(editRequestRequest.getText());
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
