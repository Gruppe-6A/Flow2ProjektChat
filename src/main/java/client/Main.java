package client;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.runClient();
    }

    private void runClient() {
        try {
            boolean go = true;
            Scanner keyboard = new Scanner(System.in);
            Socket socket = new Socket("localhost", 9001);

            // reader and writer
            Thread tr = new ReadMsg(socket);
            Thread tw = new WriteMsg(socket);

            tr.start();
            tw.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class WriteMsg extends Thread {
    Scanner keyboard;
    Socket client;
    PrintWriter pw;

    public WriteMsg(Socket socket) {
        this.client = socket;
        this.keyboard = new Scanner(System.in);
        try {
            this.pw = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        String outputString="";
        do {
            outputString = keyboard.nextLine();
            pw.println(outputString);
        } while (!outputString.equals("CLOSE"));
    }
}

class ReadMsg extends Thread {
    Socket client;
    BufferedReader br;

    public ReadMsg(Socket socket) {
        this.client = socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //
        String  inputString = "";
        boolean go=true;
        while (go) {
            try {
                if ((inputString=br.readLine()).equals("CLOSE")) {
                    go=false;
                } else {
                    System.out.println(inputString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}