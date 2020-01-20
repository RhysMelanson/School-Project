import java.util.Random;
/**
 * 
 * @author Rhys
 *
 */
public class AgentThread implements Runnable {

	public static String neededIngredient; //The ingredient required from a chef
	private int sandwichQuota = 20; //Number of sandwiches chefs must produce
	public volatile int NumSandwiches = 0; //Counter to track sandwiches produced
	private static AgentThread cs = new AgentThread();
	private static Thread thread = new Thread(cs, "New Thread");

	/**
	 * Starts the agent thread.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		thread.start();
	}

	/**
	 * Initializes chef threads then continuously chooses one ingredient that is required 
	 * to make a sandwich. The thread then notifies chefs that an ingredient is required
	 * and wait till notified by the active chef to repeat the process until 20 sandwiches
	 * are made.
	 */
	@Override
	public void run() {
		
		//Initialize all the chef thread
		ChefThread BreadChef = new ChefThread("Bread", cs, sandwichQuota);
		ChefThread JamChef = new ChefThread("Jam", cs, sandwichQuota);
		ChefThread PenutButterChef = new ChefThread("Penut Butter", cs, sandwichQuota);
		//Start all the chef threads
		BreadChef.start();
		JamChef.start();
		PenutButterChef.start();
		
		//Holder variable used to track the thread to synchronize with agent 
		//based on which chef has the needed ingredient
		ChefThread temp;
		
		while (true) {

			//Random value to select ingredients at random
			int rand = (new Random()).nextInt((3 - 1) + 1) + 1;

			//Using the random variable to put the two ingredients chosen and decide the
			//the needed ingredient
			if (rand == 1) {
				neededIngredient = "Jam";
				System.out.println("Agent released Bread and Penut Butter");
				temp = JamChef;

			} else if (rand == 2) {
				neededIngredient = "Bread";
				System.out.println("Agent released Jam and Penut Butter");
				temp = BreadChef;

			} else {
				neededIngredient = "Penut Butter";
				System.out.println("Agent released Bread and Jam");
				temp = PenutButterChef;
			}
			
			//notifies all threads waiting in make sandwich to wake up
			//and check loop condition
			synchronized (cs) {
				notifyAll();
			}
			
			//Wait on the executing chef to finish its sandwich
			synchronized (temp) {
				try {
					temp.wait();
				} catch (InterruptedException e) {}
			}
			
			//Increase sandwich counter
			NumSandwiches++;
			
			//If the chefs have met the sandwich quota notify waiting threads then terminate
			if(NumSandwiches == sandwichQuota) {
				System.out.println("The chefs have made " + sandwichQuota + " sandwiches.");
				synchronized (cs) {
					notifyAll();
				}
				break;
			}
		}
		System.out.println("Agent thread has terminated");
	}
}
