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

/**
 * 文件管理类，提供文件操作的相关功能，如创建、打开、读取、写入、关闭和删除文件。
 */
public class FileManage {

    /**
     * 已打开文件列表。
     */
    public static ArrayList<OpenedFile> openedFileArrayList = new ArrayList<>();

    /**
     * 获取已打开文件列表的长度。
     *
     * @return 已打开文件列表的长度
     */
    public static int getOpenedFileListLength() {
        return openedFileArrayList.size();
    }

    /**
     * 判断文件是否已打开。
     *
     * @param currentFileWithPath 文件的完整路径
     * @return 如果文件已打开返回 true，否则返回 false
     */
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

    /**
     * 打开文件。
     *
     * @param filename 文件名
     * @param type     打开类型（读或写）
     * @return 打开的文件对象，如果文件未找到返回 null
     * @throws IOException 如果发生 I/O 错误
     */
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
        OpenedFile file;
        if ((file = FileManage.OpenFile(filename, (byte) 'r')) == null) {
            System.out.println("open file error");
            return false;
        }
        if (file.length < length) {
            System.out.println("the file is smaller than you require");
            return false;
        }

        return true;
    }

    /**
     * 写入文件。
     *
     * @param filename 文件名
     * @param length   需要写入的长度
     * @return 如果写入成功返回 true，否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean WriteFile(String filename, int length) throws IOException {
        OpenedFile file;
        if ((file = FileManage.OpenFile(filename, (byte) 'r')) == null) {
            System.out.println("open file error");
            return false;
        }
        if (file.length < length) {
            System.out.println("the file is smaller than you require");
            return false;
        }

        return true;
    }

    /**
     * 关闭文件。
     *
     * @param filename 文件名
     * @return 如果关闭成功返回 true，否则返回 false
     */
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

    /**
     * 删除文件。
     *
     * @param filename 文件名
     * @return 如果删除成功返回 true，否则返回 false
     * @throws IOException 如果发生 I/O 错误
     */
    public static boolean DeleteFile(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];

        // 查找文件
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if((item[5]&0x04)!=0x04)
                {
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
                file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
                file.write('$');
                int location = item[6];
                byte blockNum;
                do {
                    file.seek(location);
                    blockNum = file.readByte();
                    file.seek(location);
                    file.write(0);
                    location = blockNum;
                } while (location >= 0);
                file.close();
                return true;
            }
        }
        file.close();
        System.out.println("could not find the file in current catalog");
        return false;
    }

    public static boolean TypeFile(String filename) throws IOException{
        RandomAccessFile file = new RandomAccessFile(Main.disk.file, "r");
        byte[] item = new byte[8];
        for (int i = 0; i < 8; i++) {
            file.seek(CatalogManage.currentCatalog.location * 64 + i * 8);
            file.read(item, 0, 8);
            //首先判断当前目录下是否有该文件
            if (filename.equals(new String(Arrays.copyOfRange(item, 0, 3), StandardCharsets.US_ASCII))) {
                if((item[5]&0x04)!=0x04)
                {
                    System.out.println("failed, it's not a file");
                    file.close();
                    return false;
                }
                //然后判断已打开文件列表中是否有已有该文件
//                String currentFile = CatalogManage.absolutePath + filename;
//                if (isOpened(currentFile)) {
//                    file.close();
//                    return false;
//                }
                byte[] block=new byte[64];
                int location = item[6];
                byte blockNum;
                StringBuilder content= new StringBuilder();
                do {
                    file.seek(location);
                    blockNum = file.readByte();

                    file.seek(64 * location);
                    file.read(block);
                    content.append(new String(block, StandardCharsets.US_ASCII));

                    location = blockNum;
                } while (location >= 0);
                System.out.println(content);
                file.close();
                return true;
            }
        }

        file.close();
        System.out.println("could not find the file in current catalog");
        return false;
    }



    /**
     * 修改文件。
     *
     * @param filename 文件名
     * @return 如果修改成功返回 true，否则返回 false
     */
    public static boolean ChangeFile(String filename) {
        return true;
    }
}
