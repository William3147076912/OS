import javax.management.openmbean.InvalidOpenTypeException;
import javax.sound.midi.Soundbank;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CatalogManage {
    public static Catalog currentCatalog;

    public static boolean MakeDir(String dirName) throws IOException {
        boolean finded=false;
        byte[] item=new byte[8];
        RandomAccessFile file=new RandomAccessFile(Main.disk.file,"rw");
        file.seek(currentCatalog.location);
        for (int i=0;i<8;i++)
        {
            file.seek(currentCatalog.location+i*8);
            file.read(item,0,8);
            if(item[0]!='$')
            {
                finded=true;
//                新建子目录并且改变文件分配表
                Catalog son=new Catalog(dirName);
                son.parent= currentCatalog.location;
                son.location= Main.disk.findEmpty();
                file.seek(son.location);
                file.write(255);
                son.attribute=0x08;

//               填写目录项
                file.seek(currentCatalog.location*64+i*8);
                file.write(son.name);
                file.write(' ');
                file.write(' ');
                file.write(son.attribute);
                file.write(son.location);
                file.write(son.parent);

//                将新建子目录下内容初始化为空
                for (int j=0;j<8;j++)
                {
                    file.seek(son.location*64+j*8);
                    file.write('$');

                }
                break;
            }

        }
        return finded;
    }
    public static boolean ShowDir() throws IOException {
        byte[] item=new byte[3];
        RandomAccessFile file=new RandomAccessFile(Main.disk.file,"r");
        for (int i=0;i<8;i++)
        {
            file.seek(currentCatalog.location*64+i*8);
            file.read(item,0,3);
            if (item[0]!='$')
            {
                System.out.println( new String(item, StandardCharsets.US_ASCII));
            }
        }
        return true;
    }
    public static boolean RemoveDir(String dirName) throws IOException {
        byte[] item=new byte[3];
        byte[] location=new byte[1];
        RandomAccessFile file=new RandomAccessFile(Main.disk.file,"rw");
        for (int i=0;i<8;i++)
        {
            file.seek(currentCatalog.location*64+i*8);
            file.read(item,0,3);
            if(dirName.equals(new String(item,StandardCharsets.US_ASCII)))
            {
                file.seek(currentCatalog.location*64+i*8+6);
                file.read(location);
                
            }

        }
        return true;
    }
}
