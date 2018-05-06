package com.pwr.encoder.fxfiles.controller;

import com.pwr.encoder.fxfiles.custom.CustomMessageBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import library.EncoderDecoder;


import javax.crypto.IllegalBlockSizeException;
import java.beans.Encoder;
import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private String keysPath = "keys";
    private String outPutFilePath = "encoded";
    private String filePath;

    public Button buttonChooseFile;
    public Button buttonChooseKeyPath;
    public Button buttonGenerateNewKeys;


    public TextField textFieldKeysPath;
    public TextField textFieldFilePath;
    public TextArea textAreaOriginalFile;
    public TextArea textAreaAfterChanges;

    public Button buttonEncrypt;
    public Button buttonDecrypt;

    private EncoderDecoder encoderDecoder;
    private CustomMessageBox customMessageBox;

    public void initialize(URL location, ResourceBundle resources) {
        //Cryptography cryptography = new Cryptography();
        //EncoDecoder encoDecoder = new EncoDecoder();
        customMessageBox = new CustomMessageBox();
        try {
            encoderDecoder = new EncoderDecoder();
            encoderDecoder.createNewKeys(keysPath);
            keysPath = new File(keysPath).getAbsolutePath();
            textFieldKeysPath.setText(keysPath);


        } catch (IOException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    public void buttonChooseFile_onAction(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("/"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txtFiles","*.txt"));
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile !=null){
            filePath = selectedFile.toString();
            textFieldFilePath.setText(filePath);
            String fileContent = readTextFromFile(filePath);
            if (fileContent!=null){
                textAreaOriginalFile.setText(fileContent);
                textAreaAfterChanges.clear();
            }
        }
    }

    public void buttonEncrypt_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.encryptFile(filePath,outPutFilePath);
            String fileContent = readTextFromFile(outPutFilePath);
            if (fileContent!=null){
                textAreaAfterChanges.setText(fileContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(javax.crypto.IllegalBlockSizeException e){
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja wymiany informacji nie powiodła się.",
                    "Powód: " + "zbyt duży rozmiar pliku.").showAndWait();
        }
    }

    public void buttonDecrypt_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.decryptFile(outPutFilePath,outPutFilePath);
            String fileContent = readTextFromFile(outPutFilePath);
            if (fileContent!=null){
                textAreaAfterChanges.setText(fileContent);
            }
        } catch (IOException e ) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja deszyfrowania nie powiodła się.",
                    "Powód: " + "plik do deszyfrowania nie istnieje.").showAndWait();        }
        catch ( Exception e){
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja deszyfrowania nie powiodła się.",
                    "Powód: " + "plik jest niezaszyfrowany.").showAndWait();
        }
    }

    private String readTextFromFile(String path){
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void buttonChooseKeyPath_onAction(ActionEvent actionEvent) {
        File rootDirectory;
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the root directory");
        rootDirectory = dc.showDialog(null);
        if (rootDirectory.isDirectory()) {
            keysPath=rootDirectory.toString();
            textFieldKeysPath.setText(keysPath);
        }
    }

    public void buttonGenerateNewKeys_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.createNewKeys(keysPath);
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja generacji kluczy nie powiodła się.",
                    "Powód: " + "Błąd zapisu do pliku.").showAndWait();
        }
    }
}
