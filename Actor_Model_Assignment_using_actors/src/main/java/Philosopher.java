import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Philosopher extends UntypedActor {
    public static Props mkProps(String aName, ActorRef aWeiter) {
        return Props.create(Philosopher.class, aName, aWeiter);
    }

    private String name;
    private ActorRef waiter;
    private static final int THINK_TIME = 3000;// 3 seconds
    private static final int EAT_TIME = 3000; //3 seconds

    private Philosopher(String aName, ActorRef aWaiter) {
        name = aName;
        waiter = aWaiter;
        // Let`s introduce ourselves to Waiter
        aWaiter.tell(new Messages.Introduce(aName), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Messages.Think) {
            System.out.println(name + " thinking");
            Thread.sleep(THINK_TIME);
            System.out.println(name + " gets hungry");
            waiter.tell(new Messages.Hungry(), getSelf());

        } else if (message instanceof Messages.Eat) {
            System.out.println(name + " eating");
            Thread.sleep(EAT_TIME);
            System.out.println(name + " fed up");
            waiter.tell(new Messages.FinishEat(), getSelf());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create();
        final int fork = 5;
        //creating actors here
        ActorRef waiter = system.actorOf(Waiter.mkProps(fork));
        ActorRef actors[] = new ActorRef[5];
        for(int i=0; i<5; i++)
        {
            actors[i] = system.actorOf(Philosopher.mkProps("Philosopher "+(i+1), waiter));
        }
    }
}
