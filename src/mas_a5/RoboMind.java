package mas_a5;

import java.util.HashMap;
import java.util.Map;
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

	// Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all
	// actions)]
	public String updateQList(Koordinate k) {
		return updateQList(k.getX(), k.getY());
	}
	
	public String updateQList(int currentX, int currentY) {

		double rewardCurrState = getReward(currentX + "_" + currentY);

		double p = 1 / optionsCount(currentX, currentY);

		HashMap<String, Double> qValues = new HashMap<String, Double>();

		String keyRight = (currentX + 1) + "_" + currentY;
		String keyLeft = (currentX - 1) + "_" + currentY;
		String keyUp = currentX + "_" + (currentY + 1);
		String keyDown = currentX + "_" + (currentY - 1);
		
		// Reward rechts
		if (rewList.containsKey(keyRight)) {
			double qValueAction1 = rewardCurrState + (p * getQVal(keyRight));
			qValues.put(keyRight, qValueAction1);
		}

		// Reward links
		if (rewList.containsKey(keyLeft)) {
			double qValueAction2 = rewardCurrState + (p * getQVal(keyLeft));
			qValues.put(keyLeft, qValueAction2);
		}

		// Reward oben
		if (rewList.containsKey(keyUp)) {
			double qValueAction3 = rewardCurrState + (p * getQVal(keyUp));
			qValues.put(keyUp, qValueAction3);
		}

		// Reward unten
		if (rewList.containsKey(keyDown)) {
			double qValueAction4 = rewardCurrState + (p * getQVal(keyDown));
			qValues.put(keyDown, qValueAction4);
		}

		// Max der einzelnen Actions
		Map.Entry<String, Double> maxEntry = null;

		for (Map.Entry<String, Double> entry : qValues.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		
		double oldQVal = getQVal(maxEntry.getKey());
		double newQVal = maxEntry.getValue() + oldQVal;
		
		addQVal(maxEntry.getKey(), newQVal);
		
		// QValue = Reward bei Acttion
		return maxEntry.getKey();
	}

	private double optionsCount(int currentX, int currentY) {
		double cnt = 0.0d;
		// Rechts
		if (rewList.containsKey((currentX + 1) + "_" + currentY))
			;
		cnt++;
		// Links
		if (rewList.containsKey((currentX + 1) + "_" + currentY))
			;
		cnt++;
		// Oben
		if (rewList.containsKey((currentX + 1) + "_" + currentY))
			;
		cnt++;
		// Unten
		if (rewList.containsKey((currentX + 1) + "_" + currentY))
			;
		cnt++;

		return cnt;
	}
}
