package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatServer {

    public static final int DEFAULT_PORT = 9001;
   // ArrayList<server.ClientHandler> clientList = new ArrayList<>();

    ConcurrentMap<String, ClientHandler> clientMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {


        int port = ChatServer.DEFAULT_PORT;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        ChatServer chatServer = new ChatServer();
        try {
            chatServer.runServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void Online() {
        String onlineMessage = "ONLINE#";
        for(Map.Entry<String, ClientHandler> entry : clientMap.entrySet()){
            onlineMessage += entry.getKey()+",";
        }
        for(Map.Entry<String, ClientHandler> entry : clientMap.entrySet()){
            entry.getValue().message(onlineMessage);
        }
    }








    private void runServer(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            Socket client = ss.accept();
            ClientHandler cl = new ClientHandler(this, client);

            cl.start();
        }

    }
}