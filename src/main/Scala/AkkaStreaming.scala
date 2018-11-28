import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}

import scala.concurrent.Future

object AkkaStreaming extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  val source: Source[Int,NotUsed] = Source(1 to 100)
  val done: Future[Done] = source.runForeach(i=> println(i))(materializer)
  implicit val ec = system.dispatcher
  done.onComplete(_ ⇒ system.terminate())

  val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc*next)

  val result: Future[IOResult] =
    factorials
      .map(num ⇒ ByteString(s"$num\n"))
      .runWith(FileIO.toPath(Paths.get("factorials.txt")))

}
