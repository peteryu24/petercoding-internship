package gmx.multiroomchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 7777;
    private volatile boolean running = true;

    public void startClient() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            Thread readerThread = startReaderThread(in);

            String userInput;
            while (running && !(userInput = new java.util.Scanner(System.in).nextLine()).isEmpty()) {
                out.writeUTF(userInput);
            }

            running = false;
            readerThread.join();

        } catch (UnknownHostException e) {
            System.err.println("오류: 알 수 없는 호스트: " + SERVER_ADDRESS);
        } catch (Exception e) {
            System.err.println("오류: 클라이언트 문제 발생: " + e.getMessage());
        }
    }

    private Thread startReaderThread(DataInputStream in) {
        Thread readerThread = new Thread(() -> {
            try {
                while (running) {
                    String received = in.readUTF();
                    System.out.println(received);
                }
            } catch (Exception e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        });
        readerThread.start();
        return readerThread;
    }

    public static void main(String[] args) {
        new Client().startClient();
    }
}
