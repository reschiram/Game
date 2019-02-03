package tick;

import data.Tickable;

public class TickManager {
	
	public static final int TICK_DURATION = 1000/100;
	
	private long ticks = 0;
	private long startTime = 0;
	
	private boolean run = true;
	
	private boolean released = false;
	public void release(){
		released = true;
		startTime = System.currentTimeMillis();
	}
	
	public TickManager(Tickable tickable){
		new Thread(new Runnable(){
			@Override
			public void run() {
				startTime = System.currentTimeMillis();
				while(run){
					tickable.tick();
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
					if(released)ticks++;
				}
			}
		}).start();
	}

	public double getDeltaTime() {
		return 1.0;
	}

	public long getLatency() {
		return (System.currentTimeMillis()-startTime-(TICK_DURATION*ticks))/TICK_DURATION;
	}

	public long getCurrentTick() {
		return ticks;
	}

	public void kill() {
		run = false;
	}

}
