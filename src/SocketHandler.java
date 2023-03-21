import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

		} catch (Exception e) {

		}
	}

}
