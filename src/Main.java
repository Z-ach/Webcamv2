import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setTitle("Vision");
		frame.setSize(600, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(new GUI());
		frame.setVisible(true);

	}
}