import java.io.IOException;

import javax.naming.Context;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class solution3 {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Word Length Count");
        job.setJarByClass(solution3.class);
        job.setMapperClass(solution3Mapper.class);
        job.setReducerClass(solution3Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class solution3Mapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private final static IntWritable zero = new IntWritable(0);
        private Text wordObject = new Text();
        private String wordLengthName = null;

        private String[] wordLengthArray = { "1X short:", "2short:", "3medium:", "4long:", "5X long:", "6XX long:" };

        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String word = value.toString().trim();
           

                if (word.length() >= 1 && word.length() <= 3) {
                    wordLengthName = wordLengthArray[0];

                } else if (word.length() >= 4 && word.length() <= 5) {
                    wordLengthName = wordLengthArray[1];

                } else if (word.length() >= 6 && word.length() <= 8) {
                    wordLengthName = wordLengthArray[2];

                } else if (word.length() >= 9 && word.length() <= 12) {
                    wordLengthName = wordLengthArray[3];

                } else if (word.length() >= 13 && word.length() <= 15) {
                    wordLengthName = wordLengthArray[4];

                } else if (word.length() >= 16) {
                    wordLengthName = wordLengthArray[5];

                } 

		if (word.length() > 0) {
		
		 	wordObject.set(wordLengthName);
                	context.write(wordObject, one);
		}
               

            
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // Add zero counts for the unused length names
            for (String lengthName : wordLengthArray) {
                wordObject.set(lengthName);
                context.write(wordObject, zero);
            }
        }
    }

    public static class solution3Reducer extends Reducer<Text, IntWritable, Text, Text> {
        private Text wordCountText = new Text();
        private Text wordCountNameText = new Text();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int wordCount = 0;
            for (IntWritable value : values) {
                wordCount += value.get();
            }

         
            String wordCountString = wordCount + " words";
            wordCountText.set(String.format("%-12s", wordCountString));
            String lengthNameCut = key.toString().substring(1);
            wordCountNameText.set(String.format("%-12s", lengthNameCut));
          
            context.write(wordCountNameText, wordCountText);
        }
    }

}
