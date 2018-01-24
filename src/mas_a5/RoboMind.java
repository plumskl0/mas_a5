package mas_a5;

import java.util.concurrent.ConcurrentHashMap;

import mas_a5.Koordinaten.Koordinate;

public class RoboMind {

	private static RoboMind instance;

	// Rewardliste
	private ConcurrentHashMap<String, Integer> rewList;
	
	// Qliste
	private ConcurrentHashMap<String, Double> qList;	
	
	// Arrived check
	private int arrived;
	
	private RoboMind() {
		this.rewList = new ConcurrentHashMap<String, Integer>();
		this.qList = new ConcurrentHashMap<String, Double>();
		resetArrived();
	}

	public static RoboMind getInstance() {
		if (instance == null) {
			instance = new RoboMind();
		} 
		return instance;
	}

	public void addReward(String key, int r) {
		rewList.put(key, r);
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
	
	public void arrived() {
		arrived++;
	}
	
	public boolean allBotsArrived() {
		return arrived == 2;
	}
	
	public void resetArrived() {
		arrived = 0;
	}
	
	private void updateQList(int actionX, int actionY, int currentX, int currentY){
		int options = 0;
		if (rewList.containsKey((currentX - 1) + "_" + currentY)) options++;
		if (rewList.containsKey((currentX + 1) + "_" + currentY)) options++;
		if (rewList.containsKey(currentX + "_" + (currentY-1))) options++;
		if (rewList.containsKey(currentX + "_" + (currentY+1))) options++;		
				
		double qValue = 0.0;
		double rewardAction = getReward(actionX + "_" + actionY);
		double p = 1/options;
		double maxQValueAction = 0.0;
		double qValueAction1 = 0.0;
		double qValueAction2 = 0.0;
		double qValueAction3 = 0.0;
		double qValueAction4 = 0.0;
		double x;
		
		qValueAction1 = rewardAction + (p * getQVal((actionX + 1) + "_" + actionY));
		qValueAction2 = rewardAction + (p * getQVal((actionX - 1) + "_" + actionY));
		qValueAction3 = rewardAction + (p * getQVal(actionX + "_" + (actionY+1)));
		qValueAction4 = rewardAction + (p * getQVal(actionX + "_" + (actionY-1)));
		
		
		qValue = Math.max(qValueAction1, qValueAction2);
		qValue = Math.max(qValue, qValueAction3);
		qValue = Math.max(qValue, qValueAction4);
		
		//QValue = Reward bei Acttion
				
	}
}
