import org.scalatestplus.play._
import play.api.libs.json.JsObject
import play.api.test._
import play.api.test.Helpers._


class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

    "OAuthController" should {

      "return unauthorized when trying to access resources without token" in {
        val resources = route(app, FakeRequest(GET, "/resources")).get

        status(resources) mustBe BAD_REQUEST
      }

      "return unauthorized when trying to access resources without a valid token" in {
        val resources = route(app, FakeRequest(GET, "/resources",FakeHeaders(
          ("Content-type","application/json") :: ("Authorization","Bearer faketoken") :: Nil),JsObject(Seq()))).get

        status(resources) mustBe UNAUTHORIZED
      }

    }

}