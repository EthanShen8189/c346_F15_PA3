import java.util.Scanner;

/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static int numberOfPhilosopher;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			Scanner input = new Scanner(System.in);
            
            System.out.println("Please let me know how many philosophers will come to dinner.");
            numberOfPhilosopher = input.nextInt();
            
            while(numberOfPhilosopher<=0){
                System.out.println("Please let me know how many philosophers will come to dinner.");
                numberOfPhilosopher = input.nextInt();
            }

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(numberOfPhilosopher);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[numberOfPhilosopher];

			System.out.println(numberOfPhilosopher + " philosopher(s) came in for a dinner.");

			// Let 'em sit down
			for(int j = 0; j < numberOfPhilosopher; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}



			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < numberOfPhilosopher; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
