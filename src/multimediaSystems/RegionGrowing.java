package multimediaSystems;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class RegionGrowing {

	public static Mat regionGrowing(Mat src, int thresholdValue) {

		thresholdValue = thresholdValue * 88;
		boolean[][] visited = new boolean[src.height()][src.width()];
		boolean finish = false;
		Mat segmentedImage = new Mat(src.height(), src.width(), src.type());

		// find the max value (seed)
		int maxRow = 0, maxCol = 0;
		while (!finish) {
			finish = true;
			double maxDistance = -1;
			// find seed
			for (int row = 0; row < src.height(); row++) {
				for (int col = 0; col < src.width(); col++) {
					if (!visited[row][col]) {
						double tempValue = Pixel.euclideanDistance(src.get(row, col), new double[] { 0, 0, 0 });
						if (tempValue > maxDistance) {
							maxRow = row;
							maxCol = col;
							maxDistance = tempValue;
						}
						finish = false;
					}
				}
			}

			if (!finish) {

				// testing
				// segmentedImage.put(maxRow, maxCol, new double[] {0,0,255});

				// find region
				ArrayList<Pixel> stack = new ArrayList<Pixel>();

				Pixel seed = new Pixel(maxRow, maxCol, src.get(maxRow, maxCol));
				stack.add(seed);
				visited[maxRow][maxCol] = true;

				while (stack.size() > 0) {

					ArrayList<Pixel> neighbours = new ArrayList<Pixel>();

					Pixel currentPixel = stack.get(0);

					neighbours.add(getRightPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getLeftPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getUpPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getDownPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getUpRightPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getDownRightPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getUpLeftPixel(currentPixel.getRow(), currentPixel.getCol(), src));
					neighbours.add(getDownLeftPixel(currentPixel.getRow(), currentPixel.getCol(), src));

					// add pixel in region to stack
					for (int i = 0; i < neighbours.size(); i++) {
						if (neighbours.get(i) != null) {
							if (!visited[neighbours.get(i).getRow()][neighbours.get(i).getCol()]) {
								if (neighbours.get(i).getDistance(seed.getValue()) <= thresholdValue) {
									stack.add(neighbours.get(i));
									visited[neighbours.get(i).getRow()][neighbours.get(i).getCol()] = true;
									segmentedImage.put(neighbours.get(i).getRow(), neighbours.get(i).getCol(),
											seed.getValue());
								}
							}
						}
					}
					stack.remove(0);

				}
			}

		}

		return segmentedImage;
	}

	private static Pixel getRightPixel(int r, int c, Mat src) {
		if (src.width() <= c + 1) {
			return null;
		}
		return new Pixel(r, c + 1, src.get(r, c + 1));
	}

	private static Pixel getLeftPixel(int r, int c, Mat src) {
		if (c - 1 < 0) {
			return null;
		}
		return new Pixel(r, c - 1, src.get(r, c - 1));
	}

	private static Pixel getUpPixel(int r, int c, Mat src) {
		if (r - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c, src.get(r - 1, c));
	}

	private static Pixel getDownPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1) {
			return null;
		}
		return new Pixel(r + 1, c, src.get(r + 1, c));
	}

	private static Pixel getUpRightPixel(int r, int c, Mat src) {
		if (src.width() <= c + 1 || r - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c + 1, src.get(r - 1, c + 1));
	}

	private static Pixel getDownRightPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1 || src.width() <= c + 1) {
			return null;
		}
		return new Pixel(r + 1, c + 1, src.get(r + 1, c + 1));
	}

	private static Pixel getUpLeftPixel(int r, int c, Mat src) {
		if (r - 1 < 0 || c - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c - 1, src.get(r - 1, c - 1));
	}

	private static Pixel getDownLeftPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1 || c - 1 < 0) {
			return null;
		}
		return new Pixel(r + 1, c - 1, src.get(r + 1, c - 1));
	}

}
