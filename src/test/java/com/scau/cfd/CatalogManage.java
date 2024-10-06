package com.scau.cfd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class CatalogManage {
    public static Catalog currentCatalog;
    public static String absolutePath="/";

    public static boolean MakeDir(String dirName) throws IOException {
        boolean finded = false;
        if(!dirName.matches("[^$/.]{3}")) return finded;
        byte[] item = new byte[3];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item);
            if (dirName.equals(new String(item, StandardCharsets.US_ASCII))) {
                System.out.println("the dir has existed");
                return false;
            }
        }
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (item[0] == '$') {
                finded = true;
//                新建子目录并且改变文件分配表
                Catalog son = new Catalog(dirName);
                son.parent = currentCatalog.parent;
                son.location = Main.disk.findEmpty();
                file.seek(son.location);
                file.write(255);
                son.attribute = 0x08;

//               填写目录项
                file.seek(currentCatalog.location * 64 + i * 8);
                file.write(son.name);
                file.write(' ');
                file.write(' ');
                file.write(son.attribute);
                file.write(son.location);
                file.write(son.parent);

//                将新建子目录下内容初始化为空
                for (int j = 0; j < 8; j++) {
                    file.seek(son.location * 64 + j * 8);
                    file.write('$');

                }
                break;
            }

        }
        file.close();
        return finded;
    }

    public static boolean ShowDir() throws IOException {
        byte[] item = new byte[3];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (item[0] != '$') {
                System.out.println(new String(item, StandardCharsets.US_ASCII));
            }
        }
        file.close();
        return true;
    }

    public static boolean RemoveDir(String dirName) throws IOException {
        boolean finded = false;
        byte[] item = new byte[3];
        byte[] location = new byte[1];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (dirName.equals(new String(item, StandardCharsets.US_ASCII))) {
                file.seek(currentCatalog.location * 64 + i * 8);
                file.write('$');
                file.seek(currentCatalog.location * 64 + i * 8 + 6);
                file.read(location);
                file.seek(location[0]);
                file.write(0);
                finded = true;
            }
        }
        file.close();
        return finded;
    }
    public static boolean ChangeDirectory(String dirName) throws IOException {
        RandomAccessFile file=new RandomAccessFile(Main.disk.file,"r");
        byte[] item=new byte[3];
        if(dirName.equals(".."))
        {
            currentCatalog.location=currentCatalog.parent;
            for(int i=0;i<8;i++)
            {
                byte[] item1=new byte[8];
                file.seek(currentCatalog.parent*64+i*8);
                file.read(item1,0,8);
                if(item1[0]!='$')
                {
                    currentCatalog.parent=item1[7];
                    break;
                }
            }
            if(!Objects.equals(absolutePath, "/"))
            {
                absolutePath=absolutePath.substring(0,absolutePath.length()-3);//假设目录名固定为3时有效，有待改进
                if (!absolutePath.equals("/"))
                {
                    absolutePath=absolutePath.substring(0,absolutePath.length()-1);
                }
            }
        }
        else if (dirName.equals("/"))
        {
            currentCatalog.location=2;
            currentCatalog.parent=2;
            absolutePath="/";
        } else if (dirName.matches("[^$/.]{1,3}")) {
            for (int i = 0; i < 8; i++) {
                file.seek(currentCatalog.location * 64 + i * 8);
                file.read(item, 0, 3);
                if (dirName.equals(new String(item, StandardCharsets.US_ASCII))) {
                    byte[] location=new byte[1];
                    file.seek(currentCatalog.location * 64 + i * 8 + 6);
                    file.read(location);
                    currentCatalog.parent=currentCatalog.location;
                    currentCatalog.location=location[0];
                    absolutePath=absolutePath+dirName+"/";
                    return true;
                }
            }
            System.out.println("could not find the dir");
        }else {
            System.out.println("unknown directory");
        }
        return false;
    }
}
