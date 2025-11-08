package co.spribe.api;

import co.spribe.api.common.BaseApiClient;
import co.spribe.constants.Endpoints;
import co.spribe.models.PlayerPojo;
import co.spribe.models.enums.HttpStatus;
import co.spribe.utils.JsonUtil;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * API client for Player-related operations. Provides methods to create, retrieve, update, and
 * delete players.
 */
public class PlayerApi extends BaseApiClient {

  private PlayerApi() {}

  private static class Holder {
    private static final PlayerApi INSTANCE = new PlayerApi();
  }

  public static PlayerApi getInstance() {
    return Holder.INSTANCE;
  }

  /**
   * Creates a new player and returns the native RestAssured Response.
   *
   * @param editor The editor performing the operation.
   * @param player The player data to create.
   * @return The RestAssured Response object.
   */
  public Response createPlayerNative(String editor, PlayerPojo player) {
    Allure.step("Create Player by '%s' with data: %s".formatted(editor, player));

    Map<String, Object> queryParams = JsonUtil.convertValue(player, Map.class, true);
    String path = String.format(Endpoints.PLAYER_CREATE, editor);
    return get(path, queryParams);
  }

  /**
   * Creates a new player, makes status code assertion, and returns the created PlayerPojo.
   *
   * @param editor The editor performing the operation.
   * @param player The player data to create.
   * @return The created PlayerPojo.
   */
  public PlayerPojo createPlayer(String editor, PlayerPojo player) {
    return createPlayerNative(editor, player)
        .then()
        .statusCode(HttpStatus._200.getCode())
        .extract()
        .as(PlayerPojo.class);
  }

  /**
   * Retrieves a player by ID and returns the native RestAssured Response.
   *
   * @param playerId The ID of the player to retrieve.
   * @return The RestAssured Response object.
   */
  public Response getPlayerNative(Long playerId) {
    Allure.step("Get Player with ID: " + playerId);

    return post(Endpoints.PLAYER_GET, Collections.singletonMap("playerId", playerId));
  }

  /**
   * Retrieves a player by ID, makes status code assertion, and returns the PlayerPojo.
   *
   * @param playerId The ID of the player to retrieve.
   * @return The retrieved PlayerPojo.
   */
  public PlayerPojo getPlayer(Long playerId) {
    return getPlayerNative(playerId)
        .then()
        .statusCode(HttpStatus._200.getCode())
        .extract()
        .as(PlayerPojo.class);
  }

  /** Retrieves all players and returns the native RestAssured Response. */
  public Response getAllPlayersNative() {
    Allure.step("Get All Players");

    return get(Endpoints.PLAYER_GET_ALL, Collections.emptyMap());
  }

  /** Retrieves all players, makes status code assertion, and returns the list of PlayerPojo. */
  public List<PlayerPojo> getAllPlayers() {
    List<PlayerPojo> playerPojoList =
        getAllPlayersNative()
            .then()
            .statusCode(HttpStatus._200.getCode())
            .extract()
            .jsonPath()
            .getList("players", PlayerPojo.class);
    return Optional.ofNullable(playerPojoList).orElse(Collections.emptyList());
  }

  /**
   * Deletes a player by ID and returns the native RestAssured Response.
   *
   * @param editor The editor performing the operation.
   * @param playerId The ID of the player to delete.
   * @return The RestAssured Response object.
   */
  public Response deletePlayerNative(String editor, Long playerId) {
    Allure.step("Delete Player by '%s' with ID: %d".formatted(editor, playerId));

    String path = String.format(Endpoints.PLAYER_DELETE, editor);
    return delete(path, Collections.singletonMap("playerId", playerId));
  }

  /**
   * Deletes a player by ID and makes status code assertion.
   *
   * @param editor The editor performing the operation.
   * @param playerId The ID of the player to delete.
   */
  public void deletePlayer(String editor, Long playerId) {
    deletePlayerNative(editor, playerId).then().statusCode(HttpStatus._204.getCode());
  }

  /**
   * Updates a player by ID and returns the native RestAssured Response.
   *
   * @param editor The editor performing the operation.
   * @param id The ID of the player to update.
   * @param playerPojo The updated player data.
   * @return The RestAssured Response object.
   */
  public Response updatePlayerNative(String editor, Long id, PlayerPojo playerPojo) {
    Allure.step("Update Player by '%s' with ID: %d and data: %s".formatted(editor, id, playerPojo));

    String path = String.format(Endpoints.PLAYER_UPDATE, editor, id);
    return patch(path, JsonUtil.toJson(playerPojo));
  }

  /**
   * Updates a player by ID, makes status code assertion, and returns the updated PlayerPojo.
   *
   * @param editor The editor performing the operation.
   * @param id The ID of the player to update.
   * @param playerPojo The updated player data.
   * @return The updated PlayerPojo.
   */
  public PlayerPojo updatePlayer(String editor, Long id, PlayerPojo playerPojo) {
    return updatePlayerNative(editor, id, playerPojo)
        .then()
        .statusCode(HttpStatus._200.getCode())
        .extract()
        .as(PlayerPojo.class);
  }
}
