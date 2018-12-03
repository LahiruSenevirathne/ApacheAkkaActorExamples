import akka.NotUsed
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, Materializer, OverflowStrategy}
import org.reactivestreams.Publisher

object AkkaStreamingWithPublisher extends App {
  implicit val system: akka.actor.ActorSystem = ActorSystem("KinesisActor")
  implicit val mat: Materializer = ActorMaterializer()

  val actor: ActorRef = system.actorOf(Props(new Actor {
    override def receive: PartialFunction[Any, Unit] = {
      case msg:String => { println(msg)
      }
    }
  }))
  val (ref: ActorRef, publisher: Publisher[String]) =
    Source.actorRef[String](bufferSize = 100000, OverflowStrategy.dropBuffer)
      .toMat(Sink.asPublisher(true))(Keep.both).run()
  val source: Source[String, NotUsed] =Source.fromPublisher(publisher)
  val sink: Sink[String, NotUsed] =Sink.actorRef[String](actor,onCompleteMessage = "send")

  val flow =  source to sink

  while (true) {
    for (i <- 0 until 5000) yield ref ! "akka"

  }

  flow.run()

}
