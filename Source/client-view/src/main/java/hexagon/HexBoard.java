package hexagon;

import java.awt.Graphics;

import javax.swing.JPanel;

public class HexBoard extends JPanel {

	public HexBoard() {

	}

	@Override
	public void repaint() {
		System.out.println("Calling repaint form HexBoard ... ");

		super.repaint();
	}

	@Override
	public void paintComponents(Graphics g) {
		System.out.println("Calling repaint from HexBoard ... ");

		super.paintComponents(g);
	}

}
