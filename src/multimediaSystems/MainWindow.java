package multimediaSystems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.photo.Photo;

public class MainWindow extends JFrame {

	private static int MAX_VALUE = 441;

	private static final String WINDOW_NAME = "Multimedia Systems - Region Growing";

	private int thresholdValue = MAX_VALUE;
	private Mat src;
	
	ArrayList<JRadioButton> algorithmButtons;
	JCheckBox denoiseCheck;
	
	private static int denoiseHMaxValue = 100;
	private int denoiseHValue = 10;
	
	JLabel time = new JLabel();
	JLabel regions = new JLabel();
	
	
	private JLabel imgLabel = new JLabel();

	public MainWindow() {
		super(WINDOW_NAME);

		// Create and set up the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(getContentPane());
		
		// uncomment for faster testing, comment when finished
		// setImage("test_1.jpg"); /*
		do {
			chooseImage();
		} while (src.empty());
		// */
		
		
		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		pack();
		setVisible(true);
	}

	private void addComponentsToPane(Container pane) {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		// Menu
		MenuBar menu = getMenu();
		setMenuBar(menu);

		// Algorithm radio buttons
		JRadioButton alg1Button = new JRadioButton("Algorithm 1",true);
		JRadioButton alg2Button = new JRadioButton("Algorithm 2");
		JRadioButton alg3Button = new JRadioButton("Algorithm 2 Var");

		algorithmButtons = new ArrayList<JRadioButton>();
		algorithmButtons.add(alg1Button);
		algorithmButtons.add(alg2Button);
		algorithmButtons.add(alg3Button);

		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(alg1Button);
		bgroup.add(alg2Button);
		bgroup.add(alg3Button);

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(3, 1));
		radioPanel.add(alg1Button);
		radioPanel.add(alg2Button);
		radioPanel.add(alg3Button);
		radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Algorithm"));

		
		denoiseCheck = new JCheckBox("Denoise");
		
		// Create Trackbar to choose Threshold value
		JSlider sliderThreshValue = new JSlider(1, MAX_VALUE, thresholdValue);
		sliderThreshValue.setMajorTickSpacing(50);
		sliderThreshValue.setMinorTickSpacing(10);
		sliderThreshValue.setPaintTicks(true);
		sliderThreshValue.setPaintLabels(true);

		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(2,1));
		sliderPanel.add(new JLabel("Threshold"));
		sliderPanel.add(sliderThreshValue);

		sliderThreshValue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				thresholdValue = source.getValue();
				if (!sliderThreshValue.getValueIsAdjusting()) {
					update();
				}
			}
		});
		

		JPanel denoiseHSliderPanel = new JPanel();
		denoiseHSliderPanel.setLayout(new BoxLayout(denoiseHSliderPanel, BoxLayout.PAGE_AXIS));

		// Create Trackbar to choose Threshold value
		JSlider denoiseSliderValue = new JSlider(1, denoiseHMaxValue, denoiseHValue);
		denoiseSliderValue.setMajorTickSpacing(50);
		denoiseSliderValue.setMinorTickSpacing(10);
		denoiseSliderValue.setPaintTicks(true);
		denoiseSliderValue.setPaintLabels(true);
		denoiseHSliderPanel.add(denoiseSliderValue);

		denoiseSliderValue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				denoiseHValue = source.getValue();
				if (!denoiseSliderValue.getValueIsAdjusting()) {
					if(denoiseCheck.isSelected()) {
						update();
					}
				}
			}
		});

		JPanel denoisePanel = new JPanel(new GridLayout(3,1));
		denoisePanel.add(denoiseCheck);
		denoisePanel.add(denoiseHSliderPanel);

		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(2,1));
		
		resultsPanel.add(time);
		resultsPanel.add(regions);

		JPanel algTopPanel = new JPanel();
		algTopPanel.setLayout(new GridLayout(1, 3));
		algTopPanel.add(radioPanel);
		algTopPanel.add(denoisePanel);
		algTopPanel.add(resultsPanel);
		
		JPanel algorithmPanel = new JPanel();
		algorithmPanel.setLayout(new GridLayout(2,1));
		algorithmPanel.add(algTopPanel);
		algorithmPanel.add(sliderPanel);
		
		pane.add(algorithmPanel, BorderLayout.PAGE_START);
		pane.add(imgLabel, BorderLayout.CENTER);
	}

	private void update() {
		computeRegionGrowing();
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
		computeRegionGrowing();
	}

	private void computeRegionGrowing() {
		Mat source = new Mat();
		
		if(denoiseCheck.isSelected()) {
			Photo.fastNlMeansDenoisingColored(src,source,denoiseHValue,10);
		} else {
			source = src;
		}
		
		String algorithm = "";
		for (int i = 0; i < algorithmButtons.size(); i++) {
			if (algorithmButtons.get(i).isSelected()) {
				algorithm = algorithmButtons.get(i).getText();
			}
		}
		
		long start = System.currentTimeMillis();
		Mat m = null;
		switch (algorithm) {
		case "Algorithm 1":
			m = RegionGrowing.regionGrowing(source, thresholdValue);
			break;
		case "Algorithm 2":
			m = RegionGrowing.regionGrowing2(source, thresholdValue);
			break;
		case "Algorithm 2 Var":
			m = RegionGrowing.regionGrowing2Var(source, thresholdValue);
			break;
		}

		regions.setText("Number of regions: " + RegionGrowing.regions);
		time.setText("Total time: " + ((double)(System.currentTimeMillis() - start)/1000.0) + " seconds");

		// Set up the content pane.
		Image img = HighGui.toBufferedImage(m);
		imgLabel.setIcon(new ImageIcon(img));
		imgLabel.setHorizontalAlignment( SwingConstants.CENTER);
		imgLabel.setVerticalAlignment(SwingConstants.TOP);
	}
}
