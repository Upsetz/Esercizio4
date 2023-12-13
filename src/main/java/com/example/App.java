package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Server Partito!" );

        try {

            ServerSocket server = new ServerSocket(3000);
            
            while(true){
                
                Socket s = server.accept();
                
                System.out.println("client collegato");

                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                String string;
                string = in.readLine();

                String[] parole = string.split(" ");

                String path = parole[1].substring(1);

                while(!string.isEmpty()){

                    System.out.println("la stringa è " + string);
                    
                    string = in.readLine();
                };  
                
                File file = new File(path);
                boolean flag = file.exists();

                if(flag){

                    String msg = "il file esiste";
                    System.out.println("msg");
                    out.writeBytes("HTTP/1.1 200 OK \n");
                    out.writeBytes("Content-lenght: " + msg.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes(msg + "\n");

                }
                else{
                    
                    String msg = "il file non esiste";
                    System.out.println("il file non esiste");
                    out.writeBytes("HTTP/1.1 404 Not found\n");
                    out.writeBytes("Content-lenght: " + msg.length() + "\n");
                    out.writeBytes("\n");
                    out.writeBytes( msg + "\n");

                }

                s.close();

            }
            

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private static void sendBinaryFile(Socket socket, File file) throws IOException{
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeBytes("HTTP/1.1 200 OK \\n");
        output.writeBytes("Content-Lenght: " + file.length() + "\n");

        output.writeBytes("Content-Type: " + getContentType(file) + "\n");

        output.writeBytes("\n");
        InputStream input = new FileInputStream(file);
        byte[] buf = new byte[8192];
        int n;
        while((n = input.read(buf)) != -1){
            output.write(buf, 0, n);
        }
        input.close();
    }
}