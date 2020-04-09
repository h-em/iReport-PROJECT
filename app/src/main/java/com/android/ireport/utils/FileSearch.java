package com.android.ireport.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {

    public static List<String> getDirectoryPath(String directory) {
        List<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    pathArray.add(files[i].getAbsolutePath());
                }
            }
        }

        return pathArray;
    }

    public static List<String> getFilePath(String directory) {
        List<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    pathArray.add(files[i].getAbsolutePath());
                }
            }
        }

        return pathArray;
    }
}
