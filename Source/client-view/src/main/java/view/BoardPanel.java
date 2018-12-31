package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements DebugMessageGenerator {

	private DebugMessageHandler message_handler;

	public BoardPanel() {

		this.setBackground(Color.gray);

	}

	public BoardPanel(DebugMessageHandler message_handler) {
		super();

		this.message_handler = message_handler;

		this.message_handler.handle("board panel constructor ", MessagePriority.DebugMessage);
	}

	@Override
	public void paint(Graphics g) {
		if (this.message_handler != null)
			this.message_handler.handle("calling paint in BoardPanel ", MessagePriority.DebugMessage);

		super.paint(g);
	}

	@Override
	public void repaint() {
		if (this.message_handler != null)
			this.message_handler.handle("calling repaint in BoardPanel", MessagePriority.DebugMessage);

		super.repaint();
	}

	@Override
	public void paintComponents(Graphics g) {
		if (this.message_handler != null)
			this.message_handler.handle("calling paintComponents BoardPanel", MessagePriority.DebugMessage);

		super.paintComponents(g);
	}

	public void setDebugMessageHandler(DebugMessageHandler message_handler) {
		this.message_handler = message_handler;
	}

}
