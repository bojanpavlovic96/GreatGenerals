package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class DebugFrame extends JFrame {

	private JPanel main_panel;
	private JScrollPane scroll;

	public DebugFrame() {

		this.initUI();

		this.setEventListeners();

	}

	private void initUI() {

		this.setSize(300, 700);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // exit on close closes all jframes
		// (don't know why)
		this.setResizable(false);

		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screen_size.width / 2 + 550, (int) (screen_size.getHeight() / 2));

		this.main_panel = new JPanel();
		this.main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));

		this.scroll = new JScrollPane(this.main_panel); // connect panel with scroll pane (add scroll bar
														// to the main panel)
		this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		this.add(scroll);

	}

	private void setEventListeners() {

		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'd') {

					System.out.println("D in debug");

					if (isVisible()) {
						setVisible(false);
					}
				}
			}

			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

					closeFrame();

				}

			}

		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {

				System.out.println("Window closing from debug frame ... ");
				dispose();

			}

		});

	}

	private void closeFrame() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void addMessage(String text, MessagePriority priority) {

		DebugMessage message = new DebugMessage(text, priority);

		// Dimension dim = this.getSize();
		// dim.setSize(200, 50);
		// dim.setSize(dim.getWidth() - 2 * message.getBorder_size(), 50);
		// message.setSize(dim);

		this.main_panel.add(message);

		final JScrollBar scroll_bar = this.scroll.getVerticalScrollBar();
		scroll_bar.setValue(scroll_bar.getMaximum() - 1);
		// scroll to the end to see the last message
	}

}
