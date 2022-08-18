//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not limited to reproduction, retransmission, communication, display, mirror, download, modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.ve.lib.common.utils.file;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Arrays;


/**
 * Description: IO流工具类
 * @author Ivan.Zang
 * @date 2022/6/29
 */
public class FileUtils {
    private static final String TAG ="FileUtils";

    private FileUtils(){}

    /**
     * 读取完整的文件并返回字符串
     * @param inputStream
     * @return
     */
    public static String readCompleteFileAsString(InputStream inputStream) {
        StringBuilder fileData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        } catch (IOException e) {
            return null;
        }
        return fileData.toString();
    }

    /**
     * 读取部分文件
     * @param file
     * @param start
     * @param readSize
     *          读取的总大小
     * @return
     */
    public static byte[] readFile(File file, long start, int readSize) {
        RandomAccessFile randomFile = null;

        Object var7;
        try {
            randomFile = new RandomAccessFile(file, "r");
            randomFile.seek(start);
            byte[] bytes = new byte[readSize];
            int size = randomFile.read(bytes);
            if (size != -1) {
                byte[] var21;
                if (size < readSize) {
                    var21 = Arrays.copyOfRange(bytes, 0, size);
                    return var21;
                }

                var21 = bytes;
                return var21;
            }

            var7 = null;
        } catch (IOException var19) {
            return null;
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException var18) {
                }
            }

        }

        return (byte[])var7;
    }

    public static boolean renameFilePath(String tempPath, String apkPath) {
        File file = new File(tempPath);
        File newFile = new File(apkPath);
        delete(newFile);
        return file.renameTo(newFile);
    }

    public static void delete(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);
        } else {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    deleteFileSafely(file);
                    return;
                }

                File[] var2 = childFiles;
                int var3 = childFiles.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File childFile = var2[var4];
                    delete(childFile);
                }

                deleteFileSafely(file);
            }

        }
    }

    public static String readFileByLine(File file, int start, int lineSize) {
        RandomAccessFile randomFile = null;

        try {
            randomFile = new RandomAccessFile(file, "r");
            randomFile.seek((long)start);
            StringBuilder data = new StringBuilder();
            int i = 0;

            while(true) {
                if (i < lineSize) {
                    String readData = randomFile.readLine();
                    if (readData != null) {
                        data.append(readData);
                        ++i;
                        continue;
                    }
                }

                String var18 = data.toString();
                return var18;
            }
        } catch (IOException var16) {
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException var15) {
                }
            }

        }

        return null;
    }

    public static boolean isExists(String path) {
        return !TextUtils.isEmpty(path) && fileExists(path);
    }

    private static boolean fileExists(String path) {
        return (new File(path)).exists();
    }

    public static boolean deleteFileSafely(File file) {
        if (file == null) {
            return false;
        } else {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            return file.renameTo(tmp) && tmp.delete();
        }
    }

    public static void writeData(String filePath, String data) {
        if (!TextUtils.isEmpty(filePath)) {
            deleteFileSafely(new File(filePath));
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    FileWriter fw = new FileWriter(filePath, false);
                    fw.write(data);
                    fw.flush();
                    fw.close();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }

        }
    }

    public static void writeDataAppend(String filePath, String data) {
        if (!TextUtils.isEmpty(filePath)) {
            data = data + "\n";
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    FileWriter fw = new FileWriter(filePath, true);
                    fw.write(data);
                    fw.flush();
                    fw.close();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }

        }
    }

}
