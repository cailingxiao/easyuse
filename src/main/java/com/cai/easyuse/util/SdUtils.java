package com.cai.easyuse.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

/**
 * 方便操作sd卡数据的工具类
 *
 * @author cailingxiao
 * @date 2016年2月19日
 */
public final class SdUtils {
    /**
     * 第二块sd卡能用的目录
     */
    public static final String SECOND_SD_ACCESS_DIR;
    private static final String TAG = "SdUtils";
    private static final String EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String EXTERNAL_MK_PERMISSION = android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;
    private static final String FIRST_SD_ACCESS_DIR;
    private static final String SECOND_SD_ACCESS_DIR_ROOT;
    private static final String FIRST_SD_ACCESS_DIR_ROOT;
    private static final String NORMAL_ANDROID_DATA = "/Android/data/";
    private static final String NORMAL_FILES = "/files";
    private static final String CAMERA_DIR = "Camera";
    private static final String FIRST_SD_PATH;
    private static final String SECOND_SD_PATH;
    // 和Environment相关对应
    public static String DIRECTORY_MUSIC = "Music";
    public static String DIRECTORY_PODCASTS = "Podcasts";
    public static String DIRECTORY_RINGTONES = "Ringtones";
    public static String DIRECTORY_ALARMS = "Alarms";
    public static String DIRECTORY_NOTIFICATIONS = "Notifications";
    public static String DIRECTORY_PICTURES = "Pictures";
    public static String DIRECTORY_MOVIES = "Movies";
    public static String DIRECTORY_DOWNLOADS = "Download";
    public static String DIRECTORY_DCIM = "DCIM";
    public static String DIRECTORY_DOCUMENTS = "Documents";
    private static boolean INIT = false;

    static {
        FIRST_SD_PATH = getFirstSdCardPath(); // 第一块sd卡由Environment获得，不会为空,除非卡被卸载
        SECOND_SD_PATH = getSecondSdCardPath(); // 第二块sd卡可能为空
        INIT = true;
        FIRST_SD_ACCESS_DIR_ROOT = FIRST_SD_PATH + NORMAL_ANDROID_DATA + AppUtils.getAppPackageName(null);
        SECOND_SD_ACCESS_DIR_ROOT = SECOND_SD_PATH + NORMAL_ANDROID_DATA + AppUtils.getAppPackageName(null);
        FIRST_SD_ACCESS_DIR = FIRST_SD_ACCESS_DIR_ROOT + NORMAL_FILES;
        SECOND_SD_ACCESS_DIR = SECOND_SD_ACCESS_DIR_ROOT + NORMAL_FILES;
    }

    private SdUtils() {

    }

    /**
     * 获取照相机的目录
     * <p>
     * 如果没有会自动建立该目录
     *
     * @return
     */
    public static File getCameraDirectory() {
        return new File(getExternalStoragePublicDirectory(DIRECTORY_DCIM), CAMERA_DIR);
    }

    /**
     * 获取公共目录，用于和其他应用分享数据，可以传入相关type参数获得对应得目录，如果需要存储照片、音乐、电影、下载等，推荐使用
     * <p>
     * 一般目录在sd卡上的/DCIM 、 /MUSIC、/MOVIE、/DOWNLOAD等,为了不污染根目录数据，一般少自己建立文件夹，除非知道这样做的后果
     * <p>
     * type最好使用下面定义的，否则会自动建立在根目录上
     * <p>
     * 如果没有会自动建立该目录
     *
     * @param type The type of storage directory to return. Should be one of {@link #DIRECTORY_MUSIC},
     *             {@link #DIRECTORY_PODCASTS}, {@link #DIRECTORY_RINGTONES}, {@link #DIRECTORY_ALARMS},
     *             {@link #DIRECTORY_NOTIFICATIONS}, {@link #DIRECTORY_PICTURES}, {@link #DIRECTORY_MOVIES},
     *             {@link #DIRECTORY_DOWNLOADS}, or {@link #DIRECTORY_DCIM}. May not be null.
     *
     * @return Returns the File path for the directory. Note that this directory may not yet exist, so you must make
     * sure it exists before using it such as with {@link File#mkdirs File.mkdirs()}.
     */
    public static File getExternalStoragePublicDirectory(final String type) {
        if (TextUtils.isEmpty(type)) {
            return null;
        }
        File dir;
        if (OsVerUtils.hasKitKat() && type.indexOf("/") < 0) {
            dir = Environment.getExternalStoragePublicDirectory(type);
        } else {
            dir = new File(getDefaultSdCardPath(), type);
        }
        FileUtils.mkdirs(dir);
        return dir;
    }

    /**
     * 同{@link #getExternalStoragePublicDirectory(String)},一毛一样
     * <p>
     * 如果没有会自动建立该目录
     *
     * @param type
     *
     * @return
     */
    public static File getPublicDirectory(final String type) {
        return getExternalStoragePublicDirectory(type);
    }

    /**
     * 获得私有目录的可访问根目录，在 .../Android/data/package_name/目录下
     *
     * @return
     */
    public static File getPrivateRoot() {
        File dir = new File(FIRST_SD_ACCESS_DIR_ROOT);
        FileUtils.mkdirs(dir);
        return dir;
    }

    /**
     * 获取私有目录，这个目录在api大于18是不需要android.permission.WRITE_EXTERNAL_STORAGE权限了
     * <p>
     * 效果同{@link Context#getExternalFilesDir(String)}
     * <p>
     * 文件放在/Android/data/package_name/files目录下 + type
     * <p>
     * 如果没有会自动建立该目录
     *
     * @return
     */
    public static File getPrivateDirectory(Context ctx, String type) {
        if (null == ctx) {
            return null;
        }
        File dir;
        if (OsVerUtils.hasKitKat()) {
            dir = ctx.getExternalFilesDir(type);
        } else {
            if (TextUtils.isEmpty(type)) {
                dir = new File(FIRST_SD_ACCESS_DIR);
            } else {
                dir = new File(FIRST_SD_ACCESS_DIR, type);
            }
        }
        FileUtils.mkdirs(dir);
        return dir;
    }

    /**
     * 返回/Android/data/package_name/files目录
     *
     * @param ctx
     *
     * @return
     */
    public static File getPrivateDirectory(Context ctx) {
        return getPrivateDirectory(ctx, null);
    }

    /**
     * 获得第二块sd卡的私有目录，文件在 .../Android/data/package_name/目录下
     *
     * @return
     */
    public static File getPrivateRoot2() {
        if (!hasSecondSdCard()) {
            return null;
        }
        File dir = new File(SECOND_SD_ACCESS_DIR_ROOT);
        FileUtils.mkdirs(dir);
        return dir;
    }

    /**
     * 获取第二块sd卡的私有目录，同{@link #getPrivateDirectory(Context)}
     * <p>
     * 文件放在第二块sd卡目录下的/Android/data/package_name/files目录下 + type
     * <p>
     * 如果没有会自动建立该目录
     *
     * @param type
     *
     * @return 如果没有第二块sd卡，会返回空
     */
    public static File getPrivateDirectory2(String type) {
        if (!hasSecondSdCard()) {
            return null;
        }
        File dir;
        if (TextUtils.isEmpty(type)) {
            dir = new File(SECOND_SD_ACCESS_DIR);
        } else {
            dir = new File(SECOND_SD_ACCESS_DIR, type);
        }
        FileUtils.mkdirs(dir);
        return dir;
    }

    /**
     * 获得应用在系统的目录 /data/data/package_name/files目录
     * <p>
     * 如果没有会自动建立该目录
     *
     * @param ctx
     *
     * @return
     */
    public static File getSystemDirectory(Context ctx) {
        if (null == ctx) {
            return null;
        }
        FileUtils.mkdirs(ctx.getFilesDir());
        return ctx.getFilesDir();
    }

    /**
     * 获得应用在系统的目录 /data/data/package_name/cache目录
     * <p>
     * 如果没有会自动建立该目录
     *
     * @param ctx
     *
     * @return
     */
    public static File getSystemCacheDirectory(Context ctx) {
        if (null == ctx) {
            return null;
        }
        FileUtils.mkdirs(ctx.getCacheDir());
        return ctx.getCacheDir();
    }

    /**
     * 计算文件夹/文件所在sd卡的可用空间
     *
     * @param path
     *
     * @return 单位：字节
     */
    public static long getAvailableSpace(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        try {
            final StatFs stats = new StatFs(path);
            if (OsVerUtils.hasKitKat()) {
                return stats.getAvailableBlocksLong() * stats.getBlockSizeLong();
            } else {
                return (long) stats.getAvailableBlocks() * (long) stats.getBlockSize();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 计算文件夹/文件所在sd卡的可用空间
     *
     * @param pathFileOrDir
     *
     * @return 单位：字节
     */
    public static long getAvailableSpace(File pathFileOrDir) {
        return getAvailableSpace(pathFileOrDir.getAbsolutePath());
    }

    /**
     * 返回指定目录是否有足够空间
     *
     * @param path    指定目录
     * @param reqSize 要求的空间，单位为字节byte
     *
     * @return
     */
    public static boolean hasEnoughSpace(String path, long reqSize) {
        return getAvailableSpace(path) > reqSize;
    }

    /**
     * 返回指定目录是否有足够空间
     *
     * @param fileOrDir 指定目录
     * @param reqSize   要求的空间，单位为字节byte
     *
     * @return
     */
    public static boolean hasEnoughSpace(File fileOrDir, long reqSize) {
        return getAvailableSpace(fileOrDir) > reqSize;
    }

    /**
     * 获取第一块sd卡的绝对路径
     *
     * @return
     */
    public static String getFirstSdCardPath() {
        if (!TextUtils.isEmpty(FIRST_SD_PATH)) {
            return FIRST_SD_PATH;
        }
        if (hasDefaultSdCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取第二块SD卡路径,不推荐直接使用
     * <p>
     * android4.4之后，外置sd卡除了 /Android/data/data/package_name/目录外都没有读写权限
     *
     * @return string 第二块SD卡的绝对路径,如果不存在，返回null
     */
    public static String getSecondSdCardPath() {
        if (!TextUtils.isEmpty(SECOND_SD_PATH)) {
            return SECOND_SD_PATH;
        } else if (!INIT) {
            List<String> paths = getAllExterSdcardPath(ContextUtils.getContext());
            if (null != paths && paths.size() >= 2) {
                for (String path : paths) {
                    if (path != null && !path.equals(getFirstSdCardPath())) {
                        return path;
                    }
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 获取所有的sd卡的路径
     *
     * @return
     */
    private static List<String> getAllExterSdcardPath(Context context) {
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        String[] paths;
        try {
            paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
            return Arrays.asList(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 第一张sd卡是否挂载可用
     *
     * @return
     */
    public static boolean hasFirstSdCard() {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (Throwable e) { // (sh)it happens
            externalStorageState = "";
        }
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**
     * 第二张sd卡是否挂载可用
     *
     * @return
     */
    public static boolean hasSecondSdCard() {
        String sd2 = getSecondSdCardPath();
        if (sd2 == null) {
            return false;
        }
        return checkFsWritable(SECOND_SD_ACCESS_DIR);
    }

    /**
     * 用来检测第二张sd卡是否可用的方法,
     * <p>
     * 注意这里有一个bug，即使外置sd卡没有卸载，但是存储空间不够大，或者文件数已至最大数，此时，也不能创建新文件。
     *
     * @param dir
     *
     * @return
     *
     * @hide
     */
    private static boolean checkFsWritable(String dir) {
        if (TextUtils.isEmpty(dir)) {
            return false;
        }
        File fd = new File(dir);
        FileUtils.mkdirs(fd);

        File f = new File(dir, "bui.bui");
        try {
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得指定路径的总大小
     *
     * @param path
     *
     * @return
     */
    private static long getTotalSize(String path) {
        try {
            final StatFs stats = new StatFs(path);
            if (OsVerUtils.hasKitKat()) {
                return stats.getBlockCountLong() * stats.getBlockSizeLong();
            } else {
                return (long) stats.getBlockCount() * (long) stats.getBlockSize();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获得指定路径已经使用的大小
     *
     * @param path
     *
     * @return
     */
    private static long getUsedSize(String path) {
        try {
            final StatFs stats = new StatFs(path);
            if (OsVerUtils.hasKitKat()) {
                return (stats.getBlockCountLong() - stats.getAvailableBlocksLong()) * stats.getBlockSizeLong();
            } else {
                return (long) (stats.getBlockCount() - stats.getAvailableBlocks()) * (long) stats.getBlockSize();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获得第一个sd卡的总大小
     *
     * @return
     */
    public static long getFirstSdCardTotalSize() {
        return getTotalSize(getFirstSdCardPath());
    }

    /**
     * 获得第二个sd卡的总大小
     *
     * @return
     */
    public static long getSecondSdCardTotalSize() {
        if (TextUtils.isEmpty(SECOND_SD_PATH)) {
            return 0;
        }
        return getTotalSize(getSecondSdCardPath());
    }

    /**
     * 获取第一个sd卡剩下的大小
     *
     * @return
     */
    public static long getFirstSdCardAvailableSize() {
        return getAvailableSpace(FIRST_SD_PATH);
    }

    /**
     * 获取第二个sd卡剩下的大小
     *
     * @return
     */
    public static long getSecondSdCardAvailableSize() {
        if (TextUtils.isEmpty(SECOND_SD_PATH)) {
            return 0;
        }
        return getAvailableSpace(SECOND_SD_PATH);
    }

    /**
     * 获取第一块sd卡已经使用的空间
     *
     * @return
     */
    public static long getFirstSdCardUsedSize() {
        return getUsedSize(FIRST_SD_PATH);
    }

    /**
     * 获取第二块sd卡的已经使用的空间
     *
     * @return
     */
    public static long getSecondSdCardUsedSize() {
        if (hasSecondSdCard()) {
            return getUsedSize(SECOND_SD_PATH);
        }
        return 0L;
    }

    /**
     * 获取默认的sd卡的总大小
     *
     * @return
     */
    public static long getDefaultSdCardTotalSize() {
        return getFirstSdCardTotalSize();
    }

    /**
     * 获取手机内置存储卡路径
     *
     * @return
     */
    public static String getDefaultSdCardPath() {
        return getFirstSdCardPath();
    }

    /**
     * 判断是否加载了sd卡并可用
     *
     * @return
     */
    public static boolean hasDefaultSdCard() {
        return hasFirstSdCard();
    }

    /**
     * 判断是否有sd卡的访问权限
     *
     * @param context
     *
     * @return
     */
    public static boolean hasExternalStoragePermission(Context context) {
        return PermissionUtils.hasPermission(context, EXTERNAL_STORAGE_PERMISSION);
    }

    /**
     * 判断是否有sd卡文件创建和删除权限
     *
     * @param context
     *
     * @return
     */
    public static boolean hasExternalMkPermission(Context context) {
        return PermissionUtils.hasPermission(context, EXTERNAL_MK_PERMISSION);
    }

}
