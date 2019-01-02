package view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DebugMessage extends JPanel {

	private DateTimeFormatter date_time_format = DateTimeFormatter.ofPattern("HH:mm:ss - dd.MM.yyyy");

	private int border_size = 2;

	public DebugMessage(String text, MessagePriority priority) {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel(LocalDateTime.now().format(this.date_time_format)));

		JLabel message = new JLabel(text);

		Color message_color = null;
		switch (priority) {
		case DebugMessage:
			message_color = Color.BLACK;
			break;

		case Warrning:
			message_color = Color.ORANGE;
			break;

		case Error:
			message_color = Color.BLUE;
			break;

		case Exception:
			message_color = Color.RED;
			break;
		}

		message.setForeground(message_color);

		this.add(message);
		this.setBorder(BorderFactory.createLineBorder(Color.blue, this.getBorder_size()));

	}

	public int getBorder_size() {
		return border_size;
	}

	public void setBorder_size(int border_size) {
		this.border_size = border_size;
	}

}