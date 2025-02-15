package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {
    
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Trim the line and locate the first whitespace character to separate the identifier from content
        String inputLine = value.toString().trim();
        int separatorIndex = inputLine.indexOf(" ");
        if (separatorIndex < 0) {
            return;
        }
        
        
        String documentIdentifier = inputLine.substring(0, separatorIndex);
        String documentContent = inputLine.substring(separatorIndex).trim();

        
        HashSet<String> uniqueWordSet = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(documentContent);
        while (tokenizer.hasMoreTokens()) {
            uniqueWordSet.add(tokenizer.nextToken().toLowerCase());
        }

        context.write(new Text(documentIdentifier), new Text(String.join(",", uniqueWordSet)));
    }
}
