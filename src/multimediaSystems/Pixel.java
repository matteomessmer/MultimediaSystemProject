package multimediaSystems;

public class Pixel {
	private int row, col;
	private double[] value;
	private double distanceFromBlack;

	//get Euclidean distance value
	public double getDistance(double[] value) {
		return euclideanDistance(this.value, value);
	}

	//Getters
	public double getDistanceFromBlack() {
		return distanceFromBlack;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	//Setters
	public void setCol(int col) {
		this.col = col;
	}

	public double[] getValue() {
		return value;
	}

	public void setValue(double[] value) {
		this.value = value;
	}

	//Constructor with the distanceFromBlack value
	public Pixel(int row, int col, double[] value, double distanceFromBlack) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
		this.distanceFromBlack = distanceFromBlack;
	}

	//Constructor
	public Pixel(int row, int col, double[] value) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
	}

	//Calculate Euclidean distance
	public static double euclideanDistance(double[] pixelA, double[] pixelB) {
		return Math.sqrt(Math.pow(pixelA[0] - pixelB[0], 2) + Math.pow(pixelA[1] - pixelB[1], 2)
				+ Math.pow(pixelA[2] - pixelB[2], 2));
	}
}
