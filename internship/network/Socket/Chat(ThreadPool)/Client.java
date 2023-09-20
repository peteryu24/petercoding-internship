/*
package gmx.chat;

import java.io.*;
import java.net.BindException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private String name;
    private Socket socket;
    private final String serverIP = "127.0.0.1";
    private int chatPort = 7777;
    private Scanner scanner = new Scanner(System.in);

    // 스레드 풀 선언
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        try {
            connectServer();
            startChat();
        } catch (IOException e) {
            connectionError();
        }
    }

    private void connectServer() throws IOException {
        while (true) {
            try {
                socket = new Socket(serverIP, chatPort);
                System.out.print("서버와 연결되었습니다. 대화명을 입력하세요: ");
                name = scanner.nextLine();
                break;
            } catch (BindException e) {
                System.out.println("포트 " + chatPort + "이(가) 사용 중입니다. 다른 포트로 시도합니다.");
                chatPort++;
            }
        }
    }

    private void startChat() {
        threadPool.submit(new ClientReceiver(socket));
        threadPool.submit(new ClientSender(socket, name));
    }

    private void connectionError() {
        System.out.println("서버에 연결할 수 없습니다.");
        System.out.print("재시작을 원하면 1을 입력하세요. 종료하려면 6을 입력하세요: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
        case 1:
            start();
            break;
        case 6:
            threadPool.shutdown();  // 스레드 풀 종료
            System.exit(1);
            break;
        default:
            System.out.println("잘못된 선택입니다.");
            threadPool.shutdown();  // 스레드 풀 종료
            System.exit(1);
        }
    }

    class ClientReceiver extends Thread {
        private DataInputStream input;

        ClientReceiver(Socket socket) {
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                handleStreamError("데이터를 가져올 수 없습니다.");
            }
        }

        @Override
        public void run() {
            try {
                while (input != null) {
                    System.out.println(input.readUTF());
                }
            } catch (IOException e) {
                handleStreamError("데이터를 가져올 수 없습니다.");
            }
        }
    }

    class ClientSender extends Thread {
        private DataOutputStream output;

        ClientSender(Socket socket, String name) {
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(name);
                System.out.println(name + "님이 대화방에 입장하였습니다.\n채팅을 입력하세요 (exit 입력시 종료): ");
            } catch (IOException e) {
                handleStreamError("데이터를 전송할 수 없습니다.");
            }
        }

        @Override
        public void run() {
            try {
                while (output != null) {
                    String str = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(str)) {
                        closeIO();
                        threadPool.shutdown();  // 스레드 풀 종료
                        System.exit(0);
                    }
                    output.writeUTF("[" + name + "]" + str);
                }
            } catch (IOException e) {
                handleStreamError("메세지 전송 오류.");
            }
        }
    }

    private void closeIO() {
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.out.println("자원 해제 중 오류 발생");
        }
    }

    private void handleStreamError(String errorMessage) {
        System.out.println(errorMessage);
        System.out.print("재시작을 원하면 해당 숫자를 입력하세요 (2: 데이터 수신 재시작, 3: 데이터 전송 재시작, 4~6: 종료): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
        case 2:
            threadPool.submit(new ClientReceiver(socket));
            break;
        case 3:
            threadPool.submit(new ClientSender(socket, name));
            break;
        case 4:
        case 5:
        case 6:
            System.out.println("프로그램을 종료합니다.");
            threadPool.shutdown();  // 스레드 풀 종료
            System.exit(1);
            break;
        default:
            System.out.println("잘못된 선택입니다.");
            threadPool.shutdown();  // 스레드 풀 종료
            System.exit(1);
        }
    }
}
*/
package gmx.chat;

import java.io.*;
import java.net.BindException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    static String name;
    static Socket socket;
    private final String serverIP = "127.0.0.1";
    private int chatPort = 7777;
    static Scanner scanner = new Scanner(System.in);

    // 스레드 풀 선언
    final static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        try {
            connectServer();
            startChat();
        } catch (IOException e) {
            connectionError();
        }
    }

    private void connectServer() throws IOException {
        while (true) {
            try {
                socket = new Socket(serverIP, chatPort);
                System.out.print("서버와 연결되었습니다. 대화명을 입력하세요: ");
                name = scanner.nextLine();
                break;
            } catch (BindException e) {
                System.out.println("포트 " + chatPort + "이(가) 사용 중입니다. 다른 포트로 시도합니다.");
                chatPort++;
            }
        }
    }

    private void startChat() {
        threadPool.submit(new ClientReceiver(socket));
        threadPool.submit(new ClientSender(socket, name));
    }

    private void connectionError() {
        System.out.println("서버에 연결할 수 없습니다.");
        System.out.print("재시작을 원하면 1을 입력하세요. 종료하려면 6을 입력하세요: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                start();
                break;
            case 6:
                threadPool.shutdown();  // 스레드 풀 종료
                System.exit(1);
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                threadPool.shutdown();  // 스레드 풀 종료
                System.exit(1);
        }
    }
}

