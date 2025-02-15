package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {
    
    private final List<String> docIds = new ArrayList<>();
    private final List<HashSet<String>> wordCollections = new ArrayList<>();

    @Override
    protected void reduce(Text documentId, Iterable<Text> wordListIterable, Context context) 
            throws IOException, InterruptedException {
        
        for (Text wordList : wordListIterable) {
            HashSet<String> words = new HashSet<>(Arrays.asList(wordList.toString().split(",")));
            docIds.add(documentId.toString());
            wordCollections.add(words);
        }
    }

    /**
     * Computes the Jaccard similarity between two sets of words.
     *
     * @param set1 First set of words.
     * @param set2 Second set of words.
     * @return The Jaccard similarity coefficient.
     */
    private double computeJaccard(HashSet<String> set1, HashSet<String> set2) {
        HashSet<String> common = new HashSet<>(set1);
        common.retainAll(set2);

        HashSet<String> total = new HashSet<>(set1);
        total.addAll(set2);

        return total.isEmpty() ? 0.0 : (double) common.size() / total.size();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        int totalDocuments = docIds.size();
        // Compare every unique pair of documents
        for (int i = 0; i < totalDocuments; i++) {
            for (int j = i + 1; j < totalDocuments; j++) {
                double similarity = computeJaccard(wordCollections.get(i), wordCollections.get(j));
                // Output only if similarity exceeds 50%
                if (similarity > 0.5) {
                    String pairKey = "(" + docIds.get(i) + ", " + docIds.get(j) + ")";
                    String similarityStr = String.format("%.2f%%", similarity * 100);
                    context.write(new Text(pairKey), new Text(similarityStr));
                }
            }
        }
    }
}
