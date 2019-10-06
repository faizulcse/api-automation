package testcases;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    @Test
    public void test(){
        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                //.log().all()
                .param("id",3)
                .when().get("/api/users")
                .then()
                .extract()
                .response();

        // print response data
        System.out.println(response.statusCode());
        response.prettyPrint();

        // check response data using RestAssured
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("data.id", equalTo(3));
        response.then().assertThat().body("data.first_name", equalTo("Emma"));
        response.then().assertThat().body("data.last_name", equalTo("Wong"));
        response.then().assertThat().body("data.email", equalTo("emma.wong@reqres.in"));

        // check response data using testNG
        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(jsonPath.get("data.id"), 3);
        Assert.assertEquals(jsonPath.get("data.first_name"),"Emma");
        Assert.assertEquals(jsonPath.get("data.last_name"),"Wong");
        Assert.assertEquals(jsonPath.get("data.email"),"emma.wong@reqres.in");
    }

    @Test
    public void postMethod(){
        String payload = " {\n" +
                "        \"name\": \"Faizul\",\n" +
                "        \"post\": \"SQA\",\n" +
                "        \"address\":{\n" +
                "            \"road\": \"4\",\n" +
                "            \"house\": \"38/4\",\n" +
                "            \"city\": \"Dhaka\"\n" +
                "        }\n" +
                "    }";
        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                .body(payload)
                .when().post("/api/users")
                .then()
                .extract()
                .response();

        System.out.println(response.statusCode());
        response.prettyPrint();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(jsonPath.get("size()"));
        System.out.println(jsonPath.get("id"));
    }

    @Test
    public void all_First_Name(){
        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                //.log().all()
                .param("page",1)
                .when().get("/api/users")
                .then()
                .extract()
                .response();

        System.out.println(response.statusCode());
        response.prettyPrint();

        JsonPath jsonPath = new JsonPath(response.asString());
        int size = jsonPath.get("data.size()");

        for(int i = 0; i<size; i++){
            System.out.println(jsonPath.get("data["+i+"].first_name"));
        }
    }

    @Test
    public void cCheckServer(){
        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                //.log().all()
                .param("id",4)
                .when().get("api/users")
                .then()
                .extract()
                .response();

        System.out.println(response.statusCode());
        System.out.println(response.header("Server"));
        System.out.println(response.contentType());
        response.prettyPrint();
    }
}
