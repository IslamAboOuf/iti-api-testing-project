package tests;

import base.BaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class StudentTests extends BaseTest {

    @Test
    public void shouldReturnStudentProfile_whenStudentIdIsValid() {
        test = extent.createTest("shouldReturnStudentProfile_whenStudentIdIsValid");
        given()
                .when()
                .get("/api/Student/" + VALID_STUDENT_ID + "/profile")
                .then()
                .statusCode(200)
                .body("email", notNullValue());
    }

    @Test
    public void shouldReturnNotFound_whenStudentIdDoesNotExist() {
        test = extent.createTest("shouldReturnNotFound_whenStudentIdDoesNotExist");
        given()
                .when()
                .get("/api/Student/" + INVALID_ID + "/profile")
                .then()
                .statusCode(404);
    }

    @Test(dependsOnGroups = "examReady")
    public void shouldSubmitExamSuccessfully_whenValidAnswersProvided() {
        test = extent.createTest("shouldSubmitExamSuccessfully_whenValidAnswersProvided");
        int newStudentExamId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                        [{"quesId":2,
                        "choiceId":2},
                        {"quesId":3,
                        "choiceId":2}]""")
                        .when()
                        .post("/api/Exam/" + BaseTest.examId +
                                "/submit/" + BaseTest.VALID_STUDENT_ID)
                        .then()
                        .statusCode(200)
                        .extract().path("studentExamID[0].newStudentExamId");

        BaseTest.studentExamId = newStudentExamId;
        System.out.println("✅ Student Exam ID: " + BaseTest.studentExamId);
    }

    @Test(dependsOnMethods = "shouldSubmitExamSuccessfully_whenValidAnswersProvided")
    public void shouldReturnBadRequest_whenSubmittingExamTwice() {
        test = extent.createTest("shouldReturnBadRequest_whenSubmittingExamTwice");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        [{"quesId":2,
                        "choiceId":2},
                        {"quesId":3,
                        "choiceId":2}]""")
                .when()
                .post("/api/Exam/" + BaseTest.examId +
                        "/submit/" + BaseTest.VALID_STUDENT_ID)
                .then()
                .statusCode(400)
                .body(containsString("already"));
    }

    @Test
    public void shouldReturnNotFound_whenExamDoesNotExist() {
        test = extent.createTest("shouldReturnNotFound_whenExamDoesNotExist");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        [{"quesId":2,
                        "choiceId":2},
                        {"quesId":3,
                        "choiceId":2}]""")
                .when()
                .post("/api/Exam/" + BaseTest.INVALID_ID +
                        "/submit/" + BaseTest.VALID_STUDENT_ID)
                .then()
                .statusCode(404);
    }

    @Test(dependsOnMethods = "shouldSubmitExamSuccessfully_whenValidAnswersProvided")
    public void shouldReturnGradeSuccessfully_whenExamIsSubmitted() {
        test = extent.createTest("shouldReturnGradeSuccessfully_whenExamIsSubmitted");
        given()
                .when()
                .get("/api/Exam/grade/" + BaseTest.studentExamId)
                .then()
                .statusCode(200)
                .body("scoreAchieved", notNullValue())
                .body("totalPossibleScore", notNullValue())
                .body("finalGradePercentage", notNullValue());
    }

    @Test(dependsOnMethods = "shouldSubmitExamSuccessfully_whenValidAnswersProvided")
    public void shouldAllowExamReview_whenSubmissionExists() {
        test = extent.createTest("shouldAllowExamReview_whenSubmissionExists");
        given()
                .when()
                .get("/api/Exam/Review/" + BaseTest.studentExamId)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnGroups = "examReady")
    public void shouldNotExposeCorrectAnswers_whenFetchingExamQuestions() {
        test = extent.createTest("shouldNotExposeCorrectAnswers_whenFetchingExamQuestions");
        String response = given()
                .when()
                .get("/api/Exam/" + BaseTest.examId)
                .then()
                .statusCode(200)
                .extract().asString();

        org.testng.Assert.assertFalse(
                response.toLowerCase().contains("iscorrect"),
                "SECURITY BUG: isCorrect field is exposed!"
        );
    }
}