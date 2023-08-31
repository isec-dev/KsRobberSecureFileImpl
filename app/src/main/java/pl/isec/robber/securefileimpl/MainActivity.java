package pl.isec.robber.securefileimpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private final static String FILENAME = "encrypted.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText outputText = findViewById(R.id.editTextTextMultiLine);
        Button readButton = findViewById(R.id.button);

        readButton.setOnClickListener(view ->{
            try {
                outputText.setText(readMessage());
            } catch(Exception e){
                e.printStackTrace();
                outputText.setText("#"+ e.getMessage());
            }
        });
    }

    private String readMessage() throws GeneralSecurityException, IOException {
        /** Get or create key **/
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        /** Read EncryptedFile **/
        InputStream inputStream = new EncryptedFile.Builder(
            new File(getFilesDir(), FILENAME),
            this,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build().openFileInput();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextByte = inputStream.read();
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte);
            nextByte = inputStream.read();
        }

        return byteArrayOutputStream.toString("UTF-8");
    }
}