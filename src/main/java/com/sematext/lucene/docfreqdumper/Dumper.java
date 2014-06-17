package com.sematext.lucene.docfreqdumper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class Dumper {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        String fieldName = args[1];
        Map<String, Integer> terms = new HashMap<>();

        BytesRef termBytes;

        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
        IndexSearcher searcher = new IndexSearcher(reader);
        List<AtomicReaderContext> leaves = reader.leaves();
        for (AtomicReaderContext context : leaves) {
            TermsEnum te = context.reader().terms(fieldName).iterator(null);
            while ((termBytes = te.next()) != null) {
                //terms.put(termBytes.utf8ToString(), te.docFreq());
                //System.out.println(termBytes.utf8ToString() + "\t" + te.docFreq());
                System.out.println(te.docFreq() + "\t" + termBytes.utf8ToString());
            }
        }
    }
}
