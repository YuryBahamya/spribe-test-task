package co.spribe.models.enums;

public enum PlayerGender {
  MALE("male"),
  FEMALE("female");
  private final String genderName;

  PlayerGender(String genderName) {
    this.genderName = genderName;
  }

  public String getGenderName() {
    return genderName;
  }

  @Override
  public String toString() {
    return genderName;
  }
}
