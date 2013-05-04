package cz.mencik.ws.server;

import java.io.IOException;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(serviceName = "AddNumbersService", targetNamespace = "http://duke.example.org")
public class AddNumbersImpl {

	public int addNumbers(int number1, int number2) {
		// throw new UnsupportedOperationException();
		System.out.println("Received Request!");
		System.out.println("Sleeping for 5 seconds");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return number1 + number2;
	}

	public static void main(String[] args) throws IOException {
		String url = args.length > 0 ? args[0] : "http://localhost:8080/ws-server/AddNumbers";
		Endpoint ep = Endpoint.publish(url, new AddNumbersImpl());
		System.out.println("Web service endpoint published at: " + url);
		System.out.println("Press Enter key to stop the web service endpoint");
		System.in.read();
		ep.stop();
	}
}