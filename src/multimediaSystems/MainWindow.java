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

	private int thresholdValue = 2;
	private Mat src;

	private JLabel imgLabel = new JLabel();

	public MainWindow() {
		super(WINDOW_NAME);

		//uncomment for faster testing, comment when finished
		//setImage("test_1.jpg"); /*
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
		Mat m = RegionGrowing.regionGrowing2(src, thresholdValue);
		Image img = HighGui.toBufferedImage(m);
		imgLabel.setIcon(new ImageIcon(img));
		repaint();
	}

	private MenuBar getMenu() {
		MenuBar mb = new MenuBar();
		Menu menu = new Menu("Menu");

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
		String userDirLocation = System.getProperty("user.dir");
		File userDir = new File(userDirLocation);
		JFileChooser fileChooser = new JFileChooser(userDir);
		int result = fileChooser.showOpenDialog(MainWindow.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			setImage(selectedFile.getAbsolutePath());
		} else {
			System.exit(1);
		}
	}

	private void setImage(String imagePath) {
		src = Imgcodecs.imread(imagePath);
		Mat m = RegionGrowing.regionGrowing2(src, thresholdValue);
		// Set up the content pane.
		Image img = HighGui.toBufferedImage(m);
		imgLabel.setIcon(new ImageIcon(img));
	}
}
