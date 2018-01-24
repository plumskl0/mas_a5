package mas_a5.Koordinaten;

public class KoordinatenFactory {

	private static int exitReward = 10;
	private static int trapReward = -10;
	private static int slipperyReward = -3;
	private static int impassableReward = -100;
	private static int normalReward = -1;

	public static Koordinate createKoordinate(int x, int y) {
		Koordinate c;
		if ((x == 0 && y == 3) || (x == 3 && y == 3) || (x == 3 && y == 4) || (x == 5 && y == 2) || (x == 6 && y == 1)
				|| (x == 7 && y == 0)) {// Slippery
			c = new Slippery(x, y, slipperyReward);// Slippery reward -3
		} else if ((x == 1 && y == 0) || (x == 2 && y == 2) || (x == 8 && y == 3) || (x == 9 && y == 1)) {// Trap
																											// -10
			// Trap reard -10
			c = new Trap(x, y, trapReward);
		} else if ((x == 3 && y == 1) || (x == 3 && y == 2) || (x == 6 && y == 4) || (x == 7 && y == 3)
				|| (x == 7 && y == 4)) {// impassible
			c = new Impassable(x, y, impassableReward); // Impassable reward = 0
														// als Indikator
		} else if ((x == 8 && y == 4)) {// Ziel+10
			c = new Exit(x, y, exitReward);
		} else {
			c = new Koordinate(x, y, normalReward);
		}

		return c;
	}

}
