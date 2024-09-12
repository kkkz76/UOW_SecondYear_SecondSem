import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class solution2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "State RainFall");
        job.setJarByClass(solution2.class);
        job.setMapperClass(solution2Mapper.class);
        job.setReducerClass(solution2Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class solution2Mapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text state = new Text();
        private IntWritable rainFall = new IntWritable(1);

        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            try {
                String data = value.toString();
                String[] tempDataArray = data.split("\\t");
                state.set(tempDataArray[0]);
                rainFall.set(Integer.parseInt(tempDataArray[2]));
                context.write(state, rainFall);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class solution2Reducer extends Reducer<Text, IntWritable, Text, Text> {

        private String totalRainFall = null;
        private String maxRainFall = null;
        private String minRainFall = null;
        private String result = null;

        private Text output1 = new Text();
        private Text output2 = new Text();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int totalVal = 0;
            int maxVal = Integer.MIN_VALUE;
            int minVal = Integer.MAX_VALUE;

            for (IntWritable val : values) {
                totalVal += val.get();

                if (val.get() > maxVal) {
                    maxVal = val.get();
                }
                if (val.get() < minVal) {
                    minVal = val.get();
                }
            }

            StringBuilder outputString = new StringBuilder();
            outputString.append(Integer.toString(totalVal));
            outputString.append("   ");
            outputString.append(Integer.toString(maxVal));
            outputString.append("   ");
            outputString.append(Integer.toString(minVal));
            outputString.append("   ");
            output1.set(String.format("%-19s", key));
            totalRainFall = String.format("%4s", totalVal);
            maxRainFall = String.format("%4s", maxVal);
            minRainFall = String.format("%4s", minVal);
            result = totalRainFall + maxRainFall + minRainFall;
            output2.set(result);
            context.write(output1, output2);

        }
    }
}
