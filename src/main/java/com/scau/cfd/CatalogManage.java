package com.scau.cfd;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * CatalogManage 类用于管理目录操作，包括创建目录、显示目录、删除目录和切换目录。
 */
public class CatalogManage {
    public static Catalog currentCatalog; // 当前目录
    public static String absolutePath = "/"; // 当前路径
    static ArrayList<String> strings = new ArrayList<>();

    /**
     * 创建目录。
     *
     * @param dirName 要创建的目录名称
     * @return 如果目录成功创建，返回 true；否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean MakeDir(String dirName) throws IOException {
        boolean finded = false;
        if (!dirName.matches("[^$/.]{3}")) return finded; // 检查目录名称是否合法
        byte[] item = new byte[3];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw"); // 打开磁盘文件进行读写

        // 遍历当前目录所在盘块，检查是否有重名的目录
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item);
            if (dirName.equals(new String(item, StandardCharsets.US_ASCII))) {
                System.out.println("the dir has existed");
                return false;
            }
        }

        // 查找空闲位置并创建新目录
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (item[0] == '$') {
                finded = true;
                // 新建子目录并且改变文件分配表
                Catalog son = new Catalog(dirName);
                son.parent = currentCatalog.parent;
                son.location = Main.disk.findEmpty();
                file.seek(son.location);
                file.write(255);
                son.attribute = 0x08;

                // 填写目录项
                file.seek(currentCatalog.location * 64 + i * 8);
                file.write(son.name);
                file.write(' ');
                file.write(' ');
                file.write(son.attribute);
                file.write(son.location);
                file.write(son.parent);

                // 将新建子目录下内容初始化为空
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
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r"); // 打开磁盘文件进行读取
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
        boolean isEmpty = true;
        byte[] item = new byte[8];
        byte[] location = new byte[1];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw"); // 打开磁盘文件进行读写

        // 查找要删除的目录
        for (int i = 0; i < 8; i++) {
            file.seek(currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            if (dirName.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                byte rmLocation = item[6];
                // 检查目录是否为空
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
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r"); // 打开磁盘文件进行读取
        byte[] item = new byte[3];

        if (dirName.equals("..")) {
            // 返回上一级目录
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
            if (!Objects.equals(absolutePath, "/")) {
                absolutePath = absolutePath.substring(0, absolutePath.length() - 3); // 假设目录名固定为3时有效，有待改进
                if (!absolutePath.equals("/")) {
                    absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
                }
            }
        } else if (dirName.equals("/")) {
            // 切换到根目录
            currentCatalog.location = 2;
            currentCatalog.parent = 2;
            absolutePath = "/";
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
                    absolutePath = absolutePath + dirName + "/";
                    return true;
                }
            }
            System.out.println("could not find the dir");
        } else {
            System.out.println("unknown directory");
        }
        return false;
    }
}
