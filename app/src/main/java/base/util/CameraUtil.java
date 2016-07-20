package base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by huangsheng1 on 2016/5/31.
 */
public class CameraUtil {

    public static String takePicture(Activity act, int requestCode){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/xkt/"+ System.currentTimeMillis() + ".jpg";
            //必须确保文件夹路径存在，否则拍照后无法完成回调
            File vFile = new File(path);
            if(!vFile.exists())
            {
                File vDirPath = vFile.getParentFile(); //new File(vFile.getParent());
                vDirPath.mkdirs();
            }
            Uri uri = Uri.fromFile(vFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
            act.startActivityForResult(intent, requestCode);
            return path;
        }else{
            return null;
        }
    }
    public static String takePicture(Fragment fragment, int requestCode){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/xkt/"+ System.currentTimeMillis() + ".jpg";
            //必须确保文件夹路径存在，否则拍照后无法完成回调
            File vFile = new File(path);
            if(!vFile.exists())
            {
                File vDirPath = vFile.getParentFile(); //new File(vFile.getParent());
                vDirPath.mkdirs();
            }
            Uri uri = Uri.fromFile(vFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
            fragment.startActivityForResult(intent, requestCode);
            return path;
        }else{
            return null;
        }
    }
    public static String takePicture(Context context){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/xkt/"+ System.currentTimeMillis() + ".jpg";
            //必须确保文件夹路径存在，否则拍照后无法完成回调
            File vFile = new File(path);
            if(!vFile.exists())
            {
                File vDirPath = vFile.getParentFile(); //new File(vFile.getParent());
                vDirPath.mkdirs();
            }
            Uri uri = Uri.fromFile(vFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
            context.startActivity(intent);
            return path;
        }else{
            return null;
        }
    }
}
