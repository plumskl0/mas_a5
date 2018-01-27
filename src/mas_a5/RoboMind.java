package mas_a5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mas_a5.Koordinaten.Koordinate;

public class RoboMind {

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	private double d;
	private double l;

	private static RoboMind instance;

	// Rewardliste
	private ConcurrentHashMap<String, Integer> rewList;

	// Qliste
	private ConcurrentHashMap<String, Double[]> qList;

	// Arrived check
	private int arrived;

	private ArrayList<Robo> botList;

	private RoboMind() {
		this.rewList = new ConcurrentHashMap<String, Integer>();
		this.qList = new ConcurrentHashMap<String, Double[]>();
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

	public void initQList(String key) {
		Double[] vals = { 0.0d, 0.0d, 0.0d, 0.0d };
		qList.put(key, vals);
	}

	public void addQVal(String key, int a, double val) {
		Double[] vals = qList.get(key);
		vals[a] = val;
		qList.put(key, vals);
	}

	public double getQVal(String key, int a) {
		return qList.get(key)[a];
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

	private String maxEntryInHash(String state) {

		Double[] qVals = qList.get(state);

		// Koordinaten erzeugen
		String[] coords = state.split("_");
		int x = Integer.valueOf(coords[0]);
		int y = Integer.valueOf(coords[1]);

		Double maxQ = qVals[UP];
		String nextStep = x + "_" + (y + 1) + "_" + UP;

		if (qVals[RIGHT] > maxQ)
			nextStep = (x + 1) + "_" + y + "_" + RIGHT;

		if (qVals[DOWN] > maxQ)
			nextStep = x + "_" + (y - 1) + "_" + DOWN;

		if (qVals[LEFT] > maxQ)
			nextStep = (x - 1) + "_" + y + "_" + LEFT;

		return nextStep;
	}

	public void addBot(Robo b) {
		botList.add(b);
	}

	public String getNextState(Koordinate state) {
		// Maximum Q der möglichen Actions
		return maxEntryInHash(state.toString());
	}

	public boolean containsStepy(String key) {
		return qList.containsKey(key);
	}

	public void updateQList(Koordinate state, Koordinate newState, double r, int a) {
		// Aktueller State
		double qs = getQVal(state.toString(), a);

		// Bester nächster Q
		String qsa = maxEntryInHash(newState.toString());

		// Nächste Koordinate
		String ns = qsa.substring(0, 3);
		if (!checkIfPossible(ns))
			ns = newState.toString();

		// Beste Action in der nächsten Koordinate
		int na = Integer.valueOf(qsa.split("_")[2]);

		// QValue des nächsten besten Schritts
		double qns = getQVal(ns, na);

		// Q = Q + l * (r + d * Qs' - Q)
		double newQ = qs + l * (r + d * qns - qs);

		addQVal(state.toString(), a, newQ);
	}

	public void addQValGoal(int x, int y) {
		// QVal vom Goal quick'n'dirty auf 10 setzen
		String key = x + "_" + y;
		Double[] vals = qList.get(key);
		for (int i=0; i < vals.length; i++) {
			vals[i] = 10d;
		}
		
		qList.put(key, vals);
	}

	public boolean checkIfPossible(String keyNs) {
		return qList.containsKey(keyNs);
	}

	
	public void setLearningRate(double l) {
		this.l = l;
	}
	
	public void setDiscountRate(double d) {
		this.d = d;
	}
}
