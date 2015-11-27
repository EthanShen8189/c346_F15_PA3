
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
	private enum State{eating, hungry, thinking, talking, wantTotalk}
	State[] states; //= new State[NbOfChopsticks];
	Object[] self; //= new Condition[NbOfChopsticks];
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		this.NbOfChopsticks = piNumberOfPhilosophers;
		states = new State[NbOfChopsticks];
		self = new Object[NbOfChopsticks];

		for(int i=0; i< NbOfChopsticks; i++){
			states[i] = State.thinking;
			self[i]=new Object();
		}


		// TODO: set appropriate number of chopsticks based on the # of philosophers
	}

	private void eatTest(final int piTID){
		if((states[(piTID-1) % NbOfChopsticks] !=State.eating)
				&&(states[piTID] == State.hungry)
				&&(states[(piTID+1) % NbOfChopsticks] != State.eating)){

			states[piTID] = State.eating;
			synchronized (this.self[piTID]){
				this.self[piTID].notifyAll();
			}
		}
	}

	private void talkTest(final int piTID){
		boolean talkAccess=false;
		for(int i=0; i<NbOfChopsticks; i++){
			if((states[i] != State.talking) && (states[piTID] == State.wantTotalk)){
				talkAccess = true;
			}
				if(talkAccess){
					states[piTID] = State.talking;
					synchronized (this.self[piTID]){
						this.self[piTID].notifyAll();
					}
				}
		}
	}

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		states[piTID] = State.hungry;
		eatTest(piTID);
		if(states[piTID]!=State.eating)
			try {
				synchronized (this.self[piTID]){
					this.self[piTID].wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		states[piTID] = State.thinking;
		eatTest((piTID - 1) % NbOfChopsticks);
		eatTest((piTID + 1) % NbOfChopsticks);
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		states[piTID] = State.wantTotalk;
		talkTest(piTID);
		if(states[piTID] !=State.talking)
			try {
				synchronized (this.self[piTID]){
					this.self[piTID].wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		states[piTID] = State.thinking;
		synchronized (this.self[piTID]){
			this.self[piTID].notifyAll();
		}
	}
}

// EOF
