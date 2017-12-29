package services

import scala.concurrent.Future
import javax.inject._
import play.api.inject.ApplicationLifecycle

@Singleton
class Bootstrap @Inject() (lifecycle: ApplicationLifecycle) {
  println("Executing some code at startup")

  lifecycle.addStopHook { () =>
    Future.successful()
  }
}
