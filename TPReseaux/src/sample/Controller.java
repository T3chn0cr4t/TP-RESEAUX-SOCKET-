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
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button connect;
    @FXML
    private TextArea MSG;
    @FXML
    private TextField fileName;
    @FXML
    private TextField folderpath;
    @FXML
    private TextField filepath;

    /**
     * client side
     **/
    static Socket socket = null;
    static String[] msg2;
   // static String filepath = "";



    /**
     * client side
     **/
    public void connectClient() {
        try {
            socket = new Socket(ip.getText(), Integer.valueOf(port.getText()));
            MSG.setText("Connecting .... \n");
            MSG.setText(MSG.getText() + " Choose file");
            getFileName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFileName() throws IOException {

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

    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        filepath.setText(file.getAbsolutePath());
        fileName.setText(filepath.getText());
    }

    public void getFile() {
        try {
            if (socket != null) {
                    byte[] data = new byte[1024];
                    int count = socket.getInputStream().read(data, 0, 1024);
                    MSG.setText("Receiving File...\n");
                    File video = new File(filepath.getText());
                    FileOutputStream fos = new FileOutputStream(video);
                    int i = 0;
                    while (count != -1) {
                        fos.write(data, 0, count);
                        count = socket.getInputStream().read(data, 0, 1024);
                        i++;
                        System.out.println(i * 100 / video.getTotalSpace());
                    }
                    fos.close();

                    MSG.setText(MSG.getText() + "File " + fileName.getText()
                            + " downloaded (" + i + " bytes read)" + "\n");
                    MSG.setText(MSG.getText() + "Choose file:\n");
                    for (int j = 0; j < msg2.length; j++) {
                        MSG.setText(MSG.getText() + msg2[j] + "\n");
                    }
                    fileName.setText("");


            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendFileName() {
        try {
            if (socket != null) {

                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    System.out.print("3 : file sent");
                    String msg = fileName.getText();
                    dOut.writeUTF(msg);
                    dOut.flush();

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void upload_file() {
        sendFileName();
        getFile();
    }

    @FXML
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

    public void clientGo(ActionEvent actionEvent) throws IOException {
        AnchorPane register = FXMLLoader.load(getClass().getResource("client.fxml"));
        container.getChildren().setAll(register);
    }

    public void serverGo(ActionEvent actionEvent) throws IOException {
        AnchorPane register = FXMLLoader.load(getClass().getResource("server.fxml"));
        container.getChildren().setAll(register);
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

