import java.net.Socket;
import java.io.*;
import java.math.BigInteger;

public class  ElgamalClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            // クライアントの鍵の生成
            ElgamalKeyPair clientKeyPair = ElgamalEncryptionExample.generateKeyPair(512);
            ElgamalPublicKey clientPublicKey = clientKeyPair.getPublicKey();
            ElgamalPrivateKey clientPrivateKey = clientKeyPair.getPrivateKey();

            // クライアントの公開鍵をサーバーに送信
            out.writeObject(clientPublicKey);

                // サーバーからの公開鍵を受け取る
                ElgamalPublicKey serverPublicKey = (ElgamalPublicKey) in.readObject();

                Thread receiveThread = new Thread(() -> {
                    try {
                        while (true) {
                            ElgamalCipherText cipherText = (ElgamalCipherText) in.readObject();
                            System.out.println("Received C1: " + cipherText.getC1());
                            System.out.println("Received C2: " + cipherText.getC2());
                            String decryptedMessage = ElgamalEncryptionExample.decrypt(clientPrivateKey, cipherText);
                            System.out.println("Server says: " + decryptedMessage);
                            System.out.println("Using private key: " + clientPrivateKey.getX());

                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error reading from server: " + e.getMessage());
                    }
                });

                receiveThread.start();

                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    ElgamalCipherText cipherText = ElgamalEncryptionExample.encrypt(serverPublicKey, userInput);
                    System.out.println("Using serverPublicKey: " + serverPublicKey);
                    System.out.println("cipherText: " + cipherText);
                    out.writeObject(cipherText);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Exception caught when trying to connect to port 1234 or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
