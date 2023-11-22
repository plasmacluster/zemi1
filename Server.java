import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            Thread receiveThread = new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Client says: " + inputLine);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from client: " + e.getMessage());
                }
            });

            receiveThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 1234 or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
