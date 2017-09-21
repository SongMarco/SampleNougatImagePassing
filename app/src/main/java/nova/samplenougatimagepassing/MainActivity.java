package nova.samplenougatimagepassing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView img;

    private String imgPath = "";


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        img = ( ImageView ) findViewById( R.id.img );

        findViewById( R.id.btnCamera ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            showCamera();

            }
        } );

        findViewById( R.id.button).setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                doTakeAlbumAction();
            }
        });
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if( resultCode == RESULT_OK){
            switch ( requestCode ) {
                case 1:

                    //비트맵을 파일에서 얻어와 회전값에 따라 다르게 세팅하여, 이미지뷰에 붙여준다!!

                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgPath, bounds);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bm = BitmapFactory.decodeFile(imgPath, opts);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(imgPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);


                    img.setImageBitmap( rotatedBitmap );


                    break;

                case 2:

                    // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                    // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                   Uri mImageCaptureUri = data.getData();
//
//                galAdapter.addItem(new GalItem(mImageCaptureUri));
//                galAdapter.notifyDataSetChanged();



                    img.setImageURI( mImageCaptureUri );

                    break;
            }
        }
    }

    private Uri getFileUri() {
        File dir = new File( getFilesDir(), "img" );
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        File file = new File( dir, System.currentTimeMillis() + ".png" );
        imgPath = file.getAbsolutePath();

        Log.v("dd",getApplicationContext().getPackageName()+".fileprovider");



        return FileProvider.getUriForFile( MainActivity.this, getApplicationContext().getPackageName()+".fileprovider", file );
    }

    private void showCamera() {
        Intent itt = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );


        itt.putExtra( MediaStore.EXTRA_OUTPUT, getFileUri() );
        startActivityForResult( itt, 1 );
    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 2);
    }

}