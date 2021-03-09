import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    ChatServer server1;
    Socket client;
    private String clientName;
    BufferedReader br;
    PrintWriter pw;
    static int id=0;

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
               server1.clientMap.put(input[1], this);
               server1.Online();
            }
            while(input[0] != "CLOSE"){
                 input = br.readLine().split("#");
                switch(input[0]){
                    //case "SEND": input[1];

                    case "CLOSE": pw.println("CLOSE#0"); client.close();break;
                    default: pw.println("CLOSE#1"); client.close();break;
                }
            }

    }

}
