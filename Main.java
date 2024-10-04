import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    byte[] buffer1;
    byte[] buffer2;
     public static void main(String[] args) {
//        Disk disk=new Disk(new File("Disk"));
//        System.out.printf(String.valueOf(disk.getVolume()));
//        disk.format();
         System.out.println("开机默认当前目录为主目录");
         CatalogManage.currentCatalog=new Catalog("/");
         System.out.println(Arrays.toString(CatalogManage.currentCatalog.name));
         System.out.println(CatalogManage.currentCatalog.name.length);
         Scanner scanner=new Scanner(System.in);
         String input =scanner.nextLine();
         System.out.println(input);
    }
}