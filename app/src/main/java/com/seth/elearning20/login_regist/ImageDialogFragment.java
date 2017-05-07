package com.seth.elearning20.login_regist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.seth.elearning20.R;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by Seth on 2017/5/7.
 */

public class ImageDialogFragment extends DialogFragment implements View.OnClickListener{

    private TextView get_from_album;
    private TextView get_from_camera;

    private String usrPhone;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0x11;//本地
    private static final int CODE_CAMERA_REQUEST = 0x22;//拍照
    private static final int CODE_RESULT_REQUEST = 0x33;//最终裁剪后的结果
    /*设置一个Url储存截图文件*/
    private File tempFile;
    private Uri tempUri;
    /*设置头像大小*/
    private static final int output_X = 800;
    private static final int output_Y = 800;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_choose_frog, container, false);
        usrPhone = "18868217689";
        get_from_album = (TextView) view.findViewById(R.id.take_from_album);
        get_from_camera = (TextView) view.findViewById(R.id.take_from_camera);
        /***********创建url*/
        File dir = new File(Environment.getExternalStorageDirectory()+"/Frog");
        if(!dir.exists()){
            dir.mkdirs();
        }
        tempFile = new File(Environment.getExternalStorageDirectory()+"/Frog" + File.separator + usrPhone + ".jpg");
        tempUri = Uri.fromFile(tempFile);
        /****************************************/

        get_from_album.setOnClickListener(this);
        get_from_camera.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_from_album:
                choseFrogFromAlbum();
                break;
            case R.id.take_from_camera:
                choseFrogFromCamera();

                break;
        }

    }

    private void choseFrogFromCamera() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);

    }

    private void choseFrogFromAlbum() {
        Intent intentFromAlbum = new Intent();
        // 设置文件类型
        intentFromAlbum.setType("image/*");
        intentFromAlbum.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromAlbum,CODE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //用户没有进行有效的设置操作，返回。
        if (resultCode == RESULT_CANCELED){
            toast("取消");
            return;
        }
        switch (requestCode){
            case CODE_GALLERY_REQUEST:  //相册获取
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile1 = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile1));
                } else {
                    toast("没有SDCard!");
                }

                break;

            case CODE_RESULT_REQUEST:
                if (data!= null) {
                    Log.i("backresult","222");
                    if(tempFile!=null) {
                        Bitmap photo = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                        CompleteInfoPage.getFrog().setImageBitmap(photo);
                    }
                    onDismiss(getDialog());
                }
                break;

        }
    }

    private void cropRawPhoto(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("return-data", false); //裁剪后的数据不以bitmap的形式返回
        startActivityForResult(intent, CODE_RESULT_REQUEST);

    }
    //吐司的一个小方法
    private void toast(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

}