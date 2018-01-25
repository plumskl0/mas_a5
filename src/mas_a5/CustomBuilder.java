package mas_a5;

import mas_a5.Koordinaten.Koordinate;
import mas_a5.Koordinaten.KoordinatenFactory;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class CustomBuilder implements ContextBuilder<Object> {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int xGrid = 10;
	private int yGrid = 5;
	
	private RoboMind rm;
	
	@Override
	public Context build(Context<Object> context) {
		
		context.setId("mas_a5");

		// Space und Grid
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context, new SimpleCartesianAdder<>(),
				new repast.simphony.space.continuous.StrictBorders(), xGrid, yGrid);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, xGrid, yGrid));

		// Gedächtnis der Robos erzeugen
		rm = RoboMind.getInstance();
		
		// Reward und Q Listen füllen
		for (int x = 0; x < xGrid; x++) {
			for (int y = 0; y < yGrid; y++) {
				String c = x+"_"+y;
				if (isBlocked(c))
					continue;
				
				rm.addReward(c, 0);
				rm.addQVal(c, 0.0d);
			}
		}
		
		// Koordinaten eingeben
		for (int x = 0; x < xGrid; x++) {
			for(int y = 0; y < yGrid; y++) {
				
				Koordinate k = KoordinatenFactory.createKoordinate(x, y);
				
				addKoordinate(context, k);
			}
		}
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		System.out.println(params.getInteger("maxRuns"));
		
		// Roboter erzeugen und Gedächtnis hinzufügen
		Robo r1 = new Robo(space, grid, rm, params.getInteger("maxRuns"));
		Robo r2 = new Robo(space, grid, rm, params.getInteger("maxRuns"));
		
		addRobo(context, r1, 0, 0);
		addRobo(context, r2, 0, 4);
		
		return context;
	}

	private boolean isBlocked(String c) {
		return ("3_1".equals(c) || "3_2".equals(c) || "6_4".equals(c) || "7_3".equals(c) || "7_4".equals(c));
	}
	
	private void addRobo(Context<Object> ctx, Robo b, int x, int y) {
		ctx.add(b);
		space.moveTo(b, (int) x, (int) y);
		grid.moveTo(b, (int) x, (int) y);
		rm.addBot(b);
	}
	
	private void addKoordinate(Context<Object> ctx, Koordinate k) {
		ctx.add(k);
		space.moveTo(k, (int) k.getX(), (int) k.getY());
		grid.moveTo(k, (int) k.getX(), (int) k.getY());
	}
}
