import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subroutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat(int i)
	{
		try
		{
			System.out.println("Philosopher " + getTID()+" *starts* to eat for " + i + " times.");
			yield();
			sleep((long) (Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher " + getTID()+" *finishes* eating for "+ i + " times.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{

		try {
			System.out.println("Philosopher " + getTID()+" starts to think.");
			yield();
			sleep((long) (Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher " + getTID()+" finishes thinking.");

		} catch (InterruptedException e) {
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.println("Philosopher " + getTID()+" starts to talk.");
		yield();
		saySomething();
		yield();
		System.out.println("Philosopher " + getTID()+" finishes talking.");
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run() {
        for (int i = 0; i < DiningPhilosophers.DINING_STEPS; i++) {

            try {
                DiningPhilosophers.soMonitor.pickUp(getTID());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            eat(i + 1);


            try {
                DiningPhilosophers.soMonitor.putDown(getTID());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            think();

            try {
                DiningPhilosophers.soMonitor.requestTalk();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            talk();
            DiningPhilosophers.soMonitor.endTalk();
            yield();

            if(i==9)
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Philosopher " + getTID() + " has left the table. $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        }

    } // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
