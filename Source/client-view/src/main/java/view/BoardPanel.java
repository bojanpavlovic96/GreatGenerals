package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements DebugMessageGenerator {

	private DebugMessageHandler message_handler;

	// to remove

	private int x = 300;
	private int y = 300;
	private int size = 50;

	private void cubeOperations() {

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				moveSquare(e.getX(), e.getY());
			}

		});

		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				moveSquare(e.getX(), e.getY());
			}
		});

	}

	private void moveSquare(int x, int y) {
		if (this.x != x || this.y != y) {
			repaint(this.x, this.y, this.x + size, this.y + size);

			this.x = x;
			this.y = y;
			
			// repaint(this.x, this.y, this.size, this.size);
		}
	}

	@Override
	public void repaint() {
		this.handleMessage("calling repaint -BorderPnel", MessagePriority.DebugMessage);

		super.repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		this.handleMessage("paint with color: " + g.getColor(), MessagePriority.DebugMessage);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// this.handleMessage("paintComponent with color: " + g.getColor(),
		// MessagePriority.DebugMessage);

		g.setColor(Color.red);
		g.fillRect(this.x, this.y, this.size, this.size);

	}

	// to remove

	public BoardPanel() {
		super();

		// to remove

		this.cubeOperations();

		// to remove

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
