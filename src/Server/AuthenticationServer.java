package Server;

public class AuthenticationServer {

	public static void main(String[] args) {
		// TODO: get argument/port and pass below:
		try {
			new SServer(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
