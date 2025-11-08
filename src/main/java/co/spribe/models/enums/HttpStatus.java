package co.spribe.models.enums;

public enum HttpStatus {
  _200(200, "OK"),
  _204(204, "No Content"),
  _400(400, "Bad Request"),
  _401(401, "Unauthorized"),
  _403(403, "Forbidden"),
  _404(404, "Not Found");
  private final int code;
  private final String description;

  HttpStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String toString() {
    return code + " " + description;
  }
}
