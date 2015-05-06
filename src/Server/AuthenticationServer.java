package Server;

public class AuthenticationServer {

	public static void main(String[] args) {
		// TODO: get argument/port and pass below:
		try {
			SServer server = new SServer(5999);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
