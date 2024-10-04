import java.nio.charset.StandardCharsets;

public class Catalog {
        byte[] name;
        byte attribute;
        byte location;
        Catalog[] subCatalog;
        public Catalog(String name)
        {
            this.name=new byte[3];
            byte[] theName=name.getBytes(StandardCharsets.US_ASCII);
            for (int i=0;i<3&&i<theName.length;i++)
            {
                this.name[i]=theName[i];
            }
        }

}
