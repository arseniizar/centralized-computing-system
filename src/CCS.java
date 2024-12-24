import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class CCS {
    private static int numClients = 0;
    private static int taskCounter = 0;
    private static int failedTasks = 0;
    private static int valueSum = 0;

    private static int addOps = 0;
    private static int subOps = 0;
    private static int mulOps = 0;
    private static int divOps = 0;

    private static int tempRequests = 0;
    private static int tempErrors = 0;
    private static int tempSum = 0;

    public static void main(String[] args) {
        if (args.length != 1) {
            printError("Usage: java -jar CentralServer.jar <port>");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            printError("Invalid port number. Please enter a valid integer.");
            return;
        }

        if (!isPortOpen(port)) {
            printError("Port " + port + " is already in use. Try a different port.");
            return;
        }

        printHeader("INITIALIZING SERVICES");
        new Thread(() -> handleUDP(port)).start();

        printHeader("STARTING TCP SERVER");
        new Thread(() -> handleTCP(port)).start();

        printHeader("ACTIVATING STATS MONITOR");
        startStatsThread();
    }

    private static boolean isPortOpen(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void handleUDP(int udpPort) {
        try (DatagramSocket udpSocket = new DatagramSocket(udpPort)) {
            printInfo("Discovery service listening on port: " + udpPort);
            byte[] buf = new byte[512];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());

                if (msg.startsWith("DISCOVER")) {
                    printInfo("Discovery request received from: " + packet.getAddress());
                    byte[] reply = "SERVICE_AVAILABLE".getBytes();
                    udpSocket.send(new DatagramPacket(reply, reply.length, packet.getAddress(), packet.getPort()));
                }
            }
        } catch (SocketException e) {
            printError("Failed to open UDP socket on port " + udpPort + ". " + e.getMessage());
        } catch (IOException e) {
            printError("Error while processing UDP requests. " + e.getMessage());
        }
    }

    private static void handleTCP(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                incrementClientCount();
                printInfo("Client connected: " + clientSocket.getInetAddress());
                new Thread(() -> serveClient(clientSocket)).start();
            }
        } catch (BindException e) {
            printError("Failed to bind TCP server to port " + port + ". Port is unavailable.");
        } catch (IOException e) {
            printError("Error with the TCP server. " + e.getMessage());
        }
    }

    private static void serveClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String task;
            while ((task = reader.readLine()) != null) {
                printInfo("Received task: " + task);
                String[] parts = task.split(" ");

                if (parts.length != 3) {
                    writer.println("FAILURE: Invalid request format.");
                    updateTaskStats(false, 0);
                    continue;
                }

                String op = parts[0];
                try {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    int result = calculate(op, a, b);
                    writer.println("RESULT: " + result);
                    updateTaskStats(true, result);
                } catch (ArithmeticException e) {
                    writer.println("FAILURE: " + e.getMessage());
                    updateTaskStats(false, 0);
                } catch (NumberFormatException e) {
                    writer.println("FAILURE: Invalid numbers provided.");
                    updateTaskStats(false, 0);
                }
            }
        } catch (IOException e) {
            printError("Error communicating with client. " + e.getMessage());
        }
    }

    private static int calculate(String op, int a, int b) {
        switch (op) {
            case "ADD":
                incrementOperation("ADD");
                return a + b;
            case "SUB":
                incrementOperation("SUB");
                return a - b;
            case "MUL":
                incrementOperation("MUL");
                return a * b;
            case "DIV":
                if (b == 0) throw new ArithmeticException("Division by zero");
                incrementOperation("DIV");
                return a / b;
            default:
                throw new IllegalArgumentException("Unknown operation: " + op);
        }
    }

    private static void startStatsThread() {
        ScheduledExecutorService statsScheduler = Executors.newScheduledThreadPool(1);
        statsScheduler.scheduleAtFixedRate(() -> {
            System.out.println("=======================================");
            System.out.println("SYSTEM STATS");
            System.out.println("=======================================");
            System.out.println("Clients Connected: " + numClients);
            System.out.println("Total Tasks: " + taskCounter);
            System.out.println("Errors: " + failedTasks);
            System.out.println("Sum of Results: " + valueSum);
            System.out.println("Operations: ADD=" + addOps + ", SUB=" + subOps + ", MUL=" + mulOps + ", DIV=" + divOps);
            resetTempStats();
        }, 10, 10, TimeUnit.SECONDS);
    }

    private static void resetTempStats() {
        tempRequests = 0;
        tempErrors = 0;
        tempSum = 0;
    }

    private static synchronized void incrementClientCount() {
        numClients++;
    }

    private static synchronized void updateTaskStats(boolean success, int result) {
        taskCounter++;
        tempRequests++;
        if (success) {
            valueSum += result;
            tempSum += result;
        } else {
            failedTasks++;
            tempErrors++;
        }
    }

    private static synchronized void incrementOperation(String op) {
        switch (op) {
            case "ADD": addOps++; break;
            case "SUB": subOps++; break;
            case "MUL": mulOps++; break;
            case "DIV": divOps++; break;
        }
    }

    private static void printHeader(String header) {
        System.out.println("=======================================");
        System.out.println(">>> " + header.toUpperCase() + " <<<");
        System.out.println("=======================================");
    }

    private static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    private static void printError(String message) {
        System.err.println("[ERROR] " + message);
    }
}
