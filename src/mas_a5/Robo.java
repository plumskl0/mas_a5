package mas_a5;

import mas_a5.Koordinaten.Koordinate;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Robo {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	private RoboMind mind;
	private GridPoint startPoint;

	private int goalX = 8;
	private int goalY = 4;

	private boolean arrived;

	private int maxRuns;
	private int curRun;
	
	private int stepCount;
	private int lastMaxCount;

	public Robo(ContinuousSpace<Object> space, Grid<Object> grid, RoboMind mind, int maxRuns) {
		this.space = space;
		this.grid = grid;
		this.mind = mind;
		this.maxRuns = maxRuns;
		resetArrived();
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		if (curRun <= maxRuns) {
			if (startPoint == null)
				startPoint = grid.getLocation(this);

			if (!arrived) {
				// Koordinaten Objekt der aktuellen Koordinate
				Koordinate state = getCurrentLocation();

				// Nächsten Schritt über Q's aussuchen
				String nextState = mind.getNextState(state);
				
				String keyNs = nextState.substring(0, 3);
				int a = Integer.valueOf(nextState.split("_")[2]);

				// nextState ist die Kombination aus nächster Koordinate mit der Action
				// z.B. 0_1_UP (wobei UP = 0 ist)
				if (mind.checkIfPossible(keyNs))
					moveTowards(keyNs);

				// Q Value für neue Koordinate errechnen
				Koordinate newState = getCurrentLocation();

				// Reward der Koordinate
				int r = newState.getReward();
				
				mind.updateQList(state, newState, r, a);

				// Prüfen ob am Ziel
				checkAtGoal();
			}
		}
	}

	private Koordinate getCurrentLocation() {
		// Über q value nächsten schritt auswählen
		GridPoint p = grid.getLocation(this);
		return (Koordinate) grid.getObjectAt(p.getX(), p.getY());
	}

	public void moveTowards(String ns) {
		// Prüfen ob ich mich da hin bewegen kann
		if (checkMove(ns)) {
			
			System.out.println("Moving to " + ns);
			// Die Bewegung
			String[] coords = ns.split("_");
			
			int x = Integer.valueOf(coords[0]);
			int y = Integer.valueOf(coords[1]);
			
			NdPoint otherPoint = new NdPoint(x, y);
			space.moveTo(this, otherPoint.getX(), otherPoint.getY());
			NdPoint myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}

	private boolean checkAtGoal() {
		GridPoint myPos = grid.getLocation(this);

		int curX = myPos.getX();
		int curY = myPos.getY();

		stepCount++;
		if (goalX == curX && goalY == curY) {
			mind.addQValGoal(curX, curY);
			lastMaxCount = stepCount;
			beamToStart();
			return true;
		}
		return false;
	}

	public void beamToStart() {
		int x = startPoint.getX();
		int y = startPoint.getY();
		space.moveTo(this, (int) x, (int) y);
		grid.moveTo(this, (int) x, (int) y);
		resetArrived();
		curRun++;
	}

	private void resetArrived() {
		arrived = false;
		stepCount = 0;
	}

	private boolean checkMove(String key) {
		return mind.containsStepy(key);
	}
	
	public int getSteps() {
		return stepCount;
	}
	
	public int getLastMaxCount() {
		return lastMaxCount;
	}
}
