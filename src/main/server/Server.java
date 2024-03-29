package server;

import handlers.*;
import spark.Spark;
import websocket.WebSocketHandler;

public class Server {
  static RegisterHandler registerHandler = new RegisterHandler();
  static LoginHandler loginHandler = new LoginHandler();
  static LogoutHandler logoutHandler = new LogoutHandler();
  static ClearHandler clearHandler = new ClearHandler();
  static ListGamesHandler listGamesHandler = new ListGamesHandler();
  static CreateGameHandler createGameHandler = new CreateGameHandler();
  static JoinGameHandler joinGameHandler = new JoinGameHandler();
  private final WebSocketHandler webSocketHandler = new WebSocketHandler();

  public static void main(String[] args) {
    new Server().run();
  }

  private void run() {
    Spark.port(8080);

    // Register a directory for hosting static files
    Spark.externalStaticFileLocation("web");

    Spark.webSocket("/connect", webSocketHandler);

    Spark.delete("/db", (req, res) ->
            (getClearHandlerInstance()).handleRequest(
                    res)
    );

    Spark.post("/session", (req, res) ->
            (getLoginHandlerInstance()).handleRequest(req,
                    res)
    );

    Spark.post("/user", (req, res) ->
            (getRegisterHandlerInstance()).handleRequest(req,
                    res)
    );

    Spark.delete("/session", (req, res) ->
            (getLogoutHandlerInstance()).handleRequest(req,
                    res)
    );

    Spark.get("/game", (req, res) ->
            (getListGamesHandlerInstance()).handleRequest(req,
                    res)
    );

    Spark.post("/game", (req, res) ->
            (getCreateGameHandlerInstance()).handleRequest(req,
                    res)
    );

    Spark.put("/game", (req, res) ->
            (getJoinGameHandlerInstance()).handleRequest(req,
                    res)
    );

  }

  private static RegisterHandler getRegisterHandlerInstance() { return registerHandler; }
  private static LoginHandler getLoginHandlerInstance() { return loginHandler; }
  private static LogoutHandler getLogoutHandlerInstance() { return logoutHandler; }
  private static ClearHandler getClearHandlerInstance() { return clearHandler; }
  private static ListGamesHandler getListGamesHandlerInstance() { return listGamesHandler; }
  private static CreateGameHandler getCreateGameHandlerInstance() { return createGameHandler; }
  private static JoinGameHandler getJoinGameHandlerInstance() { return joinGameHandler; }
}
