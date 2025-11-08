package co.spribe.api.common;

import co.spribe.config.AppConfig;
import co.spribe.config.PropertyController;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.io.OutputStream;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating and managing RequestSpecification instances for REST API testing.
 * Utilizes ThreadLocal to ensure thread safety in parallel test executions.
 */
public class SpecFactory {
  private static final ThreadLocal<RequestSpecification> threadLocalSpec =
      ThreadLocal.withInitial(SpecFactory::createSpec);

  private static final Logger restLogger = LoggerFactory.getLogger("RestAssured");
  private static final PrintStream restLogStream = createSlf4jPrintStream();

  private SpecFactory() {}

  private static RequestSpecification createSpec() {
    AppConfig appConfig = PropertyController.getAppConfig();

    RequestSpecBuilder builder =
        new RequestSpecBuilder()
            .setRelaxedHTTPSValidation()
            .setBaseUri(appConfig.baseUrl())
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured());
    if (appConfig.isApiLogsEnabled()) {
      builder
          .addFilter(new RequestLoggingFilter(restLogStream))
          .addFilter(new ResponseLoggingFilter(restLogStream));
    }
    return builder.build();
  }

  public static RequestSpecification getRequestSpecification() {
    return threadLocalSpec.get();
  }

  /** Creates a PrintStream that redirects output to SLF4J logger. */
  private static PrintStream createSlf4jPrintStream() {
    return new PrintStream(
        new OutputStream() {
          @Override
          public void write(int b) {
            // Not used
          }

          @Override
          public void write(byte[] b, int off, int len) {
            String msg = new String(b, off, len);
            restLogger.info(msg);
          }
        },
        true);
  }
}
