package mas_a5;

import mas_a5.Koordinaten.Koordinate;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
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
		do {
			if (startPoint == null)
				startPoint = grid.getLocation(this);

			if (!arrived) {
				// Über q value nächsten schritt auswählen
				GridPoint p = grid.getLocation(this);

				// Koordinaten Objekt der aktuellen Koordinate
				Koordinate k = (Koordinate) grid.getObjectAt(p.getX(), p.getY());

				// Reward der Koordinate merken
				mind.addReward(k);

				// Q Values berechnen und updaten
				String nextStepKey = mind.updateQList(k);

				String[] coords = nextStepKey.split("_");

				int x = Integer.valueOf(coords[0]);
				int y = Integer.valueOf(coords[1]);

				System.out.println("Koordinaten: " + x + ", " + y);

				moveTowards(new GridPoint(x, y));
			}

			// Warten auf anderen Robo

			if (mind.allBotsArrived()) {
				resetArrived();
				mind.resetArrived();
				beamToStart();
			}
		} while (curRun <= maxRuns);
	}

	public void moveTowards(GridPoint pt) {
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}

		// Prüfen ob am Ziel
		checkAtGoal();
	}

	private void checkAtGoal() {
		GridPoint myPos = grid.getLocation(this);

		int curX = myPos.getX();
		int curY = myPos.getY();

		if (goalX == curX && goalY == curY) {
			mind.arrived();
			arrived = true;
		}
	}

	private void beamToStart() {
		int x = startPoint.getX();
		int y = startPoint.getY();
		space.moveTo(this, (int) x, (int) y);
		grid.moveTo(this, (int) x, (int) y);
		curRun++;
	}

	private void resetArrived() {
		arrived = false;
	}
}
