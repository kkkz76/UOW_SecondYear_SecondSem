import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;

public class solution1 {
    public static void main(String[] args) throws Exception {

        String hdfsStr1 = args[0];
        String hdfsStr2 = args[1];

        Configuration conf = new Configuration();
        FileSystem hdfs1 = FileSystem.get(URI.create(hdfsStr1), conf);
        FileSystem hdfs2 = FileSystem.get(URI.create(hdfsStr2), conf);

        Path hdfsFile1 = new Path(hdfsStr1);
        Path hdfsFile2 = new Path(hdfsStr2);

        FSDataInputStream in = hdfs1.open(hdfsFile1);
        FSDataOutputStream out = hdfs2.create(hdfsFile2);

        IOUtils.copyBytes(in, out, 4096, true);
        hdfs1.delete(hdfsFile1);

	System.out.println("File has been deleted");

    }
}
