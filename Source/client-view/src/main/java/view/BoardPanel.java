package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements DebugMessageGenerator {

	private DebugMessageHandler message_handler;


	public BoardPanel() {
		super();

		this.setBackground(Color.gray);

	}

	public BoardPanel(DebugMessageHandler message_handler) {
		this();

		this.message_handler = message_handler;

		this.handleMessage("board panel constructor", MessagePriority.DebugMessage);

	}

	// @Override
	// public void paint(Graphics g) {
	// this.handleMessage("calling paint -BoardPanel",
	// MessagePriority.DebugMessage);
	//
	// super.paint(g);
	//
	// Graphics2D dg = (Graphics2D) g;
	//
	// dg.setColor(Color.RED);
	//
	// dg.drawLine(10, 10, 150, 200);
	//
	// this.handleMessage("draw line -paint-boardPanel",
	// MessagePriority.DebugMessage);
	//
	// }

	@Override
	public void paintComponents(Graphics g) {

		this.handleMessage("calling paintComponent -BoardPanel", MessagePriority.DebugMessage);

		super.paintComponents(g);
	}

	public void setDebugMessageHandler(DebugMessageHandler message_handler) {
		this.message_handler = message_handler;
	}

	private void handleMessage(String text, MessagePriority priority) {
		if (this.message_handler != null) {
			this.message_handler.handle(text, priority);
		}
	}

}
