import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ArrayList<SocketHandler> connections;

	public void broadcast(String msg) {
		for (SocketHandler sh : connections) {
			sh.writeMessage(msg);
		}
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(9999);
			Socket client = server.accept();
			SocketHandler handler = new SocketHandler(client);
			connections.add(handler);
		} catch (Exception e) {

		}
	}

	public class SocketHandler {
		private Socket client;
		private BufferedReader in;
		private PrintWriter out;

		private String username;

		public SocketHandler(Socket client) {
			this.client = client;

		}

		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(client.getOutputStream(), true);
				out.println("Enter an username:");
				username = in.readLine();
				System.out.println(username + " connected!Feel free to talk...");
				broadcast(username + " joined!");
			} catch (Exception e) {

			}
		}

		public void writeMessage(String msg) {
			out.println(msg);
		}
	}
}
