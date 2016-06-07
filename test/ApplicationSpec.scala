import org.scalatestplus.play._
import play.api.Application
import play.api.libs.json.{JsObject, Json, Reads}
import play.api.test._
import play.api.test.Helpers._


class ApplicationSpec extends PlaySpec with OneAppPerTest {

  case class TokenResponse(token_type : String, access_token : String, expires_in : Int, refresh_token : String)
  implicit val tokenReader : Reads[TokenResponse] = Json.reads[TokenResponse]

  case class TokenErrorResponse(error : String, error_description : String)
  implicit val TokenErrorResponseReader : Reads[TokenErrorResponse] = Json.reads[TokenErrorResponse]

  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "OAuthController" should {

    "return unauthorized when trying to access resources without token" in {
      val resources = route(app, FakeRequest(GET, "/resources")).get

      status(resources) mustBe BAD_REQUEST
    }

    "return unauthorized when trying to access resources without a valid token" in {
      val resources = route(app, FakeRequest(GET, "/resources", FakeHeaders(
        ("Content-type", "application/json") ::("Authorization", "Bearer faketoken") :: Nil), JsObject(Seq()))).get

      status(resources) mustBe UNAUTHORIZED
    }

    "return an error with invalid client when try to create a token with invalid credentials" in {
      val resources = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "wrong id",
        "client_secret" -> "wrong secret", "grant_type" -> "client_credentials")).get

      status(resources) mustBe UNAUTHORIZED

      val tokenErrorResponse = contentAsJson(resources).as[TokenErrorResponse]

      tokenErrorResponse.error mustBe "invalid_client"

    }

    "return an error with invalid client when try to create a token with invalid authorizationCode" in{
      val resources = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id",
        "client_secret" -> "bob_client_secret", "redirect_uri" -> "http://localhost:3000/callback", "code" -> "wrong code", "grant_type" -> "authorization_code")).get

      status(resources) mustBe UNAUTHORIZED

      val tokenErrorResponse = contentAsJson(resources).as[TokenErrorResponse]

      tokenErrorResponse.error mustBe "invalid_client"

    }

    "return a valid token and refresh token with valid credentials" in {
      val tokenResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id",
        "client_secret" -> "bob_client_secret", "grant_type" -> "client_credentials")).get

      status(tokenResp) mustBe OK

      val tokenResponse = contentAsJson(tokenResp).as[TokenResponse]

      tokenResponse.token_type mustBe "Bearer"
      tokenResponse.access_token must not be(tokenResponse.refresh_token)
    }

    "return a valid token when try to create a token with valid password" in {
      val resources = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "alice_client_id2",
        "client_secret" -> "alice_client_secret2", "grant_type" -> "password", "username" -> "alice@example.com", "password" -> "alice")).get

      status(resources) mustBe OK

      val tokenResponse = contentAsJson(resources).as[TokenResponse]
      tokenResponse.token_type mustBe "Bearer"
      tokenResponse.access_token must not be(tokenResponse.refresh_token)

    }

    "return a valid token when try to create a token with authorizationCode" in{
      val resources = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "alice_client_id",
        "client_secret" -> "alice_client_secret", "redirect_uri" -> "http://localhost:3000/callback", "code" -> "bob_code", "grant_type" -> "authorization_code")).get

      status(resources) mustBe OK

      val tokenResponse = contentAsJson(resources).as[TokenResponse]
      tokenResponse.token_type mustBe "Bearer"
      tokenResponse.access_token must not be(tokenResponse.refresh_token)

    }

    "return error when try to refresh the token with an invalid token" in {
      val tokenResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id",
        "client_secret" -> "bob_client_secret", "grant_type" -> "client_credentials")).get
      status(tokenResp) mustBe OK
      val tokenResponse = contentAsJson(tokenResp).as[TokenResponse]

      val refreshResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id", "client_secret" -> "bob_client_secret",
        "refresh_token" -> "Invalid token", "grant_type" -> "refresh_token")).get

      val refreshResponse = contentAsJson(refreshResp).as[TokenErrorResponse]
      refreshResponse.error mustBe "invalid_request"
    }

    "return error when try to refresh the token with a valid token but wrong credentials" in {
      val tokenResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id",
        "client_secret" -> "bob_client_secret", "grant_type" -> "client_credentials")).get
      status(tokenResp) mustBe OK
      val tokenResponse = contentAsJson(tokenResp).as[TokenResponse]

      val refreshResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id", "client_secret" -> "Invalid bob_client_secret",
        "refresh_token" -> tokenResponse.refresh_token, "grant_type" -> "refresh_token")).get

      val refreshResponse = contentAsJson(refreshResp).as[TokenErrorResponse]
      refreshResponse.error mustBe "invalid_client"
    }

    "return a new token after refresh" in {
      val tokenResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id",
        "client_secret" -> "bob_client_secret", "grant_type" -> "client_credentials")).get
      status(tokenResp) mustBe OK
      val tokenResponse = contentAsJson(tokenResp).as[TokenResponse]

      val refreshResp = route(app, FakeRequest(POST, "/oauth/access_token").withFormUrlEncodedBody("client_id" -> "bob_client_id", "client_secret" -> "bob_client_secret",
        "refresh_token" -> tokenResponse.refresh_token, "grant_type" -> "refresh_token")).get

      val refreshResponse = contentAsJson(refreshResp).as[TokenResponse]
      refreshResponse.token_type mustBe "Bearer"
      refreshResponse.access_token must not be(tokenResponse.refresh_token)
    }

  }

}
