package gmx.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread {
    private DataOutputStream output;
    private Socket socket;
    private String name;
    private Scanner scanner = new Scanner(System.in);

    public ClientSender(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
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
                    Client.threadPool.shutdown();  // 스레드 풀 종료
                    System.exit(0);
                }
                output.writeUTF("[" + name + "]" + str);
            }
        } catch (IOException e) {
            handleStreamError("메세지 전송 오류.");
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
                Client.threadPool.submit(new ClientReceiver(Client.socket));
                break;
            case 3:
                Client.threadPool.submit(new ClientSender(Client.socket, Client.name));
                break;
            case 4:
            case 5:
            case 6:
                System.out.println("프로그램을 종료합니다.");
                Client.threadPool.shutdown();  // 스레드 풀 종료
                System.exit(1);
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                Client.threadPool.shutdown();  // 스레드 풀 종료
                System.exit(1);
        }
    }
}
