import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ArrayList<SocketHandler> connections;
	private ServerSocket server;
	private boolean active;

	public Server() {
		this.connections = new ArrayList<>();
		active = false;

	}

	public void shutDown() {
		try {

			active = true;
			
			for(SocketHandler sh:connections) {
				sh.shutDown();
			}
			
			if (server.isClosed() == false) {
				server.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String msg) {
		for (SocketHandler sh : connections) {
			sh.writeMessage(msg);
		}
	}

	public void run() {
		try {
			server = new ServerSocket(9999);

			while (!active) {
				Socket client = server.accept();
				SocketHandler handler = new SocketHandler(client);
				connections.add(handler);
			}
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
				String message;
				while ((message = in.readLine()) != null) {
					if (message.startsWith("/quit")) {

					} else {
						broadcast(username + ": " + message);
					}
				}

			} catch (Exception e) {

			}
		}

		public void shutDown() {
			try {
			out.close();
			in.close();
			if(!client.isClosed()) {
				client.close();
			}
		} catch (Exception e) {
				
			}
		}
		
		public void writeMessage(String msg) {
			out.println(msg);
		}
	}
}
