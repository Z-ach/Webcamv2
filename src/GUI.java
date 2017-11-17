import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;

public class GUI extends JPanel implements ActionListener {

	private GripPipeline gp;
	private VideoCapture camera;
	private BufferedImage img;
	private int refreshRate = 50;
	double tempMaxX, tempMaxY, tempMinX, tempMinY;

	public GUI() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		gp = new GripPipeline();
		camera = new VideoCapture(0);
		System.out.println("running in gui");
		Timer timer = new Timer(refreshRate, this);
		timer.start();
		// display();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Mat frame = new Mat();
		camera.read(frame);
		gp.process(frame);
		if (camera.read(frame)) {
			img = MatToBufferedImage(frame);
			g.drawImage(img, 0, 0, this);
			org.opencv.core.Point[] points = findPoint();
			g.setColor(Color.red);
			if (points != null) {
				for(Point pt : points) {
					g.fillOval((int)pt.x, (int)pt.y, 3, 3);
				}
			}
		}
	}

	public org.opencv.core.Point[] findPoint() {
		org.opencv.core.Point points[] = new org.opencv.core.Point[1];
		points = gp.filterContoursOutput().get(0).toArray();
		return points;
	}

	public BufferedImage MatToBufferedImage(Mat frame) {
		// Mat() to BufferedImage
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);

		return image;
	}

	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}
