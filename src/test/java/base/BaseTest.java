package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;


public class BaseTest {

    public static final String BASE_URL_LOCAL  = "http://localhost:5091";
    public static final String BASE_URL_REMOTE = "https://iti-exam-system.runasp.net";

    protected static final int VALID_STUDENT_ID = 2;
    protected static final int VALID_INS_ID     = 1;
    protected static final int VALID_COURSE_ID  = 10;
    protected static final int INVALID_ID       = 999;

    public static int examId;
    public static int studentExamId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL_REMOTE;
    }

}