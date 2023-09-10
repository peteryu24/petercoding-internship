package gmx.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MultiChatServer {
    private HashMap<String, DataOutputStream> clients; // <클라이언트 이름, 출력 스트림>
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new MultiChatServer().start();
    }

    public MultiChatServer() {
        clients = new HashMap<String, DataOutputStream>();
        // 여러 스레드에서 접근할 것이므로 동기화
        Collections.synchronizedMap(clients); // 여러 스레드에서 접근하기 때문에
    }

    public void start() {
        try {
            setupServerSocket();
            System.out.println("서버가 시작되었습니다.");
            acceptClientConnections();
        } catch (IOException e) {
            handleServerSocketError(e);
        }
    }

    private void setupServerSocket() throws IOException {
        serverSocket = new ServerSocket(7777); // 루프백 주소와 포트 7777
    }

    private void acceptClientConnections() {
        while (true) { // 통신이 종료되기 전까지 연결
            try {
                Socket socket = serverSocket.accept(); // 연결이 수립되면 소켓 생성(receiver)
                ServerReceiver receiver = new ServerReceiver(socket); // 소켓으로 스트림 통신 연로 개통
                receiver.start(); // run 메소드
            } catch (IOException e) {
                handleClientConnectionError(e);
            }
        }
    }

    private void handleServerSocketError(IOException e) {
        System.out.println("서버 소켓 생성 중 예외 발생");
    }

    private void handleClientConnectionError(IOException e) {
        System.out.println("클라이언트와 연결이 끊김.");
    }

    class ServerReceiver extends Thread { // client와 통신
        Socket socket;
        DataInputStream input;
        DataOutputStream output;

        public ServerReceiver(Socket socket) { // 연결된 소켓이 인자
            this.socket = socket;
            try { // 통신할 스트림 생성
                input = new DataInputStream(socket.getInputStream()); // 읽기
                output = new DataOutputStream(socket.getOutputStream()); // 쓰기
            } catch (IOException e) {
                handleSocketStreamError(e);
            }
        }

        @Override
        public void run() { // 쓰레드를 상속받은 start메소드에서 start()로 실행
            String name = "";
            try {
                name = input.readUTF(); // 클라이언트가 서버에 접속하면 대화방에 알림
                announceClientConnection(name);
                clients.put(name, output); // 해시맵에 추가
                printClientConnectionMessage(name);
                printCurrentClientCount();

                while (input != null) {
                    String message = input.readUTF();
                    sendToAllClients(message);
                }
            } catch (IOException e) {
                handleClientDisconnection(name);
            } finally {
                removeClientFromList(name);
                announceClientDisconnection(name);
                printCurrentClientCount();
            }
        }

        private void handleSocketStreamError(IOException e) {
            System.out.println("client 소켓 생성 중 에러 발생.");
        }

        private void announceClientConnection(String name) {
            String connectionMessage = "#" + name + "[ip:" + socket.getInetAddress() + " port:" + socket.getPort() + "]"
                    + "님이 대화방에 접속하였습니다.";
            sendToAllClients(connectionMessage);
        }

        private void printClientConnectionMessage(String name) {
            System.out.println(name + "[ip:" + socket.getInetAddress() + " port:" + socket.getPort() + "]"
                    + "님이 대화방에 접속하였습니다.");
        }

        private void printCurrentClientCount() {
            System.out.println("현재 " + clients.size() + "명이 대화방에 접속 중입니다.");
        }

        private void sendToAllClients(String message) {
            Iterator<String> it = clients.keySet().iterator();
            while (it.hasNext()) {
                try {
                    DataOutputStream dos = clients.get(it.next());
                    dos.writeUTF(message);
                } catch (Exception e) {
                    handleSendingMessageError();
                }
            }
        }

        private void handleSendingMessageError() {
            // 메세지 전송 중
