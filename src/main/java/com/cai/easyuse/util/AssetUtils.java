package com.cai.easyuse.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

/**
 * 管理Asset资源
 * 
 * @author cailingxiao
 * @date 2016年2月19日
 * 
 */
public final class AssetUtils {
    private static final String TAG = "AssetUtils";

    private AssetUtils() {

    }

    /**
     * 获取assetsManager
     * 
     * @return
     */
    public static AssetManager getAssets() {
        return ResUtils.getResources().getAssets();
    }

    /**
     * 获取assets文件根目录{@code file:///android_asset}
     * 
     * @return
     */
    public static String getAssetsRootPath() {
        return "file:///android_asset";
    }

    /**
     * 返回assets文件的完整描述{@code file:///android_asset/fileName}
     * <p>
     * 如果assets文件夹中有网页，返回值可以作为路径使用
     * 
     * @param fileName
     * @return
     */
    public static String getAssetsFilePath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return getAssetsRootPath();
        }
        if (fileName.charAt(0) == '/') {
            return getAssetsRootPath() + fileName;
        }
        return getAssetsRootPath() + File.separatorChar + fileName;
    }

    /**
     * 直接读取asset文件夹下指定路径的文件
     * 
     * @param path
     * @return 内容字符串
     */
    public static String getString(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        String result = "";
        final String realPath = path.charAt(0) == '/' ? path.substring(1) : path;
        InputStream is = null;
        try {
            AssetManager am = getAssets();
            is = am.open(realPath);
            int len = is.available();
            byte[] bytes = new byte[len];
            is.read(bytes);
            return new String(bytes);
        } catch (IOException ioe) {
            LogUtils.e(TAG, ioe.getMessage());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        } finally {
            CloseUtils.close(is);
        }
        return result;
    }

    /**
     * 把指定文件从assets文件夹下文件拷贝到sd卡的指定目录下(私有目录的docuemnt下)
     * 
     * <p>
     * 若目标目录不存在，则新建
     * 
     * @param ctx
     * @param fileName 需要在assets文件夹下打开的文件
     * @return String 拷贝出来文件的绝对路径
     */
    public static String copyAssetFile(Context ctx, String fileName) {
        return copyAssetFile(ctx, fileName, SdUtils.getPrivateDirectory(ctx, SdUtils.DIRECTORY_DOCUMENTS)
                .getAbsolutePath());
    }

    /**
     * 把指定文件从assets文件夹下文件拷贝到sd卡的指定目录下
     * 
     * @param ctx
     * @param fileName
     * @param targetDir
     * @return
     */
    public static String copyAssetFile(Context ctx, String fileName, String targetDir) {
        if (TextUtils.isEmpty(targetDir)) {
            return "";
        }
        InputStream in = null;
        try {
            in = getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        String ret = copyAssetFile(in, fileName, targetDir);
        CloseUtils.close(in);
        return ret;
    }

    /**
     * 把指定文件从assets文件夹下文件拷贝到sd卡的指定目录下
     * 
     * <p>
     * 若目标目录不存在，则新建
     * 
     * @param in 由AssetManager打开的输入流
     * @param fileName 指定目标文件名
     * @param dirOfSdCard sd卡上指定目录
     * @return String 拷贝出来文件的绝对路径
     */
    private static String copyAssetFile(InputStream in, String fileName, String dirOfSdCard) {
        File dir = new File(dirOfSdCard);
        if (!FileUtils.isDirExist(dir) && !FileUtils.mkdirs(dirOfSdCard)) {
            LogUtils.d("compress Files 创建指定目录失败");
            return "";
        }
        if (null == in) {
            LogUtils.e("compress Files inputstream is null");
            return "";
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = "default.zip";
            LogUtils.w("compress Files fileName is null");
        }
        File zipFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        if (zipFile.exists()) {
            zipFile.delete();
        } else {
            FileUtils.mkdirs(zipFile.getParent());
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return "";
        }
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            CloseUtils.close(fos);
        }
        return zipFile.getAbsolutePath();

    }
}
