package com.scau.cfd;

import javafx.scene.control.SplitMenuButton;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileManage {

    public static ArrayList<OpenedFile> openedFileArrayList = new ArrayList<>();

    public static int getOpenedFileListLength() {
        return openedFileArrayList.size();
    }

    public static boolean isOpened(String currentFileWithPath) {
        for (OpenedFile openedFile : openedFileArrayList) {
            if (openedFile.pathAndFilename.equals(currentFileWithPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建文件。
     *
     * @param filename      文件名
     * @param fileAttribute 文件属性
     * @return 如果文件创建成功返回 true，否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean CreateFile(String filename, byte fileAttribute) throws IOException {
        boolean finded = false;
        byte[] item = new byte[3];
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");

        // 检查当前目录下是否存在同名文件或目录
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item);
            if (filename.equals(new String(item, StandardCharsets.US_ASCII))) {
                System.out.println("the file or dir with same name has existed");
                return false;
            }
        }

        // 查找空闲目录项
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 3);
            if (item[0] == '$') {
                finded = true;

//                新建文件并且改变文件分配表
                OurFile son = new OurFile(filename);
                son.number = Main.disk.findEmpty();
                file.seek(son.number);
                file.write(255);
                file.seek(son.number * 64);
                file.write('#');
                son.attribute = fileAttribute;
                son.type = new byte[]{'A', 'A'};
                son.length = 1;
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

    public static OpenedFile OpenFile(String filename, byte type) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];

        // 查找文件
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                //然后判断已打开文件列表中是否有已有该文件
                String currentFile = CatalogManage.absolutePath + filename;
                for (OpenedFile openedFile : openedFileArrayList) {
                    if (openedFile.pathAndFilename.equals(currentFile)) {
                        System.out.println("the file has been opened");
                        return openedFile;
                    }
                }

                // 创建新的已打开文件对象
                OpenedFile file1 = new OpenedFile(currentFile);
                file1.attribute = item[5];
                file1.number = item[6];
                file1.flag = type;
                file1.read = new Pointer(file1.number, 0);
                file1.write = file1.read;
                openedFileArrayList.add(file1);
                return file1;
            }
        }
        System.out.println("could not find the file");
        return null;
    }

    /**
     * 读取文件。
     *
     * @param filename 文件名
     * @param length   需要读取的长度
     * @return 如果读取成功返回 true，否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean ReadFile(String filename, int length) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        byte[] item = new byte[8];
        byte[] buffer = new byte[0];

        // 查找文件
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x08) == 0x08) {
                    System.out.println("failed, it's not a file,it's a directory");
                    file.close();
                    return false;
                }
                //然后判断已打开文件列表中是否有已有该文件
                // 检查文件是否已打开
                String currentFile = CatalogManage.absolutePath + filename;
                if (!isOpened(currentFile)) {
                    System.out.println("the file has not been opened,try to open it...");
                    if (OpenFile(filename, (byte) 'r') == null) {
                        System.out.println("open file error");
                        file.close();
                        return false;
                    }
                }
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8 + 7);
                byte location = item[6];
                byte nextblockNum;
                for (int k = 0; location >= 0; k++) {
                    file.seek(location);
                    nextblockNum = file.readByte();
                    buffer = Arrays.copyOf(buffer, buffer.length + 64);
                    file.seek(location * 64);
                    file.read(buffer, k * 64, 64);
                    location = nextblockNum;
                }
                String s = new String(Arrays.copyOfRange(buffer, 0, buffer.length), StandardCharsets.US_ASCII);
                s = s.split("#")[0];
                System.out.println(s);
                CloseFile(filename);
                file.close();
                return true;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return false;

    }

    public static boolean WriteFile(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        byte[] item = new byte[8];
        byte[] buffer = new byte[0];

        // 查找文件
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x04) != 0x04) {
                    System.out.println("failed, it's not a file or it's a system file");
                    file.close();
                    return false;
                }
                //然后判断已打开文件列表中是否有已有该文件
                // 检查文件是否已打开
                String currentFile = CatalogManage.absolutePath + filename;
                if (!isOpened(currentFile)) {
                    System.out.println("the file has not been opened,try to open it...");
                    if (OpenFile(filename, (byte) 'w') == null) {
                        System.out.println("open file error");
                        file.close();
                        return false;
                    }
                }
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8 + 7);
                byte location = item[6];
                byte nextblockNum;
                for (int k = 0; location > 2; k++) {
                    file.seek(location);
                    nextblockNum = file.readByte();
                    buffer = Arrays.copyOf(buffer, buffer.length + 64);
                    file.seek(location * 64);
                    file.read(buffer, k * 64, 64);
                    location = nextblockNum;
                }
                String s = new String(Arrays.copyOfRange(buffer, 0, buffer.length), StandardCharsets.US_ASCII);
                System.out.println(s);
                s = s.split("#")[0];
                System.out.println(s);
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
//                file.seek(item[6] * 64 + s.length());
//                file.write(input.getBytes());
                String newContent = s + input;
                System.out.println("newContent:" + newContent);
                int initBlockNum = item[7];
                int blockNum = newContent.length() / 64 + 1;
                //修改文件项长度
                item[7] = (byte) blockNum;
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8 + 7);
                file.write(item[7]);
                location = item[6];
                boolean done = false;
//                if (newContent.length() >= 64) {
                int k;
                for (k = 0; k < initBlockNum; k++) {
                    file.seek(location * 64);
                    file.write(newContent.substring(64 * k, (newContent.length() - 64 * k) > 64 ? 64 * (k + 1) : newContent.length()).getBytes());
                    if ((newContent.length() - 64 * k) <= 64) {
                        file.write('#');
                        done = true;
                    }
                    file.seek(location);
                    nextblockNum = file.readByte();
                    if (location > 2)
                        location = nextblockNum;
                }
                for (; k < blockNum; k++) {
                    file.seek(location);
                    nextblockNum = Main.disk.findEmpty();
                    file.write(nextblockNum);
                    file.seek(nextblockNum);
                    file.write(0xFF);
                    location = nextblockNum;
                    file.seek(location * 64);
                    file.write(newContent.substring(64 * k, (newContent.length() - 64 * k) > 64 ? 64 * (k + 1) : newContent.length()).getBytes());
                    if ((newContent.length() - 64 * k) <= 64) {
                        file.write('#');
                    }
                }
                CloseFile(filename);
                file.close();
                return true;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return false;
    }

    public static boolean CloseFile(String filename) {
        String currenFile = CatalogManage.absolutePath + filename;
        for (OpenedFile openedFile : openedFileArrayList) {
            if (openedFile.pathAndFilename.equals(currenFile)) {
                openedFileArrayList.remove(openedFile);
                return true;
            }
        }
        System.out.println("could not find the file to close");
        return false;
    }

    public static boolean DeleteFile(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "rw");
        byte[] item = new byte[8];

        // 查找文件
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x04) != 0x04) {
                    System.out.println("failed, it's not a file or it's a system file");
                    file.close();
                    return false;
                }
                //然后判断已打开文件列表中是否有已有该文件
                // 检查文件是否已打开
                String currentFile = CatalogManage.absolutePath + filename;
                if (isOpened(currentFile)) {
                    System.out.println("the file has been opened,could not delete it");
                    file.close();
                    return false;
                }

                // 删除文件
//                System.out.println(CatalogManage.currentCatalog.location * 64 + i * 8);
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
                file.write((byte) '$');
                int location = item[6];
                byte blockNum;
                do {
                    file.seek(location);
                    blockNum = file.readByte();
                    file.seek(location);
                    file.write(0);
                    location = blockNum;
                } while (location > 2);
                file.close();
                return true;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return false;
    }

    public static String TypeFile(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x04) != 0x04) {
                    System.out.println("failed, it's not a file");
                    file.close();
                    return null;
                }
                //然后判断已打开文件列表中是否有已有该文件
                String currentFile = CatalogManage.absolutePath + filename;
                if (isOpened(currentFile)) {
                    System.out.println("打开则不能显示文件内容");
                    file.close();
                    return null;
                }
//                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8 + 7);
                byte location = item[6];
                byte[] buffer = new byte[0];
                byte nextblockNum;
                for (int k = 0; location >= 0; k++) {
                    file.seek(location);
                    nextblockNum = file.readByte();
                    buffer = Arrays.copyOf(buffer, buffer.length + 64);
                    file.seek(location * 64);
                    file.read(buffer, k * 64, 64);
                    location = nextblockNum;
                }
                String s = new String(Arrays.copyOfRange(buffer, 0, buffer.length), StandardCharsets.US_ASCII);
                s = s.split("#")[0];
                file.close();
                return s;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return null;
    }

    /**
     * 修改文件。
     *
     * @param filename 文件名
     * @return 如果修改成功返回 true，否则返回 false
     */
    public static boolean Change(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if ((item[5] & 0x04) != 0x04) {
                    System.out.println("failed, it's not a file");
                    file.close();
                    return false;
                }
                //然后判断已打开文件列表中是否有已有该文件
                String currentFile = CatalogManage.absolutePath + filename;
                if (isOpened(currentFile)) {
                    System.out.println("打开则不能改变属性");
                    file.close();
                    return false;
                }
                System.out.println("当前属性值：" + item[5]);
                System.out.println("请输入要修改的属性值：");
                Scanner scanner = new Scanner(System.in);
                int attribute = scanner.nextInt();
                if (attribute > 15 || attribute < 1) {
                    System.out.println("属性值不合法，请输入0-15之间的数");
                    file.close();
                    return false;
                }
                item[5] = (byte) attribute;
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8 + 5);
                file.write(item[5]);
                file.close();
                return true;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return false;
    }
}
