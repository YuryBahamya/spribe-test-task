package co.spribe.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that logs the start, success, failure, and skip events of test methods. It also
 * logs the start and finish of the entire test suite.
 */
public class LoggingTestListener implements ITestListener, IInvokedMethodListener {
  private static final Logger log = LoggerFactory.getLogger(LoggingTestListener.class);

  private String getTestName(ITestResult result) {
    return result.getTestClass().getRealClass().getSimpleName()
        + "."
        + result.getMethod().getMethodName();
  }

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
      String testName = getTestName(testResult);
      log.info("<============= Test '{}' - STARTED =============>", testName);
      MDC.put("testName", testName);
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
      switch (testResult.getStatus()) {
        case ITestResult.SUCCESS:
          log.info("<============= Test '{}' - PASSED =============>", getTestName(testResult));
          break;
        case ITestResult.FAILURE:
          log.error(
              "<============= Test '{}' - FAILED =============>",
              getTestName(testResult),
              testResult.getThrowable());
          break;
        case ITestResult.SKIP:
          log.warn("<============= Test '{}' - SKIPPED =============>", getTestName(testResult));
          break;
        default:
          break;
      }
      MDC.remove("testName");
    }
  }

  @Override
  public void onStart(ITestContext context) {
    log.info("<============= Test suite '{}' started =============>", context.getName());
  }

  @Override
  public void onFinish(ITestContext context) {
    log.info("<============= Test suite '{}' finished =============>", context.getName());
  }
}
