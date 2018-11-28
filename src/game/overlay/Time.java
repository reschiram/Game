package game.overlay;

public class Time {
	
	private long milliSecond;

	public Time(int hours, int minutes, int milliSeconds){
		addMilliSeconds(hours*60*60*1000+minutes*60+milliSeconds);
	}

	public int getHour() {
		return (int) (milliSecond/(60*60*1000));
	}

	public int getMinute() {
		return (int) (milliSecond/(60*1000)) - getHour()*60;
	}

	public long getMilliSecond() {
		return milliSecond;
	}
	
	public void addMilliSeconds(long amount){
		milliSecond+=amount;
		while(getHour()>=24){
			milliSecond-=24*60*60*1000;
		}
	}

	public String getTime() {
		long second = milliSecond/1000-(getMinute()*(60))-(getHour()*(60*60));
		return getHour()+":"+getMinute()+":"+second;
	}
}
