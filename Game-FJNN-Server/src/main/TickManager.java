package main;

public class TickManager {
	
	private static int TICK_DURATION = 1000/100;
	private static long TICKS = 0;
	private static long StartTime = 0;
	
	private static boolean released = false;
	public static void Release(){
		released = true;
		StartTime = System.currentTimeMillis();
	}
	
	public TickManager(ServerMain sm){
		new Thread(new Runnable(){
			@Override
			public void run() {
				StartTime = System.currentTimeMillis();
				while(true){
					sm.tick();
					int wait = (int) (TICK_DURATION - getLatency()*TICK_DURATION);
					if(!released)wait = 10;
					if(wait>0){
						synchronized (Thread.currentThread()) {
							try {
								Thread.currentThread().wait(wait);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}							
						}
					}
					if(released)TICKS++;
				}
			}
		}).start();
	}

	public static double getTickDuration() {
		return TICK_DURATION;
	}

	public static long getLatency() {
		return (System.currentTimeMillis()-StartTime-TICK_DURATION*TICKS)/TICK_DURATION;
	}

	public static long getCurrentTick() {
		return TICKS;
	}

}
