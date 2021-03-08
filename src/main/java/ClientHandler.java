import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    ChatServer server1;
    Socket client;
    private String clientName;
    BufferedReader br;
    PrintWriter pw;
    static int id=0;

    public String getClientName() {
        return clientName;
    }
    public Socket getClient(){
        return client;
    }


    @Override
    public void run(){
        try {
            protocol();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void message(String msg){
        pw.println(msg);
    }

    public void protocol() throws IOException{
            String[] input = br.readLine().split("#");

            if (input[0] != "CONNECT"){
                pw.println("CLOSE#1");
                client.close();
                return;

            } else {
                clientName = input[1];
                server1.clientList.add(this);
               server1.Online();
            }
            while(input[0] != "CLOSE"){
                 input = br.readLine().split("#");
                switch(input[0]){
                    case "SEND": input[1];
                    case "CLOSE": pw.println("CLOSE#0"); client.close();break;
                    default: pw.println("CLOSE#1"); client.close();break;
                }
            }

    }

}
