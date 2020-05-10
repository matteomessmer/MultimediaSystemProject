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
import org.opencv.imgproc.Imgproc;

public class MainWindow extends JFrame {

	private static int MAX_VALUE = 255;
	private static int MAX_TYPE = 4;
	private static int MAX_BINARY_VALUE = 255;
	private static final String WINDOW_NAME = "Multimedia Systems - Region Growing";
	private static final String TRACKBAR_TYPE = "<html><body>Type: <br> 0: Binary <br> "
			+ "1: Binary Inverted <br> 2: Truncate <br> " + "3: To Zero <br> 4: To Zero Inverted</body></html>";
	private static final String TRACKBAR_VALUE = "Value";
	private int thresholdValue = 0;
	private int thresholdType = 3;
	private Mat src;
	private Mat srcGray = new Mat();
	private Mat dst = new Mat();
	private JLabel imgLabel = new JLabel();

	public static String imagePath = "";
	
	public MainWindow() {
		super(WINDOW_NAME);

		do {
			chooseImage();
		} while( src.empty());
		
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
		sliderPanel.add(new JLabel(TRACKBAR_TYPE));
		// Create Trackbar to choose type of Threshold
		JSlider sliderThreshType = new JSlider(0, MAX_TYPE, thresholdType);
		sliderThreshType.setMajorTickSpacing(1);
		sliderThreshType.setMinorTickSpacing(1);
		sliderThreshType.setPaintTicks(true);
		sliderThreshType.setPaintLabels(true);
		sliderPanel.add(sliderThreshType);
		sliderPanel.add(new JLabel(TRACKBAR_VALUE));
		// Create Trackbar to choose Threshold value
		JSlider sliderThreshValue = new JSlider(0, MAX_VALUE, 0);
		sliderThreshValue.setMajorTickSpacing(50);
		sliderThreshValue.setMinorTickSpacing(10);
		sliderThreshValue.setPaintTicks(true);
		sliderThreshValue.setPaintLabels(true);
		sliderPanel.add(sliderThreshValue);
		sliderThreshType.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				thresholdType = source.getValue();
				update();
			}
		});
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
		Imgproc.threshold(srcGray, dst, thresholdValue, MAX_BINARY_VALUE, thresholdType);
		Image img = HighGui.toBufferedImage(dst);
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
			imagePath = selectedFile.getAbsolutePath();
			src = Imgcodecs.imread(imagePath);

			// Convert the image to Gray
			Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);

			// Set up the content pane.
			Image img = HighGui.toBufferedImage(srcGray);

			imgLabel.setIcon(new ImageIcon(img));
			repaint();
		}
	}
}
