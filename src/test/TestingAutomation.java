package test;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.photo.Photo;

import multimediaSystems.RegionGrowing;

public class TestingAutomation {
	public static void test() {
		//images for testing
		String[] images = new String[] {"test_2.jpg","test_3.png","test_4.jpg","test_1.jpg"};
		
		
		for(String image : images) {
			Mat src = Imgcodecs.imread(image);
			Mat source = new Mat();
			Photo.fastNlMeansDenoisingColored(src,source,10,10);
			for(int i = 440; i > 0; i-=5) {
				long time = System.currentTimeMillis();
				Mat result = RegionGrowing.regionGrowing(source, i);
				time =System.currentTimeMillis()- time; 
				int regions = RegionGrowing.regions;
				String imageName = "threshold_" + i + "_time_" + time +"_regions_" + regions + ".jpg";
				Imgcodecs.imwrite("C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing1/" + image.replace(".", "_") + "/" + imageName, result);
				System.out.println("Written " + imageName);
			}
		}
	}
}
