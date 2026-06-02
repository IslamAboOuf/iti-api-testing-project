package tests;

import base.BaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class InstructorTests extends BaseTest {

    @Test
    public void shouldReturnInstructorProfile_whenInstructorIdIsValid() {
        test = extent.createTest("shouldReturnInstructorProfile_whenInstructorIdIsValid");
        given()
                .when()
                .get("/api/Instructor/profile/" + VALID_INS_ID)
                .then()
                .statusCode(200)
                .body("salary", notNullValue());
    }

    @Test
    public void shouldReturnNotFound_whenInstructorIdDoesNotExist() {
        test = extent.createTest("shouldReturnNotFound_whenInstructorIdDoesNotExist");
        given()
                .when()
                .get("/api/Instructor/profile/" + INVALID_ID)
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldReturnNotFound_whenInstructorHasNoExams() {
        test = extent.createTest("shouldReturnNotFound_whenInstructorHasNoExams");
        given()
                .when()
                .get("/api/Instructor/" + INVALID_ID + "/exams")
                .then()
                .statusCode(404);
    }

    @Test(groups = "examReady")
    public void shouldGenerateExamSuccessfully_whenValidCourseProvided() {
        test = extent.createTest("shouldGenerateExamSuccessfully_whenValidCourseProvided");
        int newExamId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                        {"courseID":"1",
                        "mcqCount":"15",
                        "tfCount":"5",
                        "duration":"60",
                        "instructorId":"1"}""")
                        .when()
                        .post("/api/Exam/generate-exam")
                        .then()
                        .statusCode(200)
                        .extract().path("examId");

        BaseTest.examId = newExamId;
        System.out.println("✅ Generated Exam ID: " + BaseTest.examId);
    }

    @Test
    public void shouldReturnNotFound_whenCourseDoesNotExist() {
        test = extent.createTest("shouldReturnNotFound_whenCourseDoesNotExist");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"courseID":"999",
                        "mcqCount":"15",
                        "tfCount":"5",
                        "duration":"60",
                        "instructorId":"1"}""")
                .when()
                .post("/api/Exam/generate-exam")
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldReturnBadRequest_whenRequestedQuestionsExceedAvailable() {
        test = extent.createTest("shouldReturnBadRequest_whenRequestedQuestionsExceedAvailable");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"courseID":"1",
                        "mcqCount":"50",
                        "tfCount":"25",
                        "duration":"60",
                        "instructorId":"1"}""")
                .when()
                .post("/api/Exam/generate-exam")
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldReturnBadRequest_whenQuestionCountIsZero() {
        test = extent.createTest("shouldReturnBadRequest_whenQuestionCountIsZero");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"courseID":"1",
                        "mcqCount":"0",
                        "tfCount":"0",
                        "duration":"60",
                        "instructorId":"1"}""")
                .when()
                .post("/api/Exam/generate-exam")
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldReturnBadRequest_whenRequestBodyIsEmpty() {
        test = extent.createTest("shouldReturnBadRequest_whenRequestBodyIsEmpty");
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/api/Exam/generate-exam")
                .then()
                .statusCode(400);
    }
}