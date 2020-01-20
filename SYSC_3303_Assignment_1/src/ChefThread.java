/**
 * 
 * @author Rhys
 *
 */
public class ChefThread extends Thread {
	
	private String ingredient; //This chef's ingredient
	private int sandwichQuota; //Number of sandwiches chefs must produce
	private AgentThread ag;
	
	/**
	 * Constructor for a new chef thread contains agent which initialized
	 * ingredient possessed by the chef, and total amount of sandwiches to be produced.
	 * 
	 * @param type
	 * @param agent
	 */
	public ChefThread(String type, AgentThread agent, int quota) {
		sandwichQuota = quota;
		ag = agent;
		ingredient = type;
	}

	/**
	 * Tries to make sandwiches, 
	 * eating then notifying the agent after one is made,
	 * this process is repeated until sandwich quota is met.
	 */
	@Override
	public void run() {

		//each chef will continuously make sandwiches until 20 are made
		while (ag.NumSandwiches < sandwichQuota) {                            
			makeSandwich(ingredient);
			
			//If the sandwich quota is met stop making sandwiches
			//and break without trying to eat
			if(ag.NumSandwiches == sandwichQuota) {
				break;
			}
			
			//When the sandwich is made notify the Agent to release new ingredients
			synchronized (this) {
				notifyAll();
			}
			
			//Sleep to represent the time it take for the chef to eat the sandwich
			System.out.println(ingredient + " Chef is eating the sandwich");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}
		System.out.println(ingredient + " Chef thread has terminated");
	}

	/**
	 * Waits till the possessed ingredient is the one required 
	 * and when condition is met make a sandwich. Contains condition to free threads
	 * once sandwich quota is met.
	 * 
	 * @param ingredient
	 */
	public void makeSandwich(String ingredient) {

		//Loops until the chef's ingredient is the one needed
		//or the required amount of sandwiches are made
		while (ingredient != AgentTread.neededIngredient && ag.NumSandwiches != sandwichQuota) {
			
			//Synchronized with agent thread
			synchronized (ag) {
				
				//Thread waits till notified by agent
				try {
					ag.wait();
				} catch (InterruptedException e) {}
			}
		}

		//Break if the required amount of sandwiches are produced
		if(ag.NumSandwiches == sandwichQuota) {
			return;
		}
		
		//Sleep to represent the time it take for the chef to make the sandwich
		System.out.println(ingredient + " Chef is making a sandwich.");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
	}
}
