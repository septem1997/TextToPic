package com.lambda.texttopic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatHelper;
import com.lambda.texttopic.wxapi.WXEntryActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{

    private Button GenerateButton;
    private Button SaveButton;
    private Button ShareButton;
    private EditText EditText;
    private TextView GeneratedText;
    private Context context;
    private Bitmap bitmap;
    private String path;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ShareSDK.initSDK(this);
        context = this;

        verifyStoragePermissions(MainActivity.this);

        GenerateButton = (Button) findViewById(R.id.GenerateButton);
        SaveButton = (Button) findViewById(R.id.SaveButton);
        EditText = (EditText) findViewById(R.id.text);
        GeneratedText = (TextView) findViewById(R.id.GenerateTextView);
        ShareButton = (Button) findViewById(R.id.ShareButton);


        GenerateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String s = EditText.getText().toString();
                GeneratedText.setText(s);
                drawText(s, GeneratedText.getPaint());

            }
        });


        SaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveImage();
            }
        });

        ShareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showShare();
            }
        });
    }

    private void showShare() {
        ShareSDK.initSDK(this);

        Platform.ShareParams shareParams = new Wechat.ShareParams();
        shareParams.setImageData(bitmap);
        shareParams.setShareType(Platform.SHARE_EMOJI);

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
       // wechat.setPlatformActionListener();
        wechat.share(shareParams);
    }


    private void saveImage()
    {
        try
        {
            path = Environment.getExternalStorageDirectory() + "/BiaoQingBao/" + System.currentTimeMillis() + ".png";
            File dirFirstFolder = new File(Environment.getExternalStorageDirectory() + "/BiaoQingBao");
            File f = new File(path);
            if (!dirFirstFolder.exists())
                dirFirstFolder.mkdirs();

            FileOutputStream os = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            Toast.makeText(getApplicationContext(), "已保存在:" + path, Toast.LENGTH_LONG).show();


        }catch ( Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "保存失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void drawText(String s, TextPaint textPaint)
    {
        try
        {

            String[] sa = s.split("\n");
            int line = sa.length;
            String theLongestLine = sa[0];
            for (String a : sa)
            {
                if (a.length() > theLongestLine.length())
                {
                    theLongestLine = a;
                }
            }

            bitmap = Bitmap.createBitmap((int) textPaint.measureText(theLongestLine), (int) (textPaint.descent() - textPaint.ascent()) * line + 10, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);

            int y = (int) (textPaint.descent() - textPaint.ascent()) - 10;

            for (String a : sa)
            {
                int baseX = (int) (canvas.getWidth() / 2 - textPaint.measureText(a) / 2);

                canvas.drawText(a, baseX, y, textPaint);
                y += (int) (textPaint.descent() - textPaint.ascent());
            }

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "生成失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


}
