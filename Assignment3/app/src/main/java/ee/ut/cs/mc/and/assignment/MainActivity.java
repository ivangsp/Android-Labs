package ee.ut.cs.mc.and.assignment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String HEROKU_URL = " https://image-processing2.herokuapp.com/upload";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;
    private ImageView unprocced_img;
    String TAG  = "MY_DEBUGGER";
    private LinearLayout linlaHeaderProgress;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unprocced_img       = (ImageView) findViewById(R.id.unprocced_img);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        textView            = (TextView) findViewById(R.id.textView);

    }

    public void localButtonClicked(View v){
        Toast.makeText(this, "Blurring image...", Toast.LENGTH_LONG).show();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.sniff);
        new LocalTask(bitmap).execute();
    }
    public void cloudButtonClicked(View v){
        if(bitmap != null) {
            new CloudTask(bitmap).execute();
        }
        else{
            Toast.makeText(this, "Please first select an image", Toast.LENGTH_LONG).show();
        }
    }

    public void selectImageFromGallery(View v) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {

            Uri uri = data.getData();

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                 //Log.d(TAG, String.valueOf(bitmap));
                textView.setVisibility(View.GONE);
                unprocced_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /** Our abstract AsyncTask for image processing.
     *  It will display the processing result in a UI dialog.
     *  Implementation must define the processImage() method
     */
    abstract class ImageProcessingAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        protected abstract Bitmap processImage();

        protected Bitmap doInBackground(Void... voids) {
            return processImage();
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            linlaHeaderProgress.setVisibility(View.GONE);
            unprocced_img.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            showImage(bmp);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }
    }

    /** Send file to Heroku app and display the response image in UI */
    private class CloudTask extends ImageProcessingAsyncTask {
        private final Bitmap bmp;

        public CloudTask(Bitmap bmp) {
            this.bmp = bmp;
        }

        @Override
        protected Bitmap processImage() {

            try {
                URL url = new URL(HEROKU_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();

                ByteArrayBody bab = new ByteArrayBody(data, "raw_image.jpg");
                entity.addPart("uploaded_file", bab);

                conn.addRequestProperty("Content-length", entity.getContentLength() + "");
                conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

                OutputStream os = conn.getOutputStream();
                entity.writeTo(conn.getOutputStream());
                os.close();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                    bitmap = BitmapFactory.decodeStream(responseStream);
                    conn.disconnect();
                    return bitmap;
                }

            } catch (Exception e) {
                e.printStackTrace();
                // something went wrong. connection with the server error
            }

            return null;
        }

    }

    /** --- Code below this comment does not need to be changed --- */

    private class LocalTask extends ImageProcessingAsyncTask {
        private final Bitmap bmp;
        public LocalTask(Bitmap bmp) {
            this.bmp = bmp;
        }

        @Override
        protected Bitmap processImage() {
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int[] pixels = AndroidUtils.bitmapToIntArray(bmp);

            BoxBlurFilter boxBlurFilter = new BoxBlurFilter();
            boxBlurFilter.setRadius(10);
            boxBlurFilter.setIterations(10);
            int[] result = boxBlurFilter.filter(pixels, width, height);

            return Bitmap.createBitmap(result, width, height, Bitmap.Config.ARGB_8888);
        }
    }


    /** Opens a pop-up dialog displaying the argument Bitmap image */
    public void showImage(Bitmap bmp) {
        Dialog builder = new Dialog(this);
        ImageView imageView = new ImageView(this);

        //Scale down the bitmap to avoid bitmap memory errors, set it to the imageview.
        //This code scales it such that the width is 1280
        int nh = (int) ( bmp.getHeight() * (1280.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 1280, nh, true);
        imageView.setImageBitmap(scaled);

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}