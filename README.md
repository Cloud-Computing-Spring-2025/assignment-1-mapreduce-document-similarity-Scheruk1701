[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18028172&assignment_repo_type=AssignmentRepo)

### **📌 Document Similarity Using Hadoop MapReduce**  

#### **Objective**  
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---

### **📥 Example Input**  

The input file `input.txt` contains multiple lines, each representing a document with its ID followed by its content.  

#### **Example Input (input.txt)**  
```
doc1 Hadoop is a distributed computing framework used for processing big data. It enables scalable and efficient data storage and analysis.
doc2 Hadoop is a widely used distributed computing system. It provides efficient big data processing and storage capabilities.
doc3 Big data is crucial for modern analytics. Hadoop and other distributed systems help in large-scale data processing and storage.
```

---

## 📏 Jaccard Similarity Calculation

The Jaccard Similarity between two sets is defined as the size of the intersection divided by the size of the union of the sets.

### **Formula**
```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```

Where:
- `|A ∩ B|` = Count of words present in both documents.
- `|A ∪ B|` = Count of distinct words across both documents.

### **Example Calculation**

For the following documents:

- **doc1**: `{hadoop, is, a, distributed, computing, framework, used, for, processing, big, data, it, enables, scalable, and, efficient, storage, analysis}`
- **doc2**: `{hadoop, is, a, widely, used, distributed, computing, system, it, provides, efficient, big, data, processing, and, storage, capabilities}`

#### **Step-by-Step Calculation**
- Intersection: `{hadoop, is, a, distributed, computing, used, big, data, it, efficient, processing, and, storage}` → 13
- Union: `{hadoop, is, a, distributed, computing, framework, used, for, processing, big, data, it, enables, scalable, and, efficient, storage, analysis, widely, system, provides, capabilities}` → 23

```
Jaccard Similarity = 13 / 23 ≈ 0.5652 or 56.52%
```

---

## 🛠 **Approach & Implementation**

### **MapReduce Workflow**

**1. Mapper:**
- Splits each document into words.
- Emits (word, document_id) pairs.

**2. Reducer:**
- Aggregates the document IDs for each word.
- Computes the Jaccard Similarity between document pairs.
- Outputs the pairs with a similarity score above the threshold.

## ⚙️ **Setup & Execution Steps**

### **Step 1: Start Docker & Hadoop**
```bash
docker compose up -d
```

### **Step 2: Build the JAR File**
```bash
mvn install
mv target/*.jar input/
```

### **Step 3: Copy Files to Hadoop Container**
```bash
docker cp input/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp input/input.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### **Step 4: Run the MapReduce Job**
```bash
docker exec -it resourcemanager /bin/bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
hadoop fs -mkdir -p /input/dataset
hadoop fs -put input.txt /input/dataset/
hadoop jar DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.DocumentSimilarityDriver /input/dataset/input.txt /output
```

### **Step 5: View and Retrieve the Output**
```bash
hadoop fs -cat /output/*
hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
exit
docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ ./output/
```

---

## 🚧 **Challenges Faced & Solutions**

1. **Docker Container Communication:**
    - Issue: Inconsistent communication between Hadoop services.
    - Solution: Restarted containers and ensured the correct network configurations.

2. **Memory Allocation Errors:**
    - Issue: Hadoop jobs failing due to memory limits.
    - Solution: Increased container memory allocation in `docker-compose.yml`.

3. **Jaccard Similarity Edge Cases:**
    - Issue: Handling documents with zero similarity.
    - Solution: Added checks to skip document pairs with empty intersections.

4. **File Permissions in HDFS:**
    - Issue: Permission denied when copying files.
    - Solution: Used `hdfs dfs -chmod` to set appropriate permissions.

---

## 🎯 **Conclusion**

This assignment demonstrates how **Hadoop MapReduce** can be utilized to compute document similarity using the **Jaccard Similarity** metric. The process involves efficient text processing across distributed nodes, showcasing the power of **big data frameworks** in real-world applications.

---


