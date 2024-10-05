import jdk.jfr.Unsigned;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CompletionService;

public class Main {
    byte[] buffer1;
    byte[] buffer2;
    public static Disk disk;

    public static void main(String[] args) throws IOException {
         disk=new Disk(new File("Disk"));
//        System.out.printf(String.valueOf(disk.getVolume()));
//        disk.format();

         System.out.println("开机默认当前目录为主目录");
         Catalog man=new Catalog("/");
         man.parent=2;
         man.location=2;
         man.attribute=0x0B;
         CatalogManage.currentCatalog=man;
        while (true) {
            boolean exit=false;
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
                    if(! CatalogManage.MakeDir(command[1]))
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
                    if(!CatalogManage.RemoveDir(command[1]))
                    {
                        System.out.println("error");
                    }
                    break;
                case "format":
                    disk.format();
                    break;
                case "exit":
                    exit=true;
                default:
                    System.out.println("unknown command");
            }
            if (exit)break;
        }


    }
}