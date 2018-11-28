import akka.actor.{ActorSystem, Props}

object Main{
  def main(args: Array[String]): Unit = {
    var actorSystem = ActorSystem("ActorSystem")
    var actor = actorSystem.actorOf(Props[CreatingActor],"PropExample")
    actor ! "Huuuuuuu"
  }
}