package Server;

public class AuthenticationServer {

	public static void main(String[] args) {
		// TODO: get argument/port and pass below:
		int port = 5999;
		boolean thread = false;
		try {
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-p")){
					i++;
					port  = Integer.parseInt(args[i]);
				}
				else if(args[i].equals("-t")){
					thread = true;
				}
			}
			new SServer(port, thread);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
