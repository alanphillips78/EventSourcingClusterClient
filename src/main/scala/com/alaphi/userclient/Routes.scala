package com.alaphi.userclient

import akka.http.scaladsl.server.Directives._
import scala.util.{Failure, Success}
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

class Routes(userMessageGeneratorService: UserMessageGeneratorService) {

  val userRoutes = {
    path("user" / "generate") {
      post {
        entity(as[GenerateUserData]) { generateUserData =>
          onComplete(userMessageGeneratorService.startGenerating(generateUserData.numMsgs)) {
            case Success(s) => complete(s)
            case Failure(f) => complete(BadRequest -> s"Failed: $f ")
          }
        }
      }
    }
  }

}

object Routes {
  def apply(userMessageGeneratorService: UserMessageGeneratorService): Routes = new Routes(userMessageGeneratorService)
}