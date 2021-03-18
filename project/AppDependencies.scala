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

import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc"  %% "simple-reactivemongo"      % "7.31.0-play-27",
    "uk.gov.hmrc"  %% "work-item-repo"            % "7.11.0-play-27",
    "uk.gov.hmrc"  %% "play-hmrc-api"             % "6.2.0-play-27",
    "uk.gov.hmrc"  %% "play-scheduling-play-27"   % "7.10.0",
    "uk.gov.hmrc"  %% "bootstrap-backend-play-27" % "3.3.0",
    "com.beachape" %% "enumeratum"                % "1.5.15"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-backend-play-27" % "3.3.0"  % Test,
    "org.scalatest"          %% "scalatest"                 % "3.2.1"  % Test,
    "com.typesafe.play"      %% "play-test"                 % current  % Test,
    "org.pegdown"            %  "pegdown"                   % "1.6.0"  % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"        % "3.1.2"  % Test,
    "com.github.tomakehurst" %  "wiremock-jre8"             % "2.27.2" % Test
  )

  val itTest = Seq(
    "uk.gov.hmrc"            %% "bootstrap-backend-play-27" % "3.3.0"  % "it",
    "org.scalatest"          %% "scalatest"                 % "3.2.1"  % "it",
    "com.typesafe.play"      %% "play-test"                 % current  % "it",
    "org.pegdown"            %  "pegdown"                   % "1.6.0"  % "it",
    "org.scalatestplus.play" %% "scalatestplus-play"        % "3.1.2"  % "it",
    "com.github.tomakehurst" %  "wiremock-jre8"             % "2.27.2" % "it"
  )

}
