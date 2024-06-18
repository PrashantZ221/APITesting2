package com.testautomation.apitesting.tests;

import com.testautomation.apitesting.tests.utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class PostAPIRequest extends BaseTest {

    @Test
    public void createBooking(){
        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); There in BaseTest Class

        //prepare request body
        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        booking.put("firstname", "api testing");
        booking.put("lastname", "tutorial");
        booking.put("totalprice", 1000);
        booking.put("depositpaid", true);
        booking.put("additionalneeds", "breakfast");
        booking.put("bookingdates", bookingDates);

        bookingDates.put("checkin", "2023-03-25");
        bookingDates.put("checkout", "2023-03-30");

        Response response =
                RestAssured
                        .given()
                            .contentType(ContentType.JSON)
                            .body(booking.toString())
                            .baseUri("https://restful-booker.herokuapp.com/booking")
                        //.log().all()
                        //.log().body()
                        //.log().headers()
                        .when()
                            .post()
                        .then()
                            .assertThat()
                            //.log().body()
                            //.log().headers()
                            //.log().ifValidationFails()
                            .statusCode(200)
                            .body("booking.firstname", Matchers.equalTo("api testing"))
                            .body("booking.totalprice", Matchers.equalTo(1000))
                            .body("booking.bookingdates.checkin", Matchers.equalTo("2023-03-25"))
                            .extract()
                            .response();

        int bookingId = response.path("bookingid");

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .pathParam("bookingID", bookingId)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                    .get("{bookingID}")
                .then()
                     .body("lastname", Matchers.equalTo("tutorial"));
    }

}
