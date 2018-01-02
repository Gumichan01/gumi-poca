package services

import scala.concurrent.Future
import javax.inject._
import play.api.inject.ApplicationLifecycle

import model.Server

@Singleton
class Bootstrap @Inject() (lifecycle: ApplicationLifecycle) {
  println("Executing some code at startup")

  Server.addStartData

  lifecycle.addStopHook { () =>
    Future.successful()
  }
}
