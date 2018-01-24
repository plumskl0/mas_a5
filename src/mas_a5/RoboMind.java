package mas_a5;

import java.util.concurrent.ConcurrentHashMap;

public class RoboMind {

	private static RoboMind instance;

	// Rewardliste
	private ConcurrentHashMap<String, Integer> rewList;
	
	// Qliste
	private ConcurrentHashMap<String, Double> qList;	
	
	private RoboMind() {
		this.rewList = new ConcurrentHashMap<String, Integer>();
	}

	public static RoboMind getInstance() {
		if (instance == null) {
			instance = new RoboMind();
		} 
		return instance;
	}

	public void addReward(Koordinate k) {
		rewList.put(k.toString(), k.getReward());
	}
	
	public int getReward(String key) {
		return rewList.get(key);
	}
	
	public void addQVal(String key, double d) {
		qList.put(key, d);
	}
	
	public double getQVal(String key) {
		return qList.get(key);
	}
	
}
