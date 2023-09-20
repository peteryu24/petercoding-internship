package gmx.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver extends Thread {
    private DataInputStream input;

    public ClientReceiver(Socket socket) {
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

    private void handleStreamError(String errorMessage) {
        System.out.println(errorMessage);
        System.out.print("재시작을 원하면 해당 숫자를 입력하세요 (2: 데이터 수신 재시작, 3: 데이터 전송 재시작, 4~6: 종료): ");
        int choice = Client.scanner.nextInt();
        Client.scanner.nextLine();

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
