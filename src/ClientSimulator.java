import java.io.*;
import java.net.*;
import java.util.Random;

public class ClientSimulator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar ClientSimulator.jar <server_ip> <port>");
            return;
        }

        String serverIp = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.err.println("Error: Port must be a valid number.");
            return;
        }

        Random random = new Random();
        while (true) {
            try {
                int requestBatch = random.nextInt(10) + 1;

                for (int i = 0; i < requestBatch; i++) {
                    Thread.sleep(random.nextInt(3000) + 1000);

                    try (Socket tcpSocket = new Socket(serverIp, port);
                         PrintWriter writer = new PrintWriter(tcpSocket.getOutputStream(), true);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()))) {

                        String[] operations = {"ADD", "SUB", "MUL", "DIV"};
                        String operation = operations[random.nextInt(operations.length)];
                        int operand1 = random.nextInt(100);
                        int operand2 = random.nextInt(100) + 1;

                        String request = operation + " " + operand1 + " " + operand2;
                        System.out.println("Client sending: " + request);
                        writer.println(request);

                        String response = reader.readLine();
                        System.out.println("Client received: " + response);
                    }
                }

                System.out.println("Client disconnecting temporarily...");
                Thread.sleep(random.nextInt(5000) + 5000);

            } catch (Exception e) {
                System.err.println("Client encountered an error: " + e.getMessage());
            }
        }
    }
}
