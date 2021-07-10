package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
public class Controller {
    @FXML
    private AnchorPane container;
    @FXML
    private TextField portserv;
    @FXML
    private TextArea server_info;

    @FXML
    private TextField folderpath;

    /**
     * Server Side
     */
    public static String serverFilepath = "";
    public static ServerSocket socketServer;
    public static Socket sock;
    public static boolean clientaccepted = false;
    static String filepath = "";




    public void server_start() {
        int j = Integer.valueOf(portserv.getText());
        try {
            if (!folderpath.getText().equals("")) {
                if (socketServer == null) {
                    socketServer = new ServerSocket(j);
                    server_info.appendText("Waiting...\n");
                    System.out.println("Waiting");
                    sock = socketServer.accept();
                    this.clientaccepted = true;
                    server_info.appendText(server_info.getText() + "Accepted connection : " + sock + "\n");
                    if (sock != null) {
                        System.out.println(sock);
                        sendfilename();
                    } else System.out.println("socket is null");


                }
            } else {
                // JOptionPane.showMessageDialog(this, "You do not choose where to put your file");
            }

        } catch (IOException e) {
            System.out.println("server not working: " + e);
        }

    }

    public void server_close() {
        if (sock != null) {
            try {
                server_info.setText("server closed \n");
                sock.close();
                sock = null;
                socketServer = null;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    private void choose_folder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        folderpath.setText(f.getAbsolutePath());
        System.out.println(folderpath);
    }
    public String getfilename(){
        String msg="";
        try{
            if(sock!=null){
                DataInputStream dIn = new DataInputStream(sock.getInputStream());
                msg=dIn.readUTF();
                System.out.println(msg);
                System.out.println(filepath);
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        finally{
            return msg;
        }
    }
    public String filename(){
        File directoryPath = new File(folderpath.getText());
        File [] filesList = directoryPath.listFiles();
        System.out.print(filesList);
        String msg = "" ;

        for(File file:filesList){
            msg+=file.getName()+"%qwert%";
            System.out.print(msg);
        }
        return msg ;

    }
    public  void sendfile2(){
        filepath=getfilename();
        try{
            if(sock!=null && !filepath.equals("")){
                File file=new File(folderpath+"/"+filepath);
                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                OutputStream stream = sock.getOutputStream();
                int i=1;
                int count = is.read(bytes, 0, 1024);
                server_info.appendText("Sending " + filepath + "(" + file.getTotalSpace() + " bytes)"+"\n");

                long p=0;

                while (count != -1) {
                    stream.write(bytes, 0, 1024);
                    System.out.println(i);
                    i++;
                    p=Integer.toUnsignedLong(i*100)/(file.getTotalSpace());
                    System.out.println(p);
                    count = is.read(bytes, 0, 1024);
                }

                is.close();
                stream.close();
                server_info.appendText("Done."+"\n");
                filepath="";
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public void sendfilename(){
        try{
            if(sock!=null){
                DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
                System.out.print("3");
                String msg = filename() ;
                dOut.writeUTF(msg);
                dOut.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }


    }

    @FXML
    public void close() {
        ((Stage) container.getScene().getWindow()).close();
    }

    public void back() throws IOException {
        AnchorPane main = FXMLLoader.load(getClass().getResource("sample.fxml"));
        container.getChildren().setAll(main);
    }

}
