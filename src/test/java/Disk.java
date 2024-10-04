import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Disk {
    private long volume;
    private File file;

    public Disk(File file) {
        if (file.exists()) {
            this.file=file;
            this.volume= file.length();
        } else {
            System.out.println("文件不存在");
        }
    }

    public long getVolume() {
        return volume;
    }
    public void format() {
        try {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw");

            file.seek(0);
            byte[] buffer = new byte[128];
            file.write(buffer);
            file.seek(128);

            for (int i=0;i<9;i++)
            {
                file.write('$');
                file.seek(128+i*8);
            }
            file.seek(0);
            file.write(new byte[]{(byte) 255, (byte) 255, (byte) 255});
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setVolume(String filename, int volume) {

        byte[] hello = {48};
        try (
                FileOutputStream fos = new FileOutputStream("Disk")) {
            for (int i = 0; i < volume; i++) {
                fos.write(hello);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
