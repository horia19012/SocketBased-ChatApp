import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<SocketHandler> connections;
	private ServerSocket server;
	private boolean active = true;
	private ExecutorService pool;

	public Server() {

		active = true;
		this.connections = new ArrayList<>();
	}

	public void broadcast(String msg) {
		for (SocketHandler sh : connections) {
			sh.writeMessage(msg);
		}
	}

	public void run() {
		try {
			server = new ServerSocket(9999);
			pool = Executors.newCachedThreadPool();

			while (active) {
				Socket client = server.accept();
				SocketHandler handler = new SocketHandler(client);
				connections.add(handler);

				pool.execute(handler);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutDown() {
		try {
			pool.shutdown();
			active = false;

			for (SocketHandler sh : connections) {
				sh.shutDown();
			}

			if (!server.isClosed()) {
				server.close();
			}
		} catch (Exception e) {
			// ignoring
		}
	}

	public class SocketHandler implements Runnable {

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
				System.out.println(username + " connected!");
				broadcast(username + " joined!");
				String message;
				while ((message = in.readLine()) != null) {
					if (message.startsWith("/quit")) {

						broadcast(username + " left the chat..");
						shutDown();
					} else {
						broadcast(username + ": " + message);
					}
				}

			} catch (Exception e) {
				shutDown();
			}
		}

		public void shutDown() {
			try {
				in.close();
				out.close();

				if (!client.isClosed()) {
					client.close();
				}
			} catch (Exception e) {

			}
		}

		public void writeMessage(String msg) {
			out.println(msg);
		}

	}

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
}
