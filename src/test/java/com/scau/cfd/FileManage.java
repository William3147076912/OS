package com.scau.cfd;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FileManage {


    public static ArrayList<openedFile> openedFileArrayList=new ArrayList<>();

    public static int getOpenedFileListLength() {
        return openedFileArrayList.size();
    }

    public static boolean CreateFile(String filename, byte fileAttribute) throws IOException {
        boolean finded = false;
        byte[] item = new byte[3];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item);
            if (filename.equals(new String(item, StandardCharsets.US_ASCII))) {
                System.out.println("the file or dir with same name has existed");
                return false;
            }
        }
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (item[0] == '$') {
                finded = true;

//                新建文件并且改变文件分配表
                OurFile son = new OurFile(filename);
                son.number=Main.disk.findEmpty();
                file.seek(son.number);
                file.write(255);
                son.attribute=fileAttribute;
                son.type=new byte[]{'A','A'};
                son.length=1;
//注意：此时文件类型默认为“AA”，文件长度默认为1，文件属性默认为0x04

//               填写文件项
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
                file.write(son.filename);
                file.write(son.type);
                file.write(son.attribute);
                file.write(son.number);
                file.write(son.length);
                break;
            }

        }
        file.close();
        return finded;
    }

    public static boolean OpenFile(String filename,byte type) throws IOException {
        RandomAccessFile file=new RandomAccessFile(Main.disk.file, "r");
        byte[] item=new byte[8];
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item,0,2), StandardCharsets.US_ASCII))) {
                //然后判断已打开文件列表中是否有已有该文件
                String currentFile=CatalogManage.absolutePath+filename;
                for (openedFile openedFile:openedFileArrayList)
                {
                    if (openedFile.pathAndFilename.equals(currentFile))
                    {
                        System.out.println("the file has been opened");
                        return true;
                    }
                }
                openedFile file1=new openedFile(currentFile);
                file1.attribute=item[5];
                file1.number=item[6];
                file1.flag=type;
                file1.read=new pointer(file1.number,0);
                file1.write=file1.read;
                openedFileArrayList.add(file1);
                return true;
            }
        }
        System.out.println("could not find the file");
        return false;
    }

    public static boolean ReadFile(String filename,int length) throws IOException{
        FileManage.OpenFile(filename,(byte) 'r');

        return true;
    }

    public static boolean WriteFile(String filename) {
        return true;
    }

    public static boolean CloseFile(String filename) {
        return true;
    }

    public static boolean DeleteFile(String filename) {
        return true;
    }

    public static boolean TypeFile(String filename) {
        return true;
    }

    public static boolean ChangeFile(String filename) {
        return true;
    }
}
