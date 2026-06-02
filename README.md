## 🤖 API Automation Testing

This project includes an API Automation Testing framework for the ITI Examination System using Rest Assured, TestNG, and Maven.

### 🎯 Purpose
- Automate API test scenarios (positive & negative cases)
- Validate response status codes and business logic
- Detect functional and security issues
- Integrate with CI/CD pipeline using GitHub Actions

### 🧰 Tech Stack
- Java
- Rest Assured
- TestNG
- Maven
- GitHub Actions
- Extent Reports
- Allure Report

### ⚙️ Key Concepts
- Dynamic data handling (examId, studentExamId)
- TestNG groups & dependencies
- Request/Response validation
- Negative testing scenarios
- CI/CD integration

### 📊 Execution Summary
- Total Test Cases: 23
- Passed: 17
- Failed: 6
- Success Rate: ~69.5%

### ❌ Failures Note
All failed test cases represent real API issues such as:
- 500 Internal Server Errors
- Incorrect HTTP status codes
- Missing validation logic
- Security issue (`isCorrect` field exposure)

### 🚀 CI/CD
The framework runs automatically on every push using GitHub Actions:
- Build with Maven
- Execute TestNG suite
- Fail pipeline on test failures
