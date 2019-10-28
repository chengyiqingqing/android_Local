package com.testXml;

import java.io.File;

public class FileUtil {
    // 指定目标文件夹路径(内含要分析的诸多values文件)
    public static String DIRECTORY_PATH = "E:\\nicework";
    public static String OUT_PATH = "E:\\nicework\\textExcel" + ".xls";
    public static File[] files = null;

    public static File[] getFileList() {
        File directory = new File(DIRECTORY_PATH);
        if (directory.isDirectory()) {
            return directory.listFiles();
        }
        return null;
    }

}
