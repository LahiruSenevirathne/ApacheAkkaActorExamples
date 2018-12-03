import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Future

object akkastreaming2 extends App {
  implicit val system = ActorSystem("TestSystem")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  // Source create multiple ways
  val s = Source.empty
  val s1 = Source.single("Akka")
  val s2 = Source(1 to 3)

  s runForeach println
  s1 runForeach println
  s2 runForeach println

  val infiniteDatasource = Source.repeat("sha")

  infiniteDatasource take 3 runForeach println

  def run(actor: ActorRef) = {
    Future { Thread.sleep(300); actor ! 1 }
    Future { Thread.sleep(200); actor ! 2 }
    Future { Thread.sleep(100); actor ! 3 }

  }

  val ss = Source.actorRef[Int](bufferSize = 0,OverflowStrategy.fail).mapMaterializedValue(run)
  ss runForeach println

}
