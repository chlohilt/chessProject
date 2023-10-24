package services;

import dataAccess.AuthDAO;
import dataAccess.CommonDataAccess;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responses.ResponseClass;
import spark.Request;
import spark.Response;

public class BaseService {
  CommonDataAccess commonDataAccess;
  BaseService() {
    commonDataAccess = new CommonDataAccess();
  }

  public ResponseClass authorizationCheck(Request request) {
    try {
      String authToken = request.headers("Authorization");
      if (authToken == null) {
        return new ResponseClass("Error: unauthorized");
      }
      if (getAuthDataAccess().returnUsername(authToken) != null) {
        return new ResponseClass();
      } else {
        return new ResponseClass("Error: unauthorized");
      }
    } catch (Exception e) {
      return new ResponseClass("Error: database error");
    }
  }

  public AuthDAO getAuthDataAccess() {
    return commonDataAccess.getCommonAuthDAO();
  }

  public UserDAO getUserDataAccess() {
    return commonDataAccess.getCommonUserDAO();
  }

  public GameDAO getGameDataAccess() {
    return commonDataAccess.getCommonGameDAO();
  }
}
