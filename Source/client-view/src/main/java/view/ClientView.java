package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientView extends JFrame implements EventDrivenComponent {

	private Map<String, List<ViewEventHandler>> handlers_map;

	private DebugFrame debug_frame;

	private BoardPanel board_canvas;

	private DebugMessageHandler default_debug_message_handler;

	public ClientView() {

		this.initUI();
		
		this.initBoardCanvas();
		this.setEventListeners();

		this.addComponents();

		this.handlers_map = new HashMap<String, List<ViewEventHandler>>();

	}

	public ClientView(DebugFrame new_debug_frame) {

		this();

		this.debug_frame = new_debug_frame;

		this.default_debug_message_handler = new DebugMessageHandler() {
			public void handle(String text, MessagePriority priority) {
				debug_frame.addMessage(text, priority);
			}
		};

	}

	private void initUI() {

		this.setSize(1000, 600);
		this.setLocationRelativeTo(null); // location relative to some other component

		// removes title bar
		this.setUndecorated(true);
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // exit on close closes all frames
																// (don't know why)

	}

	private void initBoardCanvas() {

		this.board_canvas = new BoardPanel(this.default_debug_message_handler);
		// this.board_canvas.setDebugMessageHandler(this.default_debug_message_handler);
		// show debug messages in debug frame

		this.add(this.board_canvas);

	}

	private void setEventListeners() {

		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'd') {
					System.out.println("'d' key pressed -GameFrame ... ");
					if (debug_frame.isVisible()) {
						System.out.println("Hide debug frame -GameFrame ...");
						hideDebugFrame();
					} else {
						showDebugFrame();
					}
				}
			}

			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.out.println("Esc key release -GameFrame ...");
					closeFrame();

				}

			}

		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {

				if (debug_frame != null) {
					System.out.println("Debug frame close -GameFrame ... ");
					debug_frame.dispose();
				}

				System.out.println("Game frame close -GameFrame ... ");
				dispose(); // this.dispose()
			}

		});

	}

	private void closeFrame() {

		// same as click on x
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}

	private void addComponents() {

	}

	public void setDebugFrame(DebugFrame debug_frame) {
		this.debug_frame = debug_frame;
	}

	public JFrame getDebugFrame() {
		return this.debug_frame;
	}

	public void showDebugFrame() {
		if (this.debug_frame != null) {
			if (!this.debug_frame.isVisible()) {
				this.debug_frame.setVisible(true);

				this.requestFocus();

			} else {
				System.out.println("Debug frame already visible -GameFrame ... ");
				this.debug_frame.toFront();
			}
		} else {
			System.err.println("Debug frame not set -GameFrame ... ");
		}
	}

	public void hideDebugFrame() {

		if (this.debug_frame != null) {

			if (this.debug_frame.isVisible()) {
				this.debug_frame.setVisible(false);
			}

		} else {
			System.out.println("Debug frame not set -GameFrame ...");
		}

	}

	public void setEventHandler(String event_name, ViewEventHandler event_handler) {

	}

	public void addEventHandler(String event_name, ViewEventHandler event_handler) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		if (handlers != null) {

			handlers.add(event_handler);

		} else {

			handlers = new ArrayList<ViewEventHandler>();
			handlers.add(event_handler);

			this.handlers_map.put(event_name, handlers);
		}

	}

	public List<ViewEventHandler> getEventHandlers(String event_name) {
		return this.handlers_map.get(event_name);
	}

	public ViewEventHandler getSingleEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name))
				return handler;
		}

		return null;

	}

	public ViewEventHandler removeEventHandler(String event_name, String handler_name) {

		List<ViewEventHandler> handlers = this.handlers_map.get(event_name);

		int index = 0;
		for (ViewEventHandler handler : handlers) {
			if (((NamedEventHandler) handler).getName().equals(handler_name)) {
				return handlers.remove(index);
			}
			index++;
		}

		return null;

	}

	public List<ViewEventHandler> removeAllEventHandlers(String event_name) {
		return this.handlers_map.remove(event_name);
	}

}
