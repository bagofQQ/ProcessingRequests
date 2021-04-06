package main.service;

import main.api.response.administration.AdministrationResponse;
import main.model.User;
import main.model.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministrationService {

    private final UserRepository userRepository;

    public AdministrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdministrationResponse administrationMethod(int idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        User user = userOptional.get();

        if (user.getIsOperator() == 0 & user.getIsAdministrator() != 1) {
            getAdministrationResponse(user, 1);
        }
        if (user.getIsOperator() == 1 & user.getIsAdministrator() != 1) {
            getAdministrationResponse(user, 0);
        }

        return new AdministrationResponse(false);
    }

    private AdministrationResponse getAdministrationResponse(User user, int isOperator) {
        user.setIsOperator(isOperator);
        userRepository.save(user);
        return new AdministrationResponse(true);
    }
}
