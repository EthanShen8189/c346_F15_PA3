import java.util.concurrent.locks.Condition;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
    public static int NbOfChopsticks;
    private enum State{eating, hungry, thinking}       //setup monitor states
    State[] states;
    boolean isTalking =false;
    /**
     * Constructor
     */
    public Monitor(int piNumberOfPhilosophers)
    {
        NbOfChopsticks = piNumberOfPhilosophers;
        states = new State[NbOfChopsticks];


        for(int i=0; i< NbOfChopsticks; i++){       //initialize all philosophers to thinking
            states[i] = State.thinking;

        }


    }

    /**
     * if the left and right of current philosopher is not eating.
     * then let current philosopher to eat.
     * else notify other to proceed.
     * @param piTID
     */
    private void eatTest(int piTID){

             if((states[(piTID+states.length-1) % states.length] != State.eating)
                     &&(states[piTID] == State.hungry)
                     &&(states[(piTID+1)%states.length] != State.eating))
                 states[piTID] = State.eating;

            this.notify();

    }


    /**
     * Grants request (returns) to eat when both chopsticks/forks are available.
     * Else forces the philosopher to wait()
     */
    public synchronized void pickUp(final int piTID) throws InterruptedException
    {
        int current = piTID-1;
        states[current] = State.hungry;
        eatTest(current);
        while(states[current]!=State.eating)
            try {
                System.out.println("Philosopher " + piTID + " is waiting for chopsticks");
                this.wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int piTID) throws InterruptedException
    {
        int current = piTID -1;
        states[current] = State.thinking;
        eatTest((current + (states.length-1)) % states.length);
        eatTest((current + 1) % states.length);

    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk() throws InterruptedException
    {
        while (isTalking)
            this.wait();

        isTalking = true;
    }

    /**
     * When one philosopher is done talking stuff, others
     * can feel free to start talking.
     */
    public synchronized void endTalk()
    {
        isTalking = false;
        this.notify();
    }
}

// EOF
