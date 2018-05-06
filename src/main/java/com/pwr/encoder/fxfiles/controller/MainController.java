package com.pwr.encoder.fxfiles.controller;

import com.pwr.encoder.fxfiles.custom.CustomMessageBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import library.EncoderDecoder;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
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
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txtFiles", "*.txt"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.toString();
            textFieldFilePath.setText(filePath);
            String fileContent = readTextFromFile(filePath);
            if (fileContent != null) {
                textAreaOriginalFile.setText(fileContent);
                textAreaAfterChanges.clear();
            }
        }
    }

    public void buttonEncrypt_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.encryptFile(filePath, outPutFilePath);

            String fileContent = readTextFromFile(outPutFilePath);
            if (fileContent != null) {
                textAreaAfterChanges.setText(fileContent);
            }
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja szyfrowania nie powiodła się.", "Powód: " + "błąd odczytu z pliku").showAndWait();
        } catch (IllegalBlockSizeException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja szyfrowania nie powiodła się.", "Powód: " + "zbyt duży rozmiar pliku.").showAndWait();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja szyfrowania nie powiodła się.", "Powód: " + "Bład szyfrowania").showAndWait();
        } catch (InvalidKeyException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja szyfrowania nie powiodła się.", "Powód: " + "Zbyt duży rozmiar pliku").showAndWait();
        }
    }

    public void buttonDecrypt_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.decryptFile(outPutFilePath, outPutFilePath);


            String fileContent = readTextFromFile(outPutFilePath);
            if (fileContent != null) {
                textAreaAfterChanges.setText(fileContent);
            }
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja deszyfrowania nie powiodła się.", "Powód: " + "plik do deszyfrowania nie istnieje.").showAndWait();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja deszyfrowania nie powiodła się.", "Powód: " + "Plik nie został zaszyfrowany").showAndWait();
        } catch (IllegalBlockSizeException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja deszyfrowania nie powiodła się.", "Powód: " + "Zbyt duży rozmiar pliku").showAndWait();

        }
    }

    private String readTextFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja odczytu z pliku nie powiodła się.", "Powód: " + "Błąd odczytu z pliku").showAndWait();
        }
        return null;
    }

    public void buttonChooseKeyPath_onAction(ActionEvent actionEvent) {
        File rootDirectory;
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the root directory");
        rootDirectory = dc.showDialog(null);
        if (rootDirectory.isDirectory()) {
            keysPath = rootDirectory.toString();
            textFieldKeysPath.setText(keysPath);
        }
    }

    public void buttonGenerateNewKeys_onAction(ActionEvent actionEvent) {
        try {
            encoderDecoder.createNewKeys(keysPath);
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie", "Operacja generacji kluczy nie powiodła się.", "Powód: " + "Błąd zapisu do pliku.").showAndWait();
        }
    }
}
