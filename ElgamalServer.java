
import java.io.*;import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.math.BigInteger;

public class ElgamalServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234);
             Socket clientSocket = serverSocket.accept();
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            ElgamalKeyPair keyPair = ElgamalEncryptionExample.generateKeyPair(512);
//            ElgamalPublicKey publicKey = keyPair.getPublicKey();
//            ElgamalPrivateKey privateKey = keyPair.getPrivateKey();
            ElgamalPublicKey serverPublicKey = keyPair.getPublicKey();
            ElgamalPrivateKey serverPrivateKey = keyPair.getPrivateKey();

            // 公開鍵をクライアントに送信
            out.writeObject(serverPublicKey);
            // クライアントからの公開鍵を受け取る
            ElgamalPublicKey clientPublicKey = (ElgamalPublicKey) in.readObject();

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
//                        ElgamalCipherText cipherText = (ElgamalCipherText) in.readObject();
//                        String decryptedMessage = ElgamalEncryptionExample.decrypt(serverPrivateKey, cipherText);
//                        System.out.println("Client says: " + decryptedMessage);
                        ElgamalCipherText cipherText = (ElgamalCipherText) in.readObject();
                        System.out.println("Received C1: " + cipherText.getC1());
                        System.out.println("Received C2: " + cipherText.getC2());
                        String decryptedMessage = ElgamalEncryptionExample.decrypt(serverPrivateKey, cipherText);
                        System.out.println("Client says: " + decryptedMessage);
                        System.out.println("Using private key: " + serverPrivateKey.getX());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error reading from client: " + e.getMessage());
                }
            });

            receiveThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                ElgamalCipherText cipherText = ElgamalEncryptionExample.encrypt(clientPublicKey, userInput);
                out.writeObject(cipherText);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception caught when trying to listen on port 1234 or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
//
//            Thread receiveThread = new Thread(() -> {
//                try {
//                    while (true) {
//                        ElgamalCipherText cipherText = (ElgamalCipherText) in.readObject();
//                        System.out.println("Received C1: " + cipherText.getC1());
//                        System.out.println("Received C2: " + cipherText.getC2());
//                        String decryptedMessage = ElgamalEncryptionExample.decrypt(privateKey, cipherText);
//                        System.out.println("Client says: " + decryptedMessage);
//                        System.out.println("Using private key: " + privateKey.getX());
//
//                    }
//                } catch (IOException | ClassNotFoundException e) {
//                    System.out.println("Error reading from client: " + e.getMessage());
//                }
//            });
//
//            receiveThread.start();
//
//            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
//                ElgamalCipherText cipherText = ElgamalEncryptionExample.encrypt(publicKey, userInput);
//                out.writeObject(cipherText);
//            }
//
//        } catch (IOException e) {
//            System.out.println("Exception caught when trying to listen on port 1234 or listening for a connection");
//            System.out.println(e.getMessage());
//        }
//    }
//}
