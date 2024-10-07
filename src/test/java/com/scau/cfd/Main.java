package com.scau.cfd;

import org.junit.Test;

import javax.sound.midi.Soundbank;
import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static Disk disk;
    byte[] buffer1;
    byte[] buffer2;

    public static void main(String[] args) throws IOException {
        disk = new Disk(new File("Disk"));
//        System.out.printf(String.valueOf(disk.getVolume()));
//        disk.format();

        System.out.println("开机默认当前目录为主目录");
        Catalog man = new Catalog("/");
        man.parent = 2;
        man.location = 2;
        man.attribute = 0x0B;
        CatalogManage.currentCatalog = man;
        while (true) {
            boolean exit = false;
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] command = input.split(" ");
            if (Objects.equals(command[0], "exit")) break;
            switch (command[0]) {
                case "md":
                    if (command.length < 2) {
                        System.out.println("error");
                        break;
                    }
                    if (!CatalogManage.MakeDir(command[1]))
                        System.out.println("error");
                    break;
                case "dir":
                    CatalogManage.ShowDir();
                    break;
                case "rd":
                    if (command.length < 2) {
                        System.out.println("error");
                        break;
                    }
                    if (!CatalogManage.RemoveDir(command[1])) {
                        System.out.println("error");
                    }
                    break;
                case "cd":
                    if (command.length < 2) {
                        System.out.println("error");
                        break;
                    }
                    CatalogManage.ChangeDirectory(command[1]);
                    break;
                //以下是文件操作
                case "cf":
                    if(!FileManage.CreateFile(command[1],(byte) 0x04))
                    {
                        System.out.println("error");
                    }
                    break;
                case "of":

                    break;
                case "df":
                    if (command.length < 2) {
                        System.out.println("error");
                        break;
                    }
                    FileManage.DeleteFile(command[1]);
                    break;
                case "format":
                    disk.format();
                    break;
                case "exit":
                    exit = true;
                default:
                    System.out.println("unknown command");
            }
            if (exit) break;
            System.out.println(CatalogManage.absolutePath);
        }
    }

    @Test
    public void test() {
        System.out.println(new OFT().isFull());
    }

    @Test
    public void myTest() {

        new Disk(new File("Disk")).format();
    }
}