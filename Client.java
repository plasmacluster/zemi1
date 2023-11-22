import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverInput;
                    while ((serverInput = in.readLine()) != null) {
                        System.out.println("Server says: " + serverInput);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                }
            });

            receiveThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to connect to port 1234 or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
