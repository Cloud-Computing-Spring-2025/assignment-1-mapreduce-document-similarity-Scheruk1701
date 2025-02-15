package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DocumentSimilarityDriver {
    public static void main(String[] args) throws Exception {
        // Verify that exactly two arguments (input and output directories) are provided
        if (args.length != 2) {
            System.err.println("Usage: DocumentSimilarityDriver <input directory> <output directory>");
            System.exit(1);
        }

        // Create a new Hadoop configuration and job instance
        Configuration config = new Configuration();
        Job similarityJob = Job.getInstance(config, "Document Similarity Analysis");

        // Set the jar, mapper, and reducer classes
        similarityJob.setJarByClass(DocumentSimilarityDriver.class);
        similarityJob.setMapperClass(DocumentSimilarityMapper.class);
        similarityJob.setReducerClass(DocumentSimilarityReducer.class);

        // Define the output types for keys and values
        similarityJob.setOutputKeyClass(Text.class);
        similarityJob.setOutputValueClass(Text.class);

        // Specify the paths for input and output
        FileInputFormat.addInputPath(similarityJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(similarityJob, new Path(args[1]));

        // Execute the job and exit depending on the outcome
        System.exit(similarityJob.waitForCompletion(true) ? 0 : 1);
    }
}
