import java.io.*;
import java.net.*;

public class FibonacciServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Choose any available port number

            System.out.println("Server started. Listening on port " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle the client request in a new thread to allow multiple clients to connect simultaneously
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())
            ) {
                int number = dis.readInt();
                int result = fibonacci(number);

                dos.writeInt(result);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private int fibonacci(int n) {
            int v1 = 0, v2 = 1, v3 = 0;
            for (int i = 2; i <= n; i++) {
                v3 = v1 + v2;
                v1 = v2;
                v2 = v3;
            }
            return v3;
        }
    }
}
