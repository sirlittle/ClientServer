/**
 * Created by sirlittle on 1/3/15.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CoolServer {
    public void go(){
        try{
            ServerSocket serverSock = new ServerSocket(4240);
            while(true){
                Socket sock = serverSock.accept();
                Thread th = new Thread(new MakeConnection(sock));
                th.start();
                System.out.println("Made a connection to: " + sock.getLocalAddress());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        CoolServer server = new CoolServer();
        server.go();
    }
    private class MakeConnection implements Runnable{
        Socket sock;
        PrintWriter writer;
        BufferedReader reader;

        ArrayList<String> pastchats;

        public MakeConnection(Socket sock){
            this.sock = sock;
        }
        public void run(){
            try {
                String address = sock.getLocalAddress().toString();
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                while(true) {
                    String advice = reader.readLine();
                    if(advice != null && advice.equals("/quit")){
                        writer.println("terminate");
                        writer.flush();
                        System.out.println("We quit that connection");
                        break;
                    }
                    else if(advice != null) {
                        System.out.println(advice);
                        //pastchats.add(advice);
                        writer.println(advice);
                        writer.flush();
                    }
                }
                writer.close();
                reader.close();
                System.out.println("Closed connection to " + address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
