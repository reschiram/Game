package game.tick;

import game.GameManager;

public class TickManager {
	
	private static int TICK_DURATION = 1000/100;
//	private static double time = System.currentTimeMillis();
	private static long TICKS = 0;
	private static long StartTime = 0;
	
	private static boolean released = false;
	public static void Release(){
		released = true;
		StartTime = System.currentTimeMillis();
	}
	
	public TickManager(GameManager gM){
		new Thread(new Runnable(){
			@Override
			public void run() {
				StartTime = System.currentTimeMillis();
				while(true){
//					time = System.currentTimeMillis();
					gM.tick();
					int wait = (int) (TICK_DURATION - getLatency()*TICK_DURATION);
					if(!released)wait = 10;
//					System.out.println(wait);
					if(wait>0){
						synchronized (Thread.currentThread()) {
							try {
								Thread.currentThread().wait(wait);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}							
						}
					}
//					System.out.println(TICKS+" -> "+(System.currentTimeMillis()-StartTime-TICK_DURATION*TICKS)+" -> "+wait);
					if(released)TICKS++;
				}
			}
		}).start();
	}

	public static double getDeltaTime() {
		return 1.0;
		//return 1.0+((double)(System.currentTimeMillis()-time)/TICK_DURATION);
		//return Math.sqrt((TICK_DURATION-(System.currentTimeMillis()-time))*(TICK_DURATION-(System.currentTimeMillis()-time)))/TICK_DURATION;
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
