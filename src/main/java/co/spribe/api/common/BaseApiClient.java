package co.spribe.api.common;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import java.util.Map;

/**
 * Abstract base class for API clients providing common HTTP methods. Utilizes RestAssured for
 * making HTTP requests.
 */
public abstract class BaseApiClient {

  public Response get(String path, Map<String, Object> queryParams) {
    return given(SpecFactory.getRequestSpecification()).queryParams(queryParams).when().get(path);
  }

  public Response post(String path, Object body) {
    return given(SpecFactory.getRequestSpecification()).body(body).when().post(path);
  }

  public Response put(String path, Object body) {
    return given(SpecFactory.getRequestSpecification()).body(body).when().put(path);
  }

  public Response patch(String path, Object body) {
    return given(SpecFactory.getRequestSpecification()).body(body).when().patch(path);
  }

  public Response delete(String path, Object body) {
    return given(SpecFactory.getRequestSpecification()).body(body).when().delete(path);
  }
}
