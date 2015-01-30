/**
 * Created by sirlittle on 1/4/15.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CoolClient{
    PrintWriter writer;
    BufferedReader reader;

    public void go(){
        try{
            Socket s = new Socket("192.168.1.20", 4240);
            Scanner input = new Scanner(System.in);
            writer = new PrintWriter(s.getOutputStream());
            InputStreamReader streamReader = new InputStreamReader(s.getInputStream());
            reader = new BufferedReader(streamReader);
            InetAddress IP=InetAddress.getLocalHost();
            System.out.println("IP of my system is := "+ IP.getHostAddress());
            String sendToServer;
            ReadStuff threadstuff = new ReadStuff();
            Thread th = new Thread(threadstuff);
            th.start();
            while(true) {
                sendToServer = input.nextLine();
                writer.println(sendToServer);
                writer.flush();
                if (sendToServer.equals("/quit")) {
                    break;
                }
            }
            threadstuff.kill();
            System.out.println("Connection Terminated");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        CoolClient client = new CoolClient();
        client.go();
    }
    private class ReadStuff implements Runnable{
        boolean killed = false;
        public void run(){
            String message;
            try{
                while(!killed){
                    message = reader.readLine();
                    if (message != null && message.equals("terminate")){
                        break;
                    }
                    else if(message != null){
                        System.out.println(message);
                    }
                }
                writer.close();
                reader.close();
            }
            catch(SocketException se){
                System.out.println("Quit while Socket was tryna read, but its k.");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        public void kill(){
            killed = true;
        }
    }

}