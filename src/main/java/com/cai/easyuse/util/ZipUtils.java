package com.cai.easyuse.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩、解压缩文件工具类
 * 
 * @author cailingxiao
 * @date 2016年3月2日
 * 
 */
public final class ZipUtils {
    private static final String TAG = "ZipUtils";

    private ZipUtils() {

    }

    /**
     * 压缩文件,文件名有中文会乱码，推荐使用ant中的org.apache.tools.ant.taskdefs.Zip来实现
     * <p>
     * 由于需要引用其他包，如果需要请下载使用
     * 
     * @param filePath 待压缩的文件路径
     * @return 压缩后的文件
     * @throws RuntimeException
     */
    public static File zip(String filePath) throws RuntimeException {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                target.delete(); // 删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 添加对应的文件Entry
                addEntry("/", source, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                CloseUtils.close(zos);
                CloseUtils.close(fos);
            }
        }
        return target;
    }

    /**
     * 扫描添加文件Entry
     * 
     * @param base 基路径
     * 
     * @param source 源文件
     * @param zos Zip文件输出流
     * @throws IOException
     */
    private static void addEntry(String base, File source, ZipOutputStream zos) throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            for (File file : source.listFiles()) {
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(entry + "/", file, zos);
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                CloseUtils.close(bis);
                CloseUtils.close(fis);
            }
        }
    }

    /**
     * 解压文件并删除原zip文件
     * 
     * @param sourceFile
     * @param targetDir
     * @return
     */
    public static boolean unzipFile(File sourceFile, String targetDir) {
        return unzipFile(sourceFile, targetDir, true);
    }

    /**
     * 解压文件.
     * 
     * @param sourceFile 待解压文件
     * @param targetDir 解压路径
     * @param deleteZip 是否删除原zip包
     * @return
     */
    public static boolean unzipFile(File sourceFile, String targetDir, boolean deleteZip) {
        if (sourceFile == null || !sourceFile.exists()) {
            return false;
        }
        File pathFile = new File(targetDir);
        FileUtils.mkdirs(pathFile);
        boolean success = false;

        ZipFile zf = null;
        byte buffer[] = new byte[1024 * 256];
        try {
            zf = new ZipFile(sourceFile);
            Enumeration<?> entries = zf.entries();
            if (entries != null) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    InputStream in = zf.getInputStream(entry);
                    String str = targetDir + File.separator + entry.getName();
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
                    OutputStream out = new FileOutputStream(targetFile);
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }

                    out.flush();
                    in.close();
                    out.close();
                    success = true;
                }
            }
        } catch (Exception e) {
            success = false;
        } finally {
            CloseUtils.close(zf);
        }
        if (deleteZip) {
            sourceFile.delete();
        }

        return success;
    }

}
