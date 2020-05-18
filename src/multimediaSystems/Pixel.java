package multimediaSystems;

public class Pixel {
	private int row, col;
	private double[] value;
	
	public double getDistance(double[] value) {
		return euclideanDistance(this.value, value);
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
	public void setCol(int col) {
		this.col = col;
	}
	public double[] getValue() {
		return value;
	}
	public void setValue(double[] value) {
		this.value = value;
	}

	
	public Pixel(int row, int col, double[] value) {
		super();
		this.row = row;
		this.col = col;
		this.value = value;
	}
	

	public static double euclideanDistance(double[] pixelA, double[] pixelB) {
		return Math.sqrt(Math.pow(pixelA[0]- pixelB[0], 2) + Math.pow(pixelA[1]-pixelB[1], 2) + Math.pow(pixelA[2]-pixelB[2], 2));
	}
}
