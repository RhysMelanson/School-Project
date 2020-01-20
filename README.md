# School-Project
A school project

School Project

Modified version of the Cigarette smokers problem using chefs and sandwiches.

The program consists of two classes AgentThread and ChefThread. The AgentThread classes contains the main method and will execute the 
program when run. The end condition of the system is decided by the sandwichQuota variable which dictates the number of sandwiches need
to be made for the program to terminate, by default it is 20.

Design desisions

Instead of terminating the program with an exit statement in the agent thread once the sandwich quota is reached I terminated each
thread seperatly. I chose to do this as this make the program more flexible as it could now be run as a sub operation and does not 
terminate all processes. 
