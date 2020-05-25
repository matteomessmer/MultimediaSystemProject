package multimediaSystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class RegionGrowing {

	public static int regions = 0;
	
	/* FIRST IMPLEMENTATION */

	public static Mat regionGrowing(Mat src, int thresholdValue) {
		regions = 0;
		
		// set threshold value taken from slider
		// set array of pixels already visited from the algorithm
		boolean[][] visited = new boolean[src.height()][src.width()];
		// exit condition for the algorithm
		boolean finish = false;
		// output image
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
				// find region
				ArrayList<Pixel> stack = new ArrayList<Pixel>();

				// start from seed
				Pixel seed = new Pixel(maxRow, maxCol, src.get(maxRow, maxCol));
				regions++;
				
				stack.add(seed);
				visited[maxRow][maxCol] = true;

				while (stack.size() > 0) {
					// initlilize the list of neightbours
					ArrayList<Pixel> neighbours = new ArrayList<Pixel>();

					// take first pixel of the list
					Pixel currentPixel = stack.get(0);

					// get neighbours of the current pixel
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

	/* SECOND IMPLEMENTATION */

	public static Mat regionGrowing2(Mat src, int thresholdValue) {
		regions = 0;
		
		// set threshold value taken from slider
		// output image
		Mat segmentedImage = new Mat(src.height(), src.width(), src.type());

		// create arraylist for all the pixels of the image
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		// put pixels in the arraylist
		for (int row = 0; row < src.height(); row++) {
			for (int col = 0; col < src.width(); col++) {
				double[] value = src.get(row, col);
				pixels.add(new Pixel(row, col, value, Pixel.euclideanDistance(value, new double[] { 0, 0, 0 })));
			}
		}

		// sort the pixels by "distance from black"
		Collections.sort(pixels, new Comparator<Pixel>() {
			@Override
			public int compare(Pixel p1, Pixel p2) {
				return ((Double) p1.getDistanceFromBlack()).compareTo(p2.getDistanceFromBlack());
			}
		});

		// exit condition for the algorithm
		boolean finish = false;
		// set array of pixels already visited from the algorithm
		boolean[][] visited = new boolean[src.height()][src.width()];

		// progress of pixels
		/*
		 * //show sorted image int i = 0; for (int row = 0; row < src.height(); row++) {
		 * for (int col = 0; col < src.width(); col++) {
		 * segmentedImage.put(row,col,pixels.get(i).getValue()); i++; }
		 *
		 * }
		 */
		/*
		 * for(Pixel p : pixels) { System.out.println(p.getDistanceFromBlack()); }
		 */

		// difference between first and second algorithm
		// Collections.reverse(pixels);

		while (!finish) {

			// find seed (not visited)
			Pixel seed = null;
			while (seed == null && pixels.size() > 0) {
				if (visited[pixels.get(0).getRow()][pixels.get(0).getCol()]) {
					pixels.remove(0);
				} else {
					seed = pixels.get(0);
					regions++;
					break;
				}
			}

			if (seed == null) {
				return segmentedImage;
			} else {
				ArrayList<Pixel> stack = new ArrayList<Pixel>();

				// Pixel seed = new Pixel(maxRow, maxCol, src.get(maxRow, maxCol));
				stack.add(seed);

				visited[seed.getRow()][seed.getCol()] = true;

				while (stack.size() > 0) {
					// initlilize the list of neightbours
					ArrayList<Pixel> neighbours = new ArrayList<Pixel>();

					// take first pixel of the list
					Pixel currentPixel = stack.get(0);

					// get neighbours of the current pixel
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

		// show possible region
		/*
		 * //testing for(int i = 0; i < subList.size(); i++) {
		 * segmentedImage.put(subList.get(i).getRow(), subList.get(i).getCol(), new
		 * double[] {255,0,0}); }
		 */
		/*
		 * // sort the pixels by distance from seed Collections.sort(subList, new
		 * Comparator<Pixel>() {
		 *
		 * @Override public int compare(Pixel p1, Pixel p2) { return ((Double) Math
		 * .sqrt(Math.pow(p1.getRow() - seed.getRow(), 2) + Math.pow(p1.getCol() -
		 * seed.getCol(), 2))) .compareTo(Math.sqrt(Math.pow(p2.getRow() -
		 * seed.getRow(), 2) + Math.pow(p2.getCol() - seed.getCol(), 2))); } });
		 *
		 */

		// after finding seed, smooth distance of region
		/*
		 * //testing for(int i = 0; i < subList.size(); i++) {
		 * src.put(subList.get(i).getRow(), subList.get(i).getCol(), new double[]
		 * {255-i/100,255-i/100,255-i/100}); }
		 *
		 * for (Pixel p : subList) { System.out.println(Math.sqrt(Math.pow(p.getRow() -
		 * seed.getRow(), 2) + Math.pow(p.getCol() - seed.getCol(), 2))); }
		 */

		return segmentedImage;
	}
	
	/* SECOND IMPLEMENTATION VARIANT (DESCENDENT ORDERING) */
	
	public static Mat regionGrowing2Var(Mat src, int thresholdValue) {
		regions =0;
		
		// set threshold value taken from slider
		// output image
		Mat segmentedImage = new Mat(src.height(), src.width(), src.type());

		// create arraylist for all the pixels of the image
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();

		// put pixels in the arraylist
		for (int row = 0; row < src.height(); row++) {
			for (int col = 0; col < src.width(); col++) {
				double[] value = src.get(row, col);
				pixels.add(new Pixel(row, col, value, Pixel.euclideanDistance(value, new double[] { 0, 0, 0 })));
			}
		}

		// sort the pixels by "distance from black"
		Collections.sort(pixels, new Comparator<Pixel>() {
			@Override
			public int compare(Pixel p1, Pixel p2) {
				return ((Double) p2.getDistanceFromBlack()).compareTo(p1.getDistanceFromBlack());
			}
		});
		
		// exit condition for the algorithm
		boolean finish = false;
		// set array of pixels already visited from the algorithm
		boolean[][] visited = new boolean[src.height()][src.width()];

		// progress of pixels
		/*
		 * //show sorted image int i = 0; for (int row = 0; row < src.height(); row++) {
		 * for (int col = 0; col < src.width(); col++) {
		 * segmentedImage.put(row,col,pixels.get(i).getValue()); i++; }
		 *
		 * }
		 */
		/*
		 * for(Pixel p : pixels) { System.out.println(p.getDistanceFromBlack()); }
		 */

		// difference between first and second algorithm
		// Collections.reverse(pixels);

		while (!finish) {

			// find seed (not visited)
			Pixel seed = null;
			while (seed == null && pixels.size() > 0) {
				if (visited[pixels.get(0).getRow()][pixels.get(0).getCol()]) {
					pixels.remove(0);
				} else {
					seed = pixels.get(0);
					regions++;
					break;
				}
			}

			if (seed == null) {
				return segmentedImage;
			} else {
				ArrayList<Pixel> stack = new ArrayList<Pixel>();

				// Pixel seed = new Pixel(maxRow, maxCol, src.get(maxRow, maxCol));
				stack.add(seed);

				visited[seed.getRow()][seed.getCol()] = true;

				while (stack.size() > 0) {
					// initialize the list of neightbours
					ArrayList<Pixel> neighbours = new ArrayList<Pixel>();

					// take first pixel of the list
					Pixel currentPixel = stack.get(0);

					// get neighbours of the current pixel
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

		// show possible region
		/*
		 * //testing for(int i = 0; i < subList.size(); i++) {
		 * segmentedImage.put(subList.get(i).getRow(), subList.get(i).getCol(), new
		 * double[] {255,0,0}); }
		 */
		/*
		 * // sort the pixels by distance from seed Collections.sort(subList, new
		 * Comparator<Pixel>() {
		 *
		 * @Override public int compare(Pixel p1, Pixel p2) { return ((Double) Math
		 * .sqrt(Math.pow(p1.getRow() - seed.getRow(), 2) + Math.pow(p1.getCol() -
		 * seed.getCol(), 2))) .compareTo(Math.sqrt(Math.pow(p2.getRow() -
		 * seed.getRow(), 2) + Math.pow(p2.getCol() - seed.getCol(), 2))); } });
		 *
		 */

		// after finding seed, smooth distance of region
		/*
		 * //testing for(int i = 0; i < subList.size(); i++) {
		 * src.put(subList.get(i).getRow(), subList.get(i).getCol(), new double[]
		 * {255-i/100,255-i/100,255-i/100}); }
		 *
		 * for (Pixel p : subList) { System.out.println(Math.sqrt(Math.pow(p.getRow() -
		 * seed.getRow(), 2) + Math.pow(p.getCol() - seed.getCol(), 2))); }
		 */

		return segmentedImage;
	}

	// get pixel on the right of the seed
	private static Pixel getRightPixel(int r, int c, Mat src) {
		if (src.width() <= c + 1) {
			return null;
		}
		return new Pixel(r, c + 1, src.get(r, c + 1));
	}

	// get pixel on the left of the seed
	private static Pixel getLeftPixel(int r, int c, Mat src) {
		if (c - 1 < 0) {
			return null;
		}
		return new Pixel(r, c - 1, src.get(r, c - 1));
	}

	// get pixel on top of the seed
	private static Pixel getUpPixel(int r, int c, Mat src) {
		if (r - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c, src.get(r - 1, c));
	}

	// get pixel on the bottom of the seed
	private static Pixel getDownPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1) {
			return null;
		}
		return new Pixel(r + 1, c, src.get(r + 1, c));
	}

	// get pixel on the right top of the seed
	private static Pixel getUpRightPixel(int r, int c, Mat src) {
		if (src.width() <= c + 1 || r - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c + 1, src.get(r - 1, c + 1));
	}

	// get pixel on the right bottom of the seed
	private static Pixel getDownRightPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1 || src.width() <= c + 1) {
			return null;
		}
		return new Pixel(r + 1, c + 1, src.get(r + 1, c + 1));
	}

	// get pixel on the left top of the seed
	private static Pixel getUpLeftPixel(int r, int c, Mat src) {
		if (r - 1 < 0 || c - 1 < 0) {
			return null;
		}
		return new Pixel(r - 1, c - 1, src.get(r - 1, c - 1));
	}

	// get pixel on the left bottom of the seed
	private static Pixel getDownLeftPixel(int r, int c, Mat src) {
		if (src.height() <= r + 1 || c - 1 < 0) {
			return null;
		}
		return new Pixel(r + 1, c - 1, src.get(r + 1, c - 1));
	}
}
