package multimediaSystems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class MainWindow extends JFrame {

	private static int MAX_VALUE = 5;

	private static final String WINDOW_NAME = "Multimedia Systems - Region Growing";

	private int thresholdValue = 4;
	private Mat src;
	
	private JLabel imgLabel = new JLabel();

	//public static String imagePath = "";
	
	public MainWindow() {
		super(WINDOW_NAME);
		
		//uncomment for faster testing, comment when finished
		//		"C:\\Users\\matteo\\Desktop\\test_2.jpg"
		//"C:\\Users\\matteo\\Desktop\\pixel.png"
		//"C:\\Users\\matteo\\Desktop\\test_3.png"
		setImage("C:\\Users\\matteo\\Pictures\\Saved Pictures\\32_1557575936.jpeg"); /*
		do {
			chooseImage();
		} while( src.empty());
		//*/	
		
		
		// Create and set up the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentsToPane(getContentPane());

		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		pack();
		setVisible(true);

	}

	private void addComponentsToPane(Container pane) {

		MenuBar menu = getMenu();
		setMenuBar(menu);

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));

		// Create Trackbar to choose Threshold value
		JSlider sliderThreshValue = new JSlider(1, MAX_VALUE, thresholdValue);
		sliderThreshValue.setMajorTickSpacing(1);
		sliderThreshValue.setMinorTickSpacing(1);
		sliderThreshValue.setPaintTicks(true);
		sliderThreshValue.setPaintLabels(true);
		sliderPanel.add(sliderThreshValue);

		sliderThreshValue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				thresholdValue = source.getValue();
				update();
			}
		});
		pane.add(sliderPanel, BorderLayout.PAGE_START);
		pane.add(imgLabel, BorderLayout.CENTER);
	}

	private void update() {
		//Imgproc.threshold(srcGray, dst, thresholdValue, MAX_BINARY_VALUE, thresholdType);

		Mat m = RegionGrowing.regionGrowing(src, thresholdValue);
		Image img = HighGui.toBufferedImage(m);
		imgLabel.setIcon(new ImageIcon(img));
		repaint();
	}

	private MenuBar getMenu() {
		MenuBar mb = new MenuBar();
		Menu menu = new Menu("Menu");

		/*
		 * Menu submenu=new Menu("Sub Menu"); MenuItem i1=new MenuItem("Item 1");
		 * MenuItem i2=new MenuItem("Item 2"); MenuItem i3=new MenuItem("Item 3");
		 * MenuItem i4=new MenuItem("Item 4"); MenuItem i5=new MenuItem("Item 5");
		 * menu.add(i1); menu.add(i2); menu.add(i3); submenu.add(i4); submenu.add(i5);
		 * menu.add(submenu);
		 */

		MenuItem openImage = new MenuItem("Open");
		openImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseImage();
			}
		});

		menu.add(openImage);
		mb.add(menu);
		return mb;
	}

	private void chooseImage() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(MainWindow.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			setImage(selectedFile.getAbsolutePath());
		}
	}
	
	private void setImage(String imagePath) {
		src = Imgcodecs.imread(imagePath);
		Mat m = RegionGrowing.regionGrowing(src, thresholdValue);
		//src.put(78, 230, new double[] {0,0,255});
		// Set up the content pane.
		Image img = HighGui.toBufferedImage(m);

		imgLabel.setIcon(new ImageIcon(img));

	}

}
