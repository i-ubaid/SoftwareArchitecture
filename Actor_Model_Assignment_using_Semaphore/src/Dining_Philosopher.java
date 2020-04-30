import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Dining_Philosopher {

    static int philosopher = 5; // To initialize both Philosophers and chopsticks arrays
    static philosopher philosophers[] = new philosopher[philosopher];
    static chopstick chopsticks[] = new chopstick[philosopher];

    static class chopstick {

        public Semaphore mutex = new Semaphore(1); //using semaphore here

        void grab() {
            try {
                mutex.acquire();
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        void release() {
            mutex.release();
        }

        boolean isFree() {
            return mutex.availablePermits() > 0;
        }

    }

    static class philosopher extends Thread {

        public int number;
        public chopstick leftchopstick;
        public chopstick rightchopstick;

        philosopher(int num, chopstick left, chopstick right) {
            number = num;
            leftchopstick = left;
            rightchopstick = right;
        }

        public void run(){

            while (true) {
                leftchopstick.grab();
                System.out.println("philosopher " + (number+1) + " grabs left chopstick.");
                rightchopstick.grab();
                System.out.println("philosopher " + (number+1) + " grabs right chopstick.");
                eat();
                leftchopstick.release();
                System.out.println("philosopher " + (number+1) + " releases left chopstick.");
                rightchopstick.release();
                System.out.println("philosopher " + (number+1) + " releases right chopstick.");
            }
        }

        void eat() {
            try {
                int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);//Selecting a random number for a philosopher to eat.
                System.out.println("philosopher " + (number+1) + " eats for " + sleepTime);
                Thread.sleep(sleepTime);
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

    }

    public static void main(String args[]) {
        //iNITIALIZING CHOPSTICK ARRAY
        for (int i = 0; i < philosopher; i++) {
            chopsticks[i] = new chopstick();
        }
        //INITIALIZING PHILOSOPHER ARRAY HERE
        //HERE TRYING TO IMPLEMENT THAT EACH PHILOSOPHER HAVE A CHOPSTICK AT LEFT AND RIGHT CHOPSTICK BELONGS TO OTHER PHILOSOPHERS
        for (int i = 0; i < philosopher; i++) {
            philosophers[i] = new philosopher(i, chopsticks[i], chopsticks[(i + 1) % philosopher]);
            philosophers[i].start();//Starting the threads here
        }

        while (true) {
            try {
                // sleep 1 sec
                Thread.sleep(1000);//putting threads to sleep

                // check for deadlock
                boolean deadlock = true;
                for (chopstick f : chopsticks) {
                    if (f.isFree()) {
                        deadlock = false;
                        break;
                    }
                }
                if (deadlock) {
                    Thread.sleep(1000);
                    System.out.println("Deadlock Occured ");
                    System.out.println("No One can Eat");
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        System.exit(0);
    }

}