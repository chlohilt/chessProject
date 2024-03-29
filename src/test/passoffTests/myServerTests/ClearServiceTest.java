package passoffTests.myServerTests;

import database.DataAccessException;
import models.Game;
import models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.ClearService;
import org.junit.jupiter.api.*;

public class ClearServiceTest {
  private final static ClearService clearService = new ClearService();
  @BeforeAll
  public static void init() {
    try {
      clearService.clear();
      clearService.getAuthDataAccess().createAuthToken("fake");
      clearService.getUserDataAccess().createUser(new User("user", "pass", "email@byu.edu"));
      clearService.getGameDataAccess().insertGame(new Game());
    } catch (Exception ignored) {}

  }

  @Test
  void clearData() throws DataAccessException {
    Assertions.assertNotEquals(0, clearService.getAuthDataAccess().getAuthTokenSize(), "DAO addition didn't work for auth");
    Assertions.assertNotEquals(0, clearService.getUserDataAccess().getUserSize(), "DAO addition didn't work for users");
    Assertions.assertNotEquals(0, clearService.getGameDataAccess().getGameSize(), "DAO addition didn't work for games");

    clearService.clear();

    Assertions.assertEquals(0, clearService.getAuthDataAccess().getAuthTokenSize(), "Clear service didn't work for auth");
    Assertions.assertEquals(0, clearService.getUserDataAccess().getUserSize(), "Clear service didn't work for users");
    Assertions.assertEquals(0, clearService.getGameDataAccess().getGameSize(), "Clear service didn't work for games");
  }
}
