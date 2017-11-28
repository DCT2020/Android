package com.example.administrator.myapplication.Module.AutoLauncherModule;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Indigo15 on 2017-06-29.
 */

public class FileManager {

    private String directoryPath;
    private String fileName;
    private String category;
    private String tag;

    public  FileManager(String directoryPath, String fileName) {
        this.directoryPath = directoryPath;
        this.fileName = fileName;
    }

    public FileManager category(String category) {
        this.category = category;
        return this;
    }

    public FileManager tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void write(String content) {
        File path = new File(directoryPath);
        if(!path.exists()) {
            if(!path.mkdirs()) {
                Log.d("FileManager: ", "Make Directories FAIL");
            }
        }
        File file = new File(directoryPath + fileName);
        if(!file.exists()) {
            Log.d("FileManager: ", "Make File FAIL");
        }

        boolean existFlag = false;
        ArrayList<String> readStrings = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                if(line.contains(category + ":" + tag + "=")) {
                    existFlag = true;
                    String origin = line.replaceFirst(category + ":" + tag + "=", "");
                    String replacedContent = line.replaceFirst(origin, content);
                    readStrings.add(replacedContent + "\n");
                } else {
                    readStrings.add(line + "\n");
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(existFlag) {
                remove();
                FileOutputStream fos = new FileOutputStream(file, true);
                for(int i = 0; i < readStrings.size(); ++i) {
                    fos.write(readStrings.get(i).getBytes());
                }
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write((category + ":" + tag + "=" + content + "\n").getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        File file = new File(directoryPath + fileName);
        String result = null;

        if(file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line;
                while((line = in.readLine()) != null) {
                    if(line.contains(category + ":") && line.contains(tag + "=")) {
                        result = line.replaceFirst(category + ":" + tag + "=", "");
                        break;
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void remove() {
        File file = new File(directoryPath + fileName);
        file.delete();
    }
}
