package co.spribe.listeners;

import co.spribe.config.AppConfig;
import co.spribe.config.PropertyController;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

/**
 * TestNG listener that alters the test suite configuration to set parallel execution mode and
 * thread count based on application properties.
 */
public class ThreadListener implements IAlterSuiteListener {
  private static final Logger log = LoggerFactory.getLogger(ThreadListener.class);
  private static final String className = ThreadListener.class.getSimpleName();

  @Override
  public void alter(List<XmlSuite> suites) {
    AppConfig config = PropertyController.getAppConfig();
    int threads = config.threadsCount();
    String modeFromConfig = config.parallelMode();

    XmlSuite.ParallelMode parallelMode = parseParallelMode(modeFromConfig);

    for (XmlSuite suite : suites) {
      suite.setParallel(parallelMode);
      suite.setThreadCount(threads);
      log.info(
          "[{}] Using parallel mode '{}' with {} threads for TestNG suite: {}",
          className,
          parallelMode,
          threads,
          suite.getName());
    }
  }

  private XmlSuite.ParallelMode parseParallelMode(String value) {
    if (StringUtils.isEmpty(value)) return XmlSuite.ParallelMode.METHODS;
    switch (value.trim().toLowerCase()) {
      case "methods":
        return XmlSuite.ParallelMode.METHODS;
      case "classes":
        return XmlSuite.ParallelMode.CLASSES;
      default:
        log.info("[{}] Unsupported parallelMode={}, defaulting to 'methods'", className, value);
        return XmlSuite.ParallelMode.METHODS;
    }
  }
}
