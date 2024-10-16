package com.scau.cfd;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * 主类，负责初始化磁盘和处理用户命令。
 */
public class Main {
    public static Disk disk;
    byte[] buffer1;
    byte[] buffer2;

    /**
     * 主方法，程序入口。
     *
     * @param args 命令行参数
     * @throws IOException 如果发生 I/O 错误
     */
    public static void main(String[] args) throws IOException {
        // 初始化磁盘
        disk = new Disk(new File("Disk"));
        // 打印磁盘容量
        // System.out.printf(String.valueOf(disk.getVolume()));
        // 格式化磁盘
        // disk.format();

        // 开机默认当前目录为主目录
        System.out.println("开机默认当前目录为主目录");
        Catalog man = new Catalog("/");
        man.parent = 2;
        man.location = 2;
        man.attribute = 0x0B;
        CatalogManage.currentCatalog = man;

        // 进入命令循环
        while (true) {
            boolean exit = false;
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] command = input.split(" ");

            // 处理退出命令
            if (Objects.equals(command[0], "exit")) break;

            // 命令处理
            switch (command[0]) {
                case "md":
                    if (command.length < 2) {
                        System.out.println("错误：缺少目录名");
                        break;
                    }
                    if (!CatalogManage.MakeDir(command[1])) {
                        System.out.println("错误：创建目录失败");
                    }
                    break;
                case "dir":
                    CatalogManage.ShowDir();
                    break;
                case "rd":
                    if (command.length < 2) {
                        System.out.println("错误：缺少目录名");
                        break;
                    }
                    if (!CatalogManage.RemoveDir(command[1])) {
                        System.out.println("错误：删除目录失败");
                    }
                    break;
                case "cd":
                    if (command.length < 2) {
                        System.out.println("错误：缺少目录名");
                        break;
                    }
                    CatalogManage.ChangeDirectory(command[1]);
                    break;
                // 文件操作
                case "cf":
                    if (command.length < 2) {
                        System.out.println("错误：缺少文件名");
                        break;
                    }
                    if (!FileManage.CreateFile(command[1], (byte) 0x04)) {
                        System.out.println("错误：创建文件失败");
                    }
                    break;
                case "of":
                    // 打开文件的命令，暂未实现
                    break;
                case "df":
                    if (command.length < 2) {
                        System.out.println("错误：缺少文件名");
                        break;
                    }
                    if (!FileManage.DeleteFile(command[1])) {
                        System.out.println("错误：删除文件失败");
                    }
                    break;
                case "format":
                    disk.format();
                    break;
                case "exit":
                    exit = true;
                default:
                    System.out.println("未知命令");
            }

            if (exit) break;

            // 打印当前目录路径
            System.out.println(CatalogManage.absolutePath);
        }
    }
}
