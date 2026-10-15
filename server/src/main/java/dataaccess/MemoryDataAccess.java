package dataaccess;

public class MemoryDataAccess implements DataAccess{

    private AuthDAO authDao;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    public MemoryDataAccess(){
        authDao = new MemoryAuthDAO();

    }

    public void clear(){

    }

}
