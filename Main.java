import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DiskManage.init(64*128);
        File file=new File("Disk");
        DiskManage.volume(file);
    }
}