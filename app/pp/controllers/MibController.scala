/*
 * Copyright 2021 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import play.api.{Configuration, Logger}
import pp.config.MibOpsQueueConfig
import pp.connectors.MibConnector
import pp.controllers.retries.MibRetries
import pp.services.MibOpsService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.ExecutionContext

@Singleton
class MibController @Inject() (
    cc:                    ControllerComponents,
    val mibOpsQueueConfig: MibOpsQueueConfig,
    val configuration:     Configuration,
    val mibOpsService:     MibOpsService,
    val mibConnector:      MibConnector

)
  (implicit val executionContext: ExecutionContext) extends BackendController(cc) with MibRetries {

  val logger: Logger = Logger(this.getClass.getSimpleName)

  def paymentCallBack(reference: String): Action[AnyContent] = Action.async { implicit request =>
    logger.debug("sendStatusUpdateToMib")
    sendPaymentUpdateToMib(reference)
  }
}

