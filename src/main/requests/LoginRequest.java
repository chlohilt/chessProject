package requests;

public class LoginRequest {
  private String message;
  private String authToken;

  private String username;
  public LoginRequest() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message=message;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken=authToken;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getUsername() {
    return username;
  }
}
