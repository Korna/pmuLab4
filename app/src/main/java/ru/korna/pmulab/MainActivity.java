package ru.korna.pmulab;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageView_full)
    ImageView imageView_full;
    @BindDrawable(R.drawable.ic_error_outline_black_24dp)
    Drawable drawable_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            intent = new Intent(this, CameraAcitivtyApi21.class);
        else
            intent = new Intent(this, CameraActivityApi16.class);

        startActivityForResult(intent, 404);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File mFile = (File) data.getSerializableExtra("file");

        Picasso
                .with(this)
                .load(mFile)

            //    .centerCrop()
   //             .resize(imageView_full.getWidth(), imageView_full.getHeight())
                .placeholder(null)
                .error(drawable_error)
                .into(imageView_full);

        Log.d("resultIs", resultCode + "and path" + mFile);
    }
}
