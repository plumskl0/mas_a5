package mas_a5;

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
	
	public Robo(ContinuousSpace<Object> space, Grid<Object> grid, RoboMind mind) {
		this.space = space;
		this.grid = grid;
		this.mind = mind;
		resetArrived();
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void run() {
		if (startPoint == null)
			startPoint = grid.getLocation(this);
		
		// Q Value berechnen für aktuelle Koordinate
		
		// Über q value nächsten schritt auswählen
		
		if (!arrived) {
//			moveTowards();
		}
		
		// Warten auf anderen Robo
		
		if (mind.allBotsArrived()) {
			resetArrived();
			mind.resetArrived();
			beamToStart();
		}
		
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
	}
	
	private void resetArrived() {
		arrived = false;
	}
}
