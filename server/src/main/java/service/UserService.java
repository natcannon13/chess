package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.models.RegisterRequest;
import server.models.RegisterResult;

import java.util.UUID;

public class UserService {

    private AuthDAO authDAO;
    private UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException{
        UserData user = userDAO.getUser(request.username());
        if(user != null){
            throw new AlreadyTakenException("Error: already taken");
        }
        else{
            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(authToken, request.username());
        }
    }

}
