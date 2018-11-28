import akka.actor.Actor

class CreatingActor extends Actor {
  override def receive: Receive = {
    case msg:String => println(msg+" "+self.path.name)
  }
}

