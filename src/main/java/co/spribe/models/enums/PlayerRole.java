package co.spribe.models.enums;

public enum PlayerRole {
  ADMIN("admin"),
  SUPERVISOR("supervisor"),
  USER("user");

  private final String roleName;

  PlayerRole(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }

  @Override
  public String toString() {
    return roleName;
  }
}
