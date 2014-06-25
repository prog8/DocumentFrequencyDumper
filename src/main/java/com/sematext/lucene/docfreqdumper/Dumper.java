package com.sematext.lucene.docfreqdumper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
                String termString = termBytes.utf8ToString();
                if (terms.containsKey(termString)) {
                    terms.put(termString, terms.get(termString) + te.docFreq());
                } else {
                    terms.put(termString, te.docFreq());
                }
            }
        }

        for (Map.Entry<String, Integer> entry : terms.entrySet()) {
            System.out.println(entry.getValue() + "\t" + entry.getKey());
        }
    }
}
