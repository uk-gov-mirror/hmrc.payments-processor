/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pp.controllers

import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import support.ItSpec
import uk.gov.hmrc.play.bootstrap.http.ErrorResponse

import scala.concurrent.Future

class HeaderValidatorSpec extends ItSpec with HeaderValidator {

  private lazy val parse = injector.instanceOf[PlayBodyParsers]

  "acceptHeaderValidationRules return false when the header value is missing" in {
    acceptHeaderValidationRules(None) shouldBe false
  }

  "acceptHeaderValidationRules return true when the version and the content type in header value is well formatted" in {
    acceptHeaderValidationRules(Some("application/vnd.hmrc.1.0+json")) shouldBe true
  }

  "acceptHeaderValidationRules return false when the content type in header value is missing" in {
    acceptHeaderValidationRules(Some("application/vnd.hmrc.1.0")) shouldBe false
  }

  "acceptHeaderValidationRules return false when the content type in header value is not well formatted" in {
    acceptHeaderValidationRules(Some("application/vnd.hmrc.v1+json")) shouldBe false
  }

  "acceptHeaderValidationRules return false when the content type in header value is not valid" in {
    acceptHeaderValidationRules(Some("application/vnd.hmrc.notvalid+XML")) shouldBe false
  }

  "return false when the version in header value is not valid" in {
    acceptHeaderValidationRules(Some("application/vnd.hmrc.notvalid+json")) shouldBe false
  }

  val standardResponse: Request[AnyContentAsEmpty.type] => Future[Status] = { _: Request[AnyContentAsEmpty.type] => Future.successful(Ok) }

  "validateAccept return an OK result when the accept header is present" in {

    val request = FakeRequest().withHeaders(("Accept", "application/json"))
    val result = validateAccept(_.isDefined, parse).invokeBlock(request, standardResponse).futureValue

    status(result) shouldBe OK
  }

  "validateAccept return an error result when the accept header is not present" in {

    val request = FakeRequest()
    val result = validateAccept(_.isDefined, parse).invokeBlock(request, standardResponse)

    val json = contentAsJson(result)

    val errorResponse = json.as[ErrorResponse]
    errorResponse.statusCode shouldBe NOT_ACCEPTABLE
    errorResponse.message shouldBe Constants.acceptHeaderMissing

  }


}
