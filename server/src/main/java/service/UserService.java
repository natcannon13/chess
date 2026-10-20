package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.models.LoginRequest;
import server.models.LoginResult;
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

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, MissingArgumentException {
        if(request.password() == null || request.password().isBlank()){
            throw new MissingArgumentException("Error: bad request");
        }
        UserData user = userDAO.getUser(request.username());
        if(user != null){
            throw new AlreadyTakenException("Error: already taken");
        }
        else{
            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(request.username(), authToken);
        }
    }

    public LoginResult login(LoginRequest request) throws MissingArgumentException, UnauthorizedException{
        if(request.password() == null || request.password().isBlank()){
            throw new MissingArgumentException("Error: bad request");
        }
        UserData user = userDAO.getUser(request.username());
        if(user == null){
            throw new MissingArgumentException("Error: bad request");
        }
        if(!request.password().equals(user.password())){
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, request.username()));
        return new LoginResult(request.username(), authToken);
    }

    

}
