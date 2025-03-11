package com.example.guardarvideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100; // Código de solicitud de permisos
    private static final int REQUEST_VIDEO_CAPTURE = 101; // Código de solicitud de grabación de video

    private VideoView videoView;
    private Button btnGrabar, btnSalvar;
    private String currentVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        btnGrabar = findViewById(R.id.btngrabar);
        btnSalvar = findViewById(R.id.btnsalvar);

        // Verificar y solicitar permisos
        checkAndRequestPermissions();

        // Configurar el botón "Grabar Video"
        btnGrabar.setOnClickListener(v -> {
            if (checkPermissionsGranted()) {
                dispatchTakeVideoIntent();
            } else {
                Toast.makeText(this, "Se necesitan permisos para grabar video", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón "Salvar"
        btnSalvar.setOnClickListener(v -> {
            if (currentVideoPath != null) {
                saveVideoToDatabase();
            } else {
                Toast.makeText(this, "No hay video para guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Explicar por qué se necesitan los permisos
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Se necesitan permisos para grabar video", Toast.LENGTH_SHORT).show();
            }

            // Solicitar permisos
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        } else {
            // Permisos ya otorgados
            Toast.makeText(this, "Permisos otorgados", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos otorgados
                Toast.makeText(this, "Permisos otorgados", Toast.LENGTH_SHORT).show();
            } else {
                // Permisos denegados
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Error al crear el archivo de video", Toast.LENGTH_SHORT).show();
                return;
            }
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.example.guardarvideo.fileprovider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No se encontró una aplicación para grabar video", Toast.LENGTH_SHORT).show();
        }
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null); // Usa el directorio externo de la aplicación
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        currentVideoPath = video.getAbsolutePath();
        return video;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if (currentVideoPath != null) {
                Uri videoUri = Uri.parse(currentVideoPath);
                videoView.setVideoURI(videoUri);
                videoView.start();
            } else {
                Toast.makeText(this, "No se pudo obtener la ruta del video", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Grabación de video cancelada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al grabar el video", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVideoToDatabase() {
        // Aquí puedes agregar el código para guardar la ruta del video en la base de datos
        // Por ejemplo:
        // SQLliteConexion dbHelper = new SQLliteConexion(this, Transacciones.NameDB, null, 1);
        // SQLiteDatabase db = dbHelper.getWritableDatabase();
        // ContentValues values = new ContentValues();
        // values.put(Transacciones.videoPath, currentVideoPath);
        // db.insert(Transacciones.tabla, null, values);
        // db.close();

        Toast.makeText(this, "Video guardado en la base de datos", Toast.LENGTH_SHORT).show();
    }
}