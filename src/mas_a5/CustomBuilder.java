package mas_a5;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class CustomBuilder implements ContextBuilder<Object> {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int xGrid = 5;
	private int yGrid = 10;
	
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

		// Koordinaten eingeben
		for (int x = 0; x < xGrid; x++) {
			for(int y = 0; y < yGrid; y++) {
				addKoordinate(context, new Koordinate(x, y));
			}
		}

		// Gedächtnis der Robos erzeugen
		
		
		// Roboter erzeugen
		
		
		return context;
	}

	
	private void addKoordinate(Context<Object> ctx, Koordinate k) {
		ctx.add(k);
		space.moveTo(k, (int) k.getX(), (int) k.getY());
		grid.moveTo(k, (int) k.getX(), (int) k.getY());
	}
}
