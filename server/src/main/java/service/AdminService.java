package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class AdminService {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    public AdminService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

}
