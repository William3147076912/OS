import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskManage {
    public static void volume(File file)
    {
        if (file.exists()) {
            // 获取文件大小
            long fileSize = file.length();
            // 输出文件大小
            System.out.println("磁盘大小: " + fileSize + " 字节");
        } else {
            System.out.println("文件不存在");
        }
    }
    public static void init(int volume)
    {

        byte[] hello={48};
        try (
                FileOutputStream fos = new FileOutputStream("Disk")) {
            for (int i=0;i<volume;i++)
            {
                fos.write(hello);
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
