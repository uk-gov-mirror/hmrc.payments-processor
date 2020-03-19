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

package pp.connectors.des

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}
import pp.model.{ChargeRefNotificationDesRequest, TaxTypes}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClient,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("des")
  private val authorizationToken: String = configuration.underlying.getString("microservice.services.des.authorizationToken")
  private val serviceEnvironment: String = configuration.underlying.getString("microservice.services.des.environment")
  private val chargeref: String = configuration.underlying.getString("microservice.services.des.chargeref-url")

  private val desHeaderCarrier: HeaderCarrier = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorizationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment, "OriginatorID" -> "MDTP")

  def sendCardPaymentsNotification(chargeRefNotificationDesRequest: ChargeRefNotificationDesRequest): Future[HttpResponse] = {
    Logger.debug(s"Calling des api 1541 for chargeRefNotificationDesRequest ${chargeRefNotificationDesRequest.toString}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val sendChargeRefUrl: String = s"$serviceURL$chargeref"
    Logger.debug(s"""Calling des api 1541 with url $sendChargeRefUrl""")

    //TODO do we really need CDS and CDSX??
    val desRequest =
      if (chargeRefNotificationDesRequest.taxType == TaxTypes.CDSX) chargeRefNotificationDesRequest.copy(taxType = TaxTypes.CDS)
      else chargeRefNotificationDesRequest

    httpClient.POST[ChargeRefNotificationDesRequest, HttpResponse](sendChargeRefUrl, desRequest)
  }

}
