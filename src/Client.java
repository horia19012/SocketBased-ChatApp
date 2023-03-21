import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	private Socket client;
	private boolean active = true;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			client = new Socket("127.0.0.1", 9999);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			InputHandler inHandler = new InputHandler();
			Thread t = new Thread(inHandler);
			t.start();

			String inMessage;
			while ((inMessage = in.readLine()) != null) {
				System.out.println(inMessage);
			}

		} catch (Exception e) {
			shutDown();
		}
	}

	public void shutDown() {
		this.active = false;
		try {
			in.close();
			out.close();
			if (!client.isClosed()) {
				client.close();
			}
		} catch (Exception e) {
			//
		}

	}

	class InputHandler implements Runnable {

		public void run() {
			try {
				BufferedReader inRead = new BufferedReader(new InputStreamReader(System.in));
				while (active) {
					String message = inRead.readLine();
					if (message.equals("/quit")) {
						out.println("Exited the chat...");
						inRead.close();
						shutDown();
					} else {
						out.println(message);
					}
				}
			} catch (Exception e) {
				//
			}
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}
}
