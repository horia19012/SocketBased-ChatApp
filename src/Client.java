import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	private Socket client;
	private boolean active;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket client = new Socket("127.0.0.1", 9999);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (Exception e) {

		}
	}
	public void shutDown() {
		active=false;
		try {
			in.close();
			out.close();
			if(!client.isClosed()) {
				client.close();
			}
		}catch(Exception e) {
			//
		}
		
	}

	class InputHandler implements Runnable {

		public void run() {
			try {
				BufferedReader inHandler = new BufferedReader(new InputStreamReader(System.in));
				while(active) {
					String message=inHandler.readLine();
					if(message.equals("/quit"));
					inHandler.close();
					shutDown();
				}
			} catch (Exception e) {
			}
		}
	}

}
