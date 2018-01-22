package mas_a5;

public class Koordinate {

	private int x;
	private int y;

	private int reward;

	public Koordinate(int x, int y) {
		// TODO Auto-generated constructor stub
		if ((x == 0 && y == 3) || (x == 3 && y == 3) || (x == 3 && y == 4) || (x == 5 && y == 2) || (x == 6 && y == 1)
				|| (x == 7 && y == 0)) {// Slippery
			this.reward = -3;// Slippery reward -3
		} else if ((x == 1 && y == 0) || (x == 2 && y == 2) || (x == 8 && y == 3) || (x == 9 && y == 1)) {// Trap
																											// -10
			// Trap reard -10
		} else if ((x == 3 && y == 1) || (x == 3 && y == 2) || (x == 6 && y == 4) || (x == 7 && y == 3)
				|| (x == 7 && y == 4)) {// impassible
			this.reward = 0; // Impassable reward = 0 als Indikator
		} else if ((x == 8 && y == 4)) {// Ziel+10
			this.reward = 10;
		} else {
			this.reward = -1; // Normal reward
		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return this.x + "_" + this.y;
	}
}
