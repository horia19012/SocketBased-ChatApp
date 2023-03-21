import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public void run() {
		try {
			ServerSocket server = new ServerSocket(9999);
			Socket client=server.accept();
		} catch (Exception e) {

		}
	}
}
