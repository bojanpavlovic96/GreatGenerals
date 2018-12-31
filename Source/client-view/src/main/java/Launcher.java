import java.awt.EventQueue;

import view.ClientView;
import view.DebugFrame;

public class Launcher {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				DebugFrame debug_frame = new DebugFrame();
				ClientView game_frame = new ClientView(debug_frame);

				game_frame.setVisible(true);

			}

		});

	}

}
