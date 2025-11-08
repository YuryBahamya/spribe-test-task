package co.spribe.models;

import co.spribe.constants.GlobalConstants;
import co.spribe.models.enums.PlayerGender;
import co.spribe.models.enums.PlayerRole;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;

public class PlayerPojoBuilder {
  private Integer age =
      ThreadLocalRandom.current().nextInt(GlobalConstants.MIN_AGE, GlobalConstants.MAX_AGE + 1);
  private String gender =
      ThreadLocalRandom.current().nextBoolean()
          ? PlayerGender.MALE.getGenderName()
          : PlayerGender.FEMALE.getGenderName();
  private String login = "test_login_" + RandomStringUtils.secure().nextAlphanumeric(12);
  private String password =
      "p0"
          + RandomStringUtils.secure()
              .nextAlphanumeric(
                  ThreadLocalRandom.current()
                      .nextInt(
                          GlobalConstants.PASSWORD_MIN_LENGTH - 2,
                          GlobalConstants.PASSWORD_MAX_LENGTH - 1));
  private String role = PlayerRole.USER.getRoleName();
  private String screenName = "test_sname_" + RandomStringUtils.secure().nextAlphanumeric(12);

  public PlayerPojoBuilder setAge(Integer age) {
    this.age = age;
    return this;
  }

  public PlayerPojoBuilder setGender(String gender) {
    this.gender = gender;
    return this;
  }

  public PlayerPojoBuilder setLogin(String login) {
    this.login = login;
    return this;
  }

  public PlayerPojoBuilder setPassword(String password) {
    this.password = password;
    return this;
  }

  public PlayerPojoBuilder setRole(String role) {
    this.role = role;
    return this;
  }

  public PlayerPojoBuilder setScreenName(String screenName) {
    this.screenName = screenName;
    return this;
  }

  public PlayerPojo build() {
    PlayerPojo playerPojo =
        new PlayerPojo()
            .setAge(age)
            .setGender(gender)
            .setLogin(login)
            .setPassword(password)
            .setRole(role)
            .setScreenName(screenName);
    return playerPojo;
  }

  public static PlayerPojo randomPlayerWithRole(PlayerRole role) {
    String roleName = role == null ? null : role.getRoleName();
    return new PlayerPojoBuilder().setRole(roleName).build();
  }

  public static PlayerPojo randomUserPlayer() {
    return new PlayerPojoBuilder().build();
  }
}
