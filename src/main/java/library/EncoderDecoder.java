package library;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;


public class EncoderDecoder {

    private KeyGenerator keyGenerator;
    private Cryptography cryptography;
    private String pathToPrivateKey, pathToPublicKey;

    public EncoderDecoder() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        this.keyGenerator = new KeyGenerator(4098 );
        this.cryptography = new Cryptography();

    }


    public boolean encryptFile(String pathDecryptedFile, String newPath) throws IOException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (new File(pathDecryptedFile).exists()) {
            cryptography.encryptFile(cryptography.getFileInBytes(new File(pathDecryptedFile)), new File(newPath), cryptography.getPublicKey(pathToPublicKey));
            return true;
        }
        return false;
    }

    public boolean decryptFile(String pathEncryptedFile, String newPath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        if (new File(pathEncryptedFile).exists()) {
            cryptography.decryptFile(cryptography.getFileInBytes(new File(pathEncryptedFile)), new File(newPath), cryptography.getPrivateKey(pathToPrivateKey));
            return true;
        }
        return false;
    }

    public void createNewKeys(String keysPath) throws IOException {
        keyGenerator.createKeys();
        keyGenerator.writeToFile(keysPath + "/publicKey", keyGenerator.getPublicKey().getEncoded());
        keyGenerator.writeToFile(keysPath + "/privateKey", keyGenerator.getPrivateKey().getEncoded());
        pathToPrivateKey = keysPath + "/privateKey";
        pathToPublicKey = keysPath + "/publicKey";
    }
}
