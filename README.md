# Spribe Test Task: Player Controller API Test Framework

This is a lightweight, parallel-ready REST API test automation framework developed as part of a technical assignment for the Senior QA Automation Engineer position

---

## 🚀 Technologies Used

- **Java 17**
- **Maven 3.9.10** — build tool and dependency management
- **TestNG** — test runner and assertion framework
- **RestAssured** — HTTP client for API testing
- **Allure Reporting** — rich and interactive test reports
- **SLF4J** — tool for logging


---

## 📁 Project Structure

```
src/
├───main/
│   ├───java/co/spribe
│   │           ├───api/             # API interaction layer 
│   │           ├───config/          # Configuration management
│   │           ├───constants/       # Constant values
│   │           ├───exceptions/      # Custom exceptions
│   │           ├───listeners/       # TestNG listeners
│   │           ├───models/          # POJOs for request/response bodies
│   │           └───utils/           # Utility classes
│   │
│   └───resources/                   # Resource files (properties, configs, logs)
│
└───test/
    ├───java/co/spribe/tests/        # Test classes
    │
    └───resources/                   # Test resources (TestNG XMLs, data files)
```

---

## 🔧 Setup

### Prerequisites
- Java 17+
- Maven 3.9.10+
- Git
- IDE (e.g., IntelliJ IDEA, Eclipse) — optional but recommended

### Java and Maven Installation
Ensure Java and Maven are installed and configured in your system's PATH.

To verify Java installation:
```bash
java -version
```
Expected output:
```
java version "17.0.8" 2023-07-18 LTS
Java(TM) SE Runtime Environment (build 17.0.8+9-LTS-211)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.8+9-LTS-211, mixed mode, sharing)
```
To verify Maven installation:
```bash
mvn -version
```
Expected output:
```
Apache Maven 3.9.10 (5f519b97e944483d878815739f519b2eade0a91d)
```

### Clone Repository and Install Dependencies

```bash
git clone https://github.com/YuryBahamya/spribe-test-task.git
cd spribe-test-task
mvn clean install
```

---

## ✅ How to Run Tests

### Via Maven Command Line

```bash
mvn clean test
```

Available systems properties for test configuration:
- `-DbaseUrl=` — Base URL of the API (default: `http://3.68.165.45`)
- `-DthreadsCount=` — Number of parallel threads (default: `3`)
- `-DparallelMode=` — Parallel execution mode (possible values: `methods`, `classes` ; default: `methods`)
- `-DapiLogsEnabled=` — Enable/disable API request/response logging (default: `true`)

### Via IDE (e.g., IntelliJ IDEA)
1. Open the project in your IDE.
2. Navigate to `src/test/resources/testng.xml`.
3. Right-click on `testng.xml` and select "Run 'testng.xml'".

### How to Generate and View Allure Report
```bash
mvn allure:serve
```

### Log Files
Log files are generated in the `logs/` directory after test execution.

### TestNG configuration
The TestNG configuration file is located at `src/test/resources/testng.xml`.
You can modify it to include/exclude specific tests or suites as needed.

---

## 📝 Test Cases Covered Endpoints

- `POST /player/create/{editor}` – Create player
- `DELETE /player/delete/{editor}` – Delete player
- `PATCH /player/update/{editor}/{id}` – Update player
- `POST /player/get` – Get player by ID
- `GET /player/get/all` – Get all players
---

## 🐞 Found Issues
A detailed list of found issues can be found in the [FOUND_ISSUES.md](FOUND_ISSUES.md) file.

## 👤 Author

**Yury Bahamya**    
Senior Software Test Automation Engineer
[LinkedIn →](https://www.linkedin.com/in/yury-bogomya-a3754a77/)