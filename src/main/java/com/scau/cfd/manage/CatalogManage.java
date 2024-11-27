package com.scau.cfd.manage;

import com.scau.cfd.app.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class CatalogManage {
    public static Catalog currentCatalog;
    public static StringProperty absolutePath = new SimpleStringProperty("/");

    public static boolean MakeDir(String dirName) throws IOException {
        boolean finded = false;
        if (!dirName.matches("[^$/.]{3}")) {
            System.out.println("文件名不正确，应为三个字符且不包含$,/或.");
            return finded;
        }
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

    /**
     * 显示当前目录下的所有子目录。
     *
     * @return 如果操作成功，返回 true；否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
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

    /**
     * 删除目录。
     *
     * @param dirName 要删除的目录名称
     * @return 如果目录成功删除，返回 true；否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean RemoveDir(String dirName) throws IOException {
        boolean finded = false;
        byte[] item = new byte[8];
        byte[] location = new byte[1];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            if (dirName.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x04) == 0x04) {
                    System.out.println("it's not a catalog");
                    return false;
                } else if ((item[5] & 0x02) == 0x02) {
                    System.out.println("fail,it belongs to system");
                    return false;
                }
                byte rmLocation = item[6];
                for (int j = 0; j < 8; j++) {
                    file.seek(rmLocation * 64 + j * 8);
                    file.read(item);
                    if (item[0] != (byte) '$') {
                        System.out.println("the dir is not empty");
                        return false;
                    }
                }
                // 删除目录
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

    /**
     * 切换目录。
     *
     * @param dirName 要切换到的目录名称
     * @return 如果目录切换成功，返回 true；否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean ChangeDirectory(String dirName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[3];
        if (dirName.equals("..")) {
            currentCatalog.location = currentCatalog.parent;
            for (int i = 0; i < 8; i++) {
                byte[] item1 = new byte[8];
                file.seek(currentCatalog.parent * 64 + i * 8);
                file.read(item1, 0, 8);
                if (item1[0] != '$') {
                    currentCatalog.parent = item1[7];
                    break;
                }
            }
            if (!Objects.equals(absolutePath.get(), "/")) {
                absolutePath.set(absolutePath.get().substring(0, absolutePath.get().length() - 3));// 假设目录名固定为3时有效，有待改进
                if (!absolutePath.get().equals("/")) {
                    absolutePath.set(absolutePath.get().substring(0, absolutePath.get().length() - 1));
                }
            }
        } else if (dirName.equals("/")) {
            currentCatalog.location = 2;
            currentCatalog.parent = 2;
            absolutePath.set("/");
        } else if (dirName.matches("[^$/.]{1,3}")) {
            // 切换到指定目录
            for (int i = 0; i < 8; i++) {
                file.seek(currentCatalog.location * 64 + i * 8);
                file.read(item, 0, 3);
                if (dirName.equals(new String(item, StandardCharsets.US_ASCII))) {
                    byte[] location = new byte[1];
                    file.seek(currentCatalog.location * 64 + i * 8 + 6);
                    file.read(location);
                    currentCatalog.parent = currentCatalog.location;
                    currentCatalog.location = location[0];
                    absolutePath.set(absolutePath.get() + dirName + "/");
                    return true;
                }
            }
            System.out.println("could not find the dir");
        } else {
            System.out.println("unknown directory");
        }
        return false;
    }

    public static ArrayList<Object> ReturnAllItemInCurrent() throws IOException {
        ArrayList<Object> allItem = new ArrayList<>();
        byte[] item = new byte[8];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item);
            if (item[0] == '$') continue;
            if ((item[5] & 0x04) == 0x04)// 文件
            {
                allItem.add(new OurFile(Arrays.copyOfRange(item, 0, 3), Arrays.copyOfRange(item, 3, 5), item[5], item[6], item[7]));
            } else if ((item[5] & 0x04) != 0x04)// 目录
            {
                allItem.add(new Catalog(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII), item[5], currentCatalog.location * 64 + i * 8));
            }
        }
        file.close();
        return allItem;
    }

    public static ArrayList<Object> ReturnAllItemInCurrent(Catalog catalog) throws IOException {
        ArrayList<Object> allItem = new ArrayList<>();
        byte[] item = new byte[8];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        for (int i = 0; i < 8; i++) {
            file.seek(catalog.location * 64 + i * 8);
            file.read(item);
            if (item[0] == '$') continue;
            if ((item[5] & 0x04) == 0x04)// 文件
            {
                allItem.add(new OurFile(Arrays.copyOfRange(item, 0, 3), Arrays.copyOfRange(item, 3, 5), item[5], item[6], item[7]));
            } else if ((item[5] & 0x04) != 0x04)// 目录
            {
                allItem.add(new Catalog(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII), item[5], catalog.location * 64 + i * 8));
            }
        }
        file.close();
        return allItem;
    }

    public static boolean ChangeName(String originName, String newName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        byte[] item = new byte[3];
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (originName.equals(new String(item, StandardCharsets.US_ASCII))) {
                file.seek(currentCatalog.location * 64 + i * 8);
                file.write(newName.getBytes());
                file.close();
                return true;
            }
        }
        return false;
    }


    public static int CatalogSize(String dirName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            if (dirName.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                file.close();
                return Size((int) item[6] * 64);
            }
        }
        file.close();
        return 0;
    }

    public static int Size(int location) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            file.seek(location + i * 8);
            file.read(item, 0, 8);
            if (item[0] != '$') {
                if ((item[5] & 0x08) == 0x08)// 目录
                {
                    sum += Size((int) item[6] * 64);
                } else {
                    return item[7];
                }
            }
        }
        file.close();
        return sum;
    }
}
