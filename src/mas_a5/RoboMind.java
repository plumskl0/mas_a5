package mas_a5;

import java.util.concurrent.ConcurrentHashMap;

public class RoboMind {

	private static RoboMind instance;
	
	private ConcurrentHashMap<String, Integer> rewList;
	
	private RoboMind() {
		this.rewList = new ConcurrentHashMap<String, Integer>();
	}

	public static RoboMind getInstance() {
		if (instance == null) {
			instance = new RoboMind();
		} 
		return instance;
	}

	public void addInfo(Koordinate k) {
		rewList.put(k.toString(), k.getReward());
	}
	
	public int getInfo(String key) {
		return rewList.get(key);
	}
	
}
