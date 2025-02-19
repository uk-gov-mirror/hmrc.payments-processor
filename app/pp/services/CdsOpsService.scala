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

package pp.services

import java.time.{Clock, LocalDateTime, ZoneId}

import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.Logger
import play.api.mvc.Results
import pp.config.CdsOpsQueueConfig
import pp.connectors.CdsConnector
import pp.model.cds.{NotificationCds, NotifyImmediatePaymentRequest}
import pp.model.wokitems.CdsOpsWorkItem
import pp.model.{Origins, TaxTypes}
import pp.scheduling.cds.CdsOpsMongoRepo
import uk.gov.hmrc.workitem.WorkItem

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CdsOpsService @Inject()(
                               val repo: CdsOpsMongoRepo,
                               val queueConfig: CdsOpsQueueConfig,
                               cdsConnector: CdsConnector,
                               val clock: Clock,
                             )(implicit val executionContext: ExecutionContext) extends WorkItemService[CdsOpsWorkItem] with Results {

  val logger: Logger = Logger(this.getClass.getSimpleName)

  //These are all specific to cds processing
  def sendWorkItem(workItem: WorkItem[CdsOpsWorkItem]): Future[Unit] = {

    logger.debug("inside sendWorkItemToCdsOps")
    for {
      _ <- cdsConnector.paymentCallback(workItem.item.notificationCds)
    } yield ()

  }


  def sendCdsOpsToWorkItemRepo(notificationCds: NotificationCds): Future[WorkItem[CdsOpsWorkItem]] = {
    logger.debug("inside sendCardPaymentsNotificationAsync")
    val time = LocalDateTime.now(clock)
    val jodaLocalDateTime = new DateTime(time.atZone(ZoneId.systemDefault).toInstant.toEpochMilli)
    val workItem = CdsOpsWorkItem(time, availableUntil(time), TaxTypes.cds, Origins.OPS, reference = notificationCds.notifyImmediatePaymentRequest.requestDetail.paymentReference, notificationCds)
    repo.pushNew(workItem, jodaLocalDateTime)

  }


}