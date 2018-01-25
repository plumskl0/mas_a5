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
				Koordinate k = getCurrentLocation();

				// Nächsten Schritt über Q's aussuchen
				String[] coords = mind.getNextStep(k);
				
				int x = Integer.valueOf(coords[0]);
				int y = Integer.valueOf(coords[1]);
				
				moveTowards(new GridPoint(x, y));
				
				// Q Value für neue Koordinate errechnen
				Koordinate newk = getCurrentLocation();

				// Reward der Koordinate merken
				mind.addReward(newk);
				
				mind.updateQList(k, newk);

				System.out.println("Koordinaten: " + x + ", " + y);
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
	
	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());

			space.moveTo(this, otherPoint.getX(), otherPoint.getY());
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}

	private void checkAtGoal() {
		GridPoint myPos = grid.getLocation(this);

		int curX = myPos.getX();
		int curY = myPos.getY();

		if (goalX == curX && goalY == curY) {
			beamToStart();
//			mind.arrived();
//			arrived = true;
		}
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
	}
}
