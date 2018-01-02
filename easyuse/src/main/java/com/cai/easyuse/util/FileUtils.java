package com.cai.easyuse.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.text.TextUtils;

/**
 * 文件操作类
 * 在非私有目录放文件需要下面的权限
 * <p></p>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 *
 * @author cailingxiao
 * @date 2016年3月1日
 */
public final class FileUtils {
    private static final String TAG = "FileUtils";

    private static final String NO_MEDIA = ".nomedia";

    private static final int MAX_RESURSION_DEEP = 30;

    private FileUtils() {

    }

    /**
     * 获取文件名字，不包含后缀
     *
     * @param file
     *
     * @return
     */
    public static String getPureFileName(File file) {
        if (null == file) {
            return "";
        }
        String tmpFileName = file.getName();
        if (file.isDirectory()) {
            return tmpFileName;
        }
        int index = tmpFileName.indexOf(".");
        if (index > 0 && index < tmpFileName.length() - 1) {
            return tmpFileName.substring(0, index);
        } else {
            return tmpFileName;
        }
    }

    /**
     * 获取文件的名字，不包含后缀
     *
     * @param filePath
     *
     * @return
     */
    public static String getPureFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return getPureFileName(new File(filePath));
    }

    /**
     * 获取文件的扩展名
     *
     * @param file
     *
     * @return
     */
    public static String getFileExtention(File file) {
        if (null == file) {
            return "";
        }
        return getFileExtention(file.getAbsolutePath());
    }

    /**
     * 获取指定路径对应文件的扩展名
     *
     * @param filePath
     *
     * @return
     */
    public static String getFileExtention(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String ext = "";
        int index = filePath.lastIndexOf(".");
        if (index > 0 && index < filePath.length() - 1) {
            ext = filePath.substring(index + 1);
        }
        return ext;
    }

    /**
     * 非空条件下，如果目录不存在，则构建目录
     *
     * @param dir
     *
     * @return
     */
    public static boolean mkdirs(final File dir) {
        if (null != dir && !dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    /**
     * 非空条件下，如果目录不存在，则构建目录
     *
     * @param dirPath
     *
     * @return
     */
    public static boolean mkdirs(final String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        return mkdirs(new File(dirPath));
    }

    /**
     * 检查目录是否存在，并且为目录文件。
     *
     * @param dir 要检查的目录文件
     *
     * @return boolean
     */
    public static boolean isDirExist(File dir) {
        return isFileExist(dir) && dir.isDirectory();
    }

    /**
     * 检查目录是否存在，并且为目录文件
     *
     * @param dir
     *
     * @return
     */
    public static boolean isDirExist(String dir) {
        if (TextUtils.isEmpty(dir)) {
            return false;
        }
        File dirFile = new File(dir);
        return dirFile.exists() && dirFile.isDirectory();
    }

    /**
     * 文件是否存在
     *
     * @param pathFile
     *
     * @return
     */
    public static boolean isFileExist(File pathFile) {
        if (null == pathFile) {
            return false;
        }
        return pathFile.exists();
    }

    /**
     * 文件是否存在
     *
     * @param path
     *
     * @return
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return isFileExist(file);
    }

    /**
     * 修改文件名为fileName
     *
     * @param file
     * @param fileName
     *
     * @return
     */
    public static boolean rename(File file, String fileName) {
        if (null == file || TextUtils.isEmpty(fileName)) {
            return false;
        }
        if (!isFileExist(file)) {
            LogUtils.w("rename,file not exist,return false");
            return false;
        }
        File tmpFile = new File(file.getParent(), fileName);
        return file.renameTo(tmpFile);
    }

    /**
     * 将一个文件移动到另一个地方
     *
     * @param sourceFile
     * @param targetFile
     * @param deleteSource 是否删掉源文件
     *
     * @return
     */
    public static boolean move(File sourceFile, File targetFile, boolean deleteSource) {
        if (null != sourceFile && sourceFile.exists() && sourceFile.isFile() && null != targetFile) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel in = null;
            FileChannel out = null;
            boolean ret = false;
            try {
                fis = new FileInputStream(sourceFile);
                fos = new FileOutputStream(targetFile);
                in = fis.getChannel();
                out = fos.getChannel();
                in.transferTo(0, in.size(), out);
                ret = true;
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
                ret = false;
            } finally {
                CloseUtils.close(fis, in, fos, out);
                if (deleteSource) {
                    deleteSingleFile(sourceFile);
                }
            }
            return ret;
        }
        return false;
    }

    /**
     * 删除指定文件或者目录，如果是目录会删除目录下所有的文件
     *
     * @param fd
     *
     * @return
     */
    public static boolean del(File fd) {
        if (null == fd) {
            return false;
        }
        if (!isFileExist(fd.getAbsolutePath())) {
            return true;
        }
        if (fd.isDirectory()) {
            return removeDirRecurion(fd.getAbsolutePath(), 0);
        }
        return deleteSingleFile(fd);
    }

    /**
     * 删除单一文件，推荐使用
     *
     * @param file
     */
    public static boolean deleteSingleFile(File file) {
        // 部分手机删除之后，再次创建时可能会报设备繁忙的bug。故加入时间戳，重命名文件，然后进行删除。
        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        return to.delete();
    }

    /**
     * 递归删除文件夹下所有的东西
     *
     * @param dirPath
     * @param currentDeep
     *
     * @return
     */
    private static boolean removeDirRecurion(String dirPath, int currentDeep) {
        if (TextUtils.isEmpty(dirPath)) {
            LogUtils.w("removeDir, dirPath is empty, return false");
            return false;
        }

        File dir = new File(dirPath);

        // 目录不存在，直接返回true
        if (!dir.exists()) {
            return true;
        }

        boolean result = false;
        File[] files = dir.listFiles();

        // 空目录或文件，直接删除自己后返回
        if (null == files) {
            return dir.delete();
        }

        // 删除子目录及文件
        for (int i = 0, length = files.length; i < length; i++) {
            if (files[i].isDirectory() && currentDeep < MAX_RESURSION_DEEP) {
                result = removeDirRecurion(files[i].getAbsolutePath(), currentDeep + 1);
            } else {
                result = del(files[i]);
            }
        }

        // 删除当前目录
        result = dir.delete();

        dir = null; // 递归优化，防止OOM
        files = null;

        return result;
    }

    /**
     * 拷贝文件。将文件srcFile拷贝到targetDir目录下
     *
     * @param srcFile   源文件
     * @param targetDir 指定的目录
     *
     * @return
     */
    public static boolean copyFileToDir(File srcFile, File targetDir) {
        if (null == srcFile) {
            return false;
        }
        return copyFile(srcFile, new File(targetDir, srcFile.getName()));
    }

    /**
     * 获取指定目录占用空间大小。
     *
     * @param dirPath 目录绝对路径
     *
     * @return long 单位byte
     */
    public static long getDirSize(String dirPath) {
        return getDirSizeRecurion(dirPath, 0);
    }

    /**
     * 递归计算指定目录占用空间大小。
     *
     * @param dirPath     目录绝对路径
     * @param currentDeep 本次递归的深度
     *
     * @return long 单位byte
     */
    private static long getDirSizeRecurion(String dirPath, int currentDeep) {
        if (TextUtils.isEmpty(dirPath)) {
            LogUtils.w("getDirSize, path is empty, return 0");
            return 0L;
        }

        File dir = new File(dirPath);

        if (!isDirExist(dir)) {
            LogUtils.w("getDirSize, dirPath not exist or not a dir, return 0");
            return 0L;
        }

        File[] files = dir.listFiles();

        if (null == files) {
            return 0L;
        }

        long totalSize = 0L;

        for (int i = 0, length = files.length; i < length; i++) {
            if (files[i].isDirectory() && currentDeep < MAX_RESURSION_DEEP) {
                totalSize += getDirSizeRecurion(files[i].getAbsolutePath(), currentDeep + 1);
            } else {
                totalSize += files[i].length();
            }
        }

        dir = null; // 递归优化，防止OOM
        files = null;

        return totalSize;
    }

    /**
     * 获取文件大小。
     *
     * @param filePath 文件绝对路径
     *
     * @return long 单位byte
     */
    public static long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.w("getFileSize, filePath is empty, return 0");
            return 0;
        }

        return getFileSize(new File(filePath));
    }

    /**
     * 获取文件大小，只能获取文件的大小，要获取目录的大小{@link FileUtils#getDirSize} 如果为目录，直接返回0。
     *
     * @param file 文件
     *
     * @return long 单位byte
     */
    public static long getFileSize(File file) {
        if (!isFileExist(file)) {
            LogUtils.w("getFileSize, file is not exist, file:" + file.getAbsolutePath() + ", return 0");
            return 0;
        }

        if (file.isDirectory()) {
            LogUtils.w("getFileSize, file is dir, return 0");
            return 0;
        }

        return file.length();
    }

    /**
     * 解压文件到当前目录
     *
     * @param sourceFile 待解压的zip文件。
     */
    public static boolean unzipFileToCurrentDirectory(File sourceFile) {
        if (sourceFile == null) {
            return false;
        }
        return unzipFileToSpecDirectory(sourceFile, sourceFile.getParentFile()
                .getAbsolutePath());
    }

    /**
     * 解压文件到指定目录
     *
     * @param sourceFile 源文件。
     * @param targetPath 需要解压到的路径。
     */
    public static boolean unzipFileToSpecDirectory(File sourceFile,
                                                   String targetPath) {
        if (sourceFile == null || !sourceFile.exists()) {
            return false;
        }
        File pathFile = new File(targetPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        boolean success = false;

        if (!success) { // 如果minizip解压失败,采用java的解压.
            ZipFile zf = null;
            byte[] buffer = new byte[16 * 1024];
            try {
                zf = new ZipFile(sourceFile);
                Enumeration<?> entries = zf.entries();
                if (entries != null) {
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = ((ZipEntry) entries.nextElement());
                        String str = targetPath + File.separator
                                + entry.getName();
                        if (entry.isDirectory()) {
                            File targetFile = new File(str);
                            if (!targetFile.exists()) {
                                targetFile.mkdirs();
                            }
                            continue;
                        }
                        File targetFile = new File(str);
                        if (!targetFile.exists()) {
                            targetFile.createNewFile();
                        } else {
                            targetFile.delete();
                            targetFile.createNewFile();
                        }
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
                        BufferedInputStream in = new BufferedInputStream(zf.getInputStream(entry));
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        out.flush();
                        out.close();
                        in.close();
                        success = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            } finally {
                if (zf != null) {
                    try {
                        zf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        sourceFile.delete();
        return success;
    }

    /**
     * 读取文件内容，支持读取assets目录下的文件。
     *
     * @param fis 文件读取流。
     *
     * @return string 读出来的文件内容。
     */
    public static String readFileContent(InputStream fis) {
        if (fis == null) {
            return null;
        }
        String result = "";
        try {
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            result = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据文件名读取文件内容
     *
     * @param file 需要读取的文件。
     *
     * @return string 读出来的文件内容。
     */
    public static String readFileContent(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        String result = "";
        try {
            fis = new FileInputStream(file);
            result = readFileContent(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据文件名读取文件内容
     *
     * @param filePath 需要读取的文件路径。
     *
     * @return string 读取出来的文件内容。
     */
    public static String readFileContent(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        return readFileContent(file);
    }

    /**
     * 文件拷贝
     *
     * @param sourceFile 源文件。
     * @param targetPath 目标文件路径。
     */
    public static void copyFile(File sourceFile, String targetPath) {
        if (TextUtils.isEmpty(targetPath)) {
            return;
        }
        File targetFile = new File(targetPath);
        copyFile(sourceFile, targetFile);
    }

    /**
     * 文件拷贝
     *
     * @param sourceFile 源文件。
     * @param targetFile 目标文件。
     */
    public static boolean copyFile(File sourceFile, File targetFile) {
        if (sourceFile == null || !sourceFile.exists() || targetFile == null) {
            return false;
        }
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(targetFile);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 文件合并。
     *
     * @param sourceFileList 需要合并的文件列表。
     * @param targetFile     目标文件。
     */
    public static void combineFiles(List<File> sourceFileList, File targetFile) {
        if (sourceFileList == null || sourceFileList.isEmpty()
                || targetFile == null) {
            return;
        }
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            out = new FileOutputStream(targetFile).getChannel();
            int size = sourceFileList.size();
            for (int i = 0; i < size; i++) {
                in = new FileInputStream(sourceFileList.get(i)).getChannel();
                in.transferTo(0, in.size(), out);
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在文件内写入内容.
     *
     * @param content      内容
     * @param filePath     文件路径
     * @param shouldAppend 是否追加到文件末尾,true在现有内容最后追加,false删除原内容,直接覆盖.
     *
     * @return true if success,otherwise false.
     */
    public static boolean writeContentToFile(String content, String filePath,
                                             boolean shouldAppend) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                try {
                    if (filePath.indexOf("/") != -1) {
                        String parentPath = filePath.substring(0,
                                filePath.lastIndexOf("/"));
                        if (!TextUtils.isEmpty(parentPath)) {
                            File folder = new File(parentPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                        }
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            } else {
                if (!shouldAppend) {
                    file.delete();
                    file.createNewFile();
                }
            }
            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.flush();
        } catch (Exception e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean writeContentToFile(byte[] content, String filePath,
                                             boolean shouldAppend) {
        if (content == null || TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                try {
                    if (filePath.indexOf("/") != -1) {
                        String parentPath = filePath.substring(0,
                                filePath.lastIndexOf("/"));
                        if (!TextUtils.isEmpty(parentPath)) {
                            File folder = new File(parentPath);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                        }
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            } else {
                if (!shouldAppend) {
                    file.delete();
                    file.createNewFile();
                }
            }
            fos = new FileOutputStream(file, true);
            fos.write(content);
            fos.flush();
        } catch (Exception e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 从某一位置读取指定数量的内容.
     *
     * @param buf          存储内容的缓冲buffer
     * @param sourceFile   目标文件
     * @param fileStartPos 开始位置
     * @param length       需要读取的大小
     *
     * @return len 读取的实际数量.最大为{@code buf.length}
     *
     * @throws ArrayIndexOutOfBoundsException 如果
     *                                        {@code fileStartPos < 0 || length > buf.length}
     */
    public static int read(byte[] buf, File sourceFile, long fileStartPos,
                           long length) throws ArrayIndexOutOfBoundsException {
        if (buf == null) {
            return -1;
        }
        if (fileStartPos < 0 || length > buf.length) {
            throw new ArrayIndexOutOfBoundsException("start < 0 || end > len."
                    + " start=" + fileStartPos + ", count=" + length + ", len="
                    + buf.length);
        }
        if (!sourceFile.exists()) {
            return -1;
        }
        try {
            RandomAccessFile file = new RandomAccessFile(sourceFile, "r");
            if (file.length() <= fileStartPos) {
                file.close();
                return -1;
            }
            file.seek(fileStartPos);
            int ret = file.read(buf, 0,
                    (int) (Math.min(file.length() - fileStartPos, length)));
            file.close();
            return ret;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 从某一位置开始写入文件.
     *
     * @param file         目标文件
     * @param content      写入的内容
     * @param fileStartPos 从此位置开始写入
     * @param length       写入的长度.此长度不能大于{@code content.length}
     *
     * @return true if 写入成功.反之 false.
     *
     * @throws IllegalArgumentException       if {@code length < 0}
     * @throws ArrayIndexOutOfBoundsException if{@code length > content.length}
     */
    public static boolean write(File file, byte[] content, long fileStartPos,
                                int length) throws IllegalArgumentException,
            ArrayIndexOutOfBoundsException {
        if (content == null || content.length <= 0) {
            return false;
        }

        if (length < 0) {
            throw new IllegalArgumentException("length < 0 : " + length);
        }
        if (length > content.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "length > content.length. " + " length=" + length
                            + ", content.length=" + content.length);
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile targetFile = new RandomAccessFile(file, "rw");
            targetFile.seek(fileStartPos);
            targetFile.write(content, 0, length);
            targetFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取合适的大小
     *
     * @param file
     *
     * @return
     */
    public static String getPropFileSize(File file) {
        if (null == file) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        final long size = getFileSize(file);
        if (size < 1024) {
            sb.append(size);
            sb.append("Byte");
        } else if (size < 1024 * 1024) {
            sb.append(size / 1024);
            sb.append("KB");
        } else if (size / 1024 < 1024 * 1024) {
            sb.append(size / 1024 / 1024);
            sb.append("MB");
        } else {
            sb.append(size / 1024 / 1024 / 1024);
            sb.append("GB");
        }
        return sb.toString();
    }

    /**
     * 在制定路径下创建.nomedia文件
     *
     * @param dir
     *
     * @return
     */
    public static File createNoMediaFile(File dir) {
        File file = new File(dir, NO_MEDIA);
        if (!file.exists()) {
            FileUtils.mkdirs(file);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将serilizable写文件
     *
     * @param obj
     * @param file
     *
     * @return
     */
    public static boolean writeSerilizableObj(Object obj, File file) {
        if (null == file || null == obj) {
            return false;
        }
        if (!isFileExist(file.getParent())) {
            mkdirs(file.getParent());
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
            return false;
        } finally {
            CloseUtils.close(fos, oos);
        }
        return true;
    }

    /**
     * 读取serilizable文件到内存
     *
     * @param file
     *
     * @return
     */
    public static Serializable readSerilizableObj(File file) {
        if (null == file || !file.exists() || !file.canRead()) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Serializable obj = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            obj = (Serializable) ois.readObject();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
            return null;
        } finally {
            CloseUtils.close(ois, fis);
        }
        return obj;
    }
}
