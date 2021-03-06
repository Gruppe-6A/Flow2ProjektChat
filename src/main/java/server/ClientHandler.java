package server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class ClientHandler extends Thread {
    ChatServer server1;
    Socket client;
    private String clientName;
    BufferedReader br;
    PrintWriter pw;

    public ClientHandler(ChatServer server1, Socket client) {
        this.server1 = server1;
        this.client = client;
        try {
            pw = new PrintWriter(client.getOutputStream(),true);
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run(){
        try {
            this.pw.println();
            protocol();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void message(String msg){
        pw.println(msg);
    }

    public void msgToAll(String msg) {
        for (Map.Entry<String, ClientHandler> entry : server1.clientMap.entrySet()) {
            entry.getValue().message("MESSAGE#" + this.clientName +"#" + msg);
        }
    }

    public void PrivateMessage (String clientName, String msg) throws IOException {
            String[] clientNameArray = clientName.split(",");

            for (String CNA : clientNameArray) {
                if (server1.clientMap.containsKey(CNA)) {
                    server1.clientMap.get(CNA).message("MESSAGE#" + this.clientName + "#" + msg);
                } else {
                    close2();
                }
            }
        }
    private void close0() throws IOException {
        pw.println("CLOSE#0");
        client.close();
        server1.clientMap.remove(clientName, this);
        server1.Online();
    }
    private void close1() throws IOException {
        pw.println("CLOSE#1");
        client.close();
        if (server1.clientMap.containsKey(clientName)){
            server1.clientMap.remove(clientName, this);
        }
        server1.Online();
    }
    private void close2() throws IOException {
        pw.println("CLOSE#2");
        client.close();
        if (server1.clientMap.containsKey(clientName)){
            server1.clientMap.remove(clientName, this);
        }
        server1.Online();
    }

        public void protocol () throws IOException {
            String[] input = br.readLine().split("#");
            if (input[0].equals("CONNECT")) {
                clientName = input[1];
                if (!server1.clientMap.containsKey(input[1])) {
                    server1.clientMap.put(input[1], this);
                    server1.Online();
                } else {
                    close1();
                }

            } else {
                close1();
                return;
            }
            while (!input[0].equals("CLOSE")) {
                try {
                    input = br.readLine().split("#");
                } catch (NullPointerException e){
                    server1.clientMap.remove(clientName, this);
                    server1.Online();
                    return;
                } catch (SocketException e){
                    server1.clientMap.remove(clientName, this);
                    server1.Online();
                    return;
                }
                switch (input[0]) {

                    case "SEND":
                        if (input[1].equals("*")) {
                            msgToAll(input[2]);
                        } else {
                            PrivateMessage(input[1], input[2]);
                        }break;
                    case "CLOSE":
                       close0();
                        break;
                    default:
                       close1();
                        break;
                }
            }
        }
    }
