package data;

public class Funktions {
	
	public static void wait(int amount){
		try {
			synchronized (Thread.currentThread()) {
				Thread.currentThread().wait(amount);							
			}
		} catch (InterruptedException e) {e.printStackTrace();}
	}

}
