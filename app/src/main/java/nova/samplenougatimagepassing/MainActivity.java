package nova.samplenougatimagepassing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

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
//            showCamera();
                doTakeAlbumAction();
            }
        } );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if( resultCode == RESULT_OK){
            switch ( requestCode ) {
                case 1:
                    Bitmap bitmap = BitmapFactory.decodeFile( imgPath );
                    img.setImageBitmap( bitmap );
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