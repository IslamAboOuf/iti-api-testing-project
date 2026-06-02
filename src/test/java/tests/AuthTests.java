package tests;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BaseTest.BASE_URL_LOCAL;
    }

    @Test
    public void shouldLoginSuccessfully_whenStudentCredentialsAreValid() {
        test = extent.createTest("shouldLoginSuccessfully_whenStudentCredentialsAreValid");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"ali.khaled@student.com",
                        "password":"123456",
                        "role":"student"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(200)
                .body("email", equalTo("ali.khaled@student.com"));
    }

    @Test
    public void shouldLoginSuccessfully_whenInstructorCredentialsAreValid() {
        test = extent.createTest("shouldLoginSuccessfully_whenInstructorCredentialsAreValid");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"dr.ahmed.hassan@iti.gov.eg",
                        "password":"123456",
                        "role":"instructor"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(200)
                .body("email", equalTo("dr.ahmed.hassan@iti.gov.eg"));
    }

    @Test
    public void shouldReturnUnauthorized_whenStudentEmailIsInvalid() {
        test = extent.createTest("shouldReturnUnauthorized_whenStudentEmailIsInvalid");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"alikhaled@student.com",
                        "password":"123456",
                        "role":"student"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorized_whenStudentPasswordIsInvalid() {
        test = extent.createTest("shouldReturnUnauthorized_whenStudentPasswordIsInvalid");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"ali.khaled@student.com",
                        "password":"12345678",
                        "role":"student"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorized_whenRoleIsIncorrect() {
        test = extent.createTest("shouldReturnUnauthorized_whenRoleIsIncorrect");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"ali.khaled@student.com",
                        "password":"123456",
                        "role":"instructor"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldAllowLogin_whenEmailCaseIsDifferent() {
        test = extent.createTest("shouldAllowLogin_whenEmailCaseIsDifferent");
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"email":"ALI.KHALED@STUDENT.COM",
                        "password":"123456",
                        "role":"student"}""")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldReturnBadRequest_whenRequestBodyIsEmpty() {
        test = extent.createTest("shouldReturnBadRequest_whenRequestBodyIsEmpty");
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/api/Account/login")
                .then()
                .statusCode(400);
    }

}