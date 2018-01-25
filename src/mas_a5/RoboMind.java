package mas_a5;

import java.util.ArrayList;
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
	
	private ArrayList<Robo> botList;

	private RoboMind() {
		this.rewList = new ConcurrentHashMap<String, Integer>();
		this.qList = new ConcurrentHashMap<String, Double>();
		this.botList = new ArrayList<Robo>();
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
		System.out.println("Arrived: " + arrived);
		if (arrived == 2) {
			
			for (int i = 0; i < botList.size(); i++) {
				botList.get(i).beamToStart();
			}
			
			resetArrived();
		}
	}

	public void resetArrived() {
		arrived = 0;
	}

	public String[] getNextStep(Koordinate k) {
		int x = k.getX();
		int y = k.getY();
		
		// Welche möglichkeiten gibt es?
		String keyRight = (x + 1) + "_" + y;
		String keyLeft = (x - 1) + "_" + y;
		String keyUp = x + "_" + (y + 1);
		String keyDown = x + "_" + (y - 1);
		
		HashMap<String, Double> qVals = new HashMap<String, Double>();
		
		if (qList.containsKey(keyRight)) {
			qVals.put(keyRight, qList.get(keyRight));
		}
		
		if (qList.containsKey(keyLeft)) {
			qVals.put(keyLeft, qList.get(keyLeft));
		}
		
		if (qList.containsKey(keyUp)) {
			qVals.put(keyUp, qList.get(keyUp));
		}
		
		if (qList.containsKey(keyDown)) {
			qVals.put(keyDown, qList.get(keyDown));
		}
		
		Map.Entry<String, Double> maxEntry = maxEntryInHash(qVals);
		
		return maxEntry.getKey().split("_");
	}
	
	// Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all
	// actions)]
	public void updateQList(Koordinate k, Koordinate newk) {
		updateQList(k.getX(), k.getY(), newk.getX(), newk.getY());
	}

	public void updateQList(int oldX, int oldY, int newx, int newy) {

		double rewNewState = getReward(newx + "_" + newy);

		double p = 1;//optionsCount(oldX, oldY);

		HashMap<String, Double> qValues = new HashMap<String, Double>();

		String keyRight = (oldX + 1) + "_" + oldY;
		String keyLeft = (oldX - 1) + "_" + oldY;
		String keyUp = oldX + "_" + (oldY + 1);
		String keyDown = oldX + "_" + (oldY - 1);

		// Reward rechts
		if (rewList.containsKey(keyRight)) {
			double qValueAction1 = (p * getQVal(keyRight));
			System.out.println("QValueRight: " + qValueAction1);
			qValues.put(keyRight, qValueAction1);
		}

		// Reward links
		if (rewList.containsKey(keyLeft)) {
			double qValueAction2 = (p * getQVal(keyLeft));
			System.out.println("QValueLeft: " + qValueAction2);

			qValues.put(keyLeft, qValueAction2);
		}

		// Reward oben
		if (rewList.containsKey(keyUp)) {
			double qValueAction3 = (p * getQVal(keyUp));
			System.out.println("QValueUP: " + qValueAction3);

			qValues.put(keyUp, qValueAction3);
		}

		// Reward unten
		if (rewList.containsKey(keyDown)) {
			double qValueAction4 = (p * getQVal(keyDown));
			System.out.println("QValueDown: " + qValueAction4);

			qValues.put(keyDown, qValueAction4);
		}

		// Max der einzelnen Actions
		
		Map.Entry<String, Double> maxEntry = maxEntryInHash(qValues);

		double max_a = getQVal(maxEntry.getKey());
		double sum2 = rewNewState + max_a;
		double sum1 = getQVal(oldX + "_" + oldY);
		
		double newQVal = sum1 + sum2;
		addQVal(oldX + "_" + oldY, newQVal);
	}

	private Map.Entry<String, Double> maxEntryInHash(HashMap<String, Double> qValues){
		Map.Entry<String, Double> maxEntry = null;
		
		for (Map.Entry<String, Double> entry : qValues.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		
		return maxEntry;
	}
	
	private double optionsCount(int currentX, int currentY) {
		double cnt = 0.0d;

		// Rechts
		if (rewList.containsKey((currentX + 1) + "_" + currentY))
			cnt++;

		// Links
		if (rewList.containsKey((currentX - 1) + "_" + currentY))
			cnt++;

		// Oben
		if (rewList.containsKey(currentX + "_" + (currentY + 1)))
			cnt++;

		// Unten
		if (rewList.containsKey(currentX + "_" + (currentY - 1)))
			cnt++;

		return cnt;
	}
	
	public void addBot(Robo b) {
		botList.add(b);
	}
}
