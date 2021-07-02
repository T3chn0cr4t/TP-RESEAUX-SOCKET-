package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;

public class Controller {
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button connect;
    @FXML
    public Label MSG;
    /**
     * client side
     **/
    static Socket socket = null;
    static String[] msg2;
    static String filepath = "";

    private void connectClient() {
        try {
            socket = new Socket(ip.getText(), Integer.valueOf(port.getText()));
            MSG.setText("Connecting ....");
            MSG.setText(MSG.getText() + " Choose file");
            getFileName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFileName() {
        try {
            if (socket != null) {
                DataInputStream dIn = new DataInputStream(socket.getInputStream());
                String msg = dIn.readUTF();
                System.out.println(msg);
                msg2 = msg.split("%qwert%");
                for (int i = 0; i < msg2.length; i++) {
                    MSG.setText(MSG.getText() + msg2[i] + "\n");
                }
                System.out.println(msg);
            } else {
                System.out.println("socket null");
            }
        } catch (IOException e) {
            System.out.println("2: " + e);
        }
    }

    public void getFile() {
        try {
            if (socket != null) {
                if (!filepath.equals("")) {
                    byte[] data = new byte[1024];
                    int count = socket.getInputStream().read(data, 0, 1024);
                    MSG.setText("Receiving File...\n");
                    File video = new File(filepath);
                    FileOutputStream fos = new FileOutputStream(video);
                    int i = 0;
                    while (count != -1) {
                        fos.write(data, 0, count);
                        count = socket.getInputStream().read(data, 0, 1024);
                        i++;
                        System.out.println(i * 100 / video.getTotalSpace());
                    }
                    fos.close();

                    MSG.setText(MSG.getText() + "File " + filetouppload.getText()
                            + " downloaded (" + i + " bytes read)" + "\n");
                    MSG.setText(MSG.getText() + "Choose file:\n");
                    for (int j = 0; j < msg2.length; j++) {
                        MSG.setText(MSG.getText() + msg2[j] + "\n");
                    }
                    filetouppload.setText("");
                } else {
                    JOptionPane.showMessageDialog(client_connect, "You do not choose where to put your file");
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void sendFile() {
        try {
            if (socket != null) {
                if (!filepath.equals("")) {
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    System.out.print("3");
                    String msg = filetouppload.getText();
                    dOut.writeUTF(msg);
                    dOut.flush();
                } else {
                    JOptionPane.showMessageDialog(client_connect, "You do not choose where to put your file");
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void upload_file() {
        sendFile();
        getFile();
    }

    private void client_disconnect() {
        if (socket != null) {
            try {
                MSG.setText("client disconnected \n");
                socket.close();
            } catch (IOException e) {
                System.out.print(e);
            }
        }
    }


}

