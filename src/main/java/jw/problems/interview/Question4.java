package jw.problems.interview;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created these interview questions for screening candidates
 * https://www.interviewzen.com/dashboard/8NWv2B
 *
 * You are given a dictionary of words and a (possibly infinite) buffered
 * stream of characters e.g. from a TCP socket.
 *
 * Your job is to write any matched words to a buffered writer. Characters
 * can be reused between words. You may assume that all characters are
 * lowercase alphabet i.e. 'a' < char < 'z'. If multiple matches are found
 * simultaneously, the order of output is not important.
 *
 * For example, given:
 * dictionary = {at, bat, atm, man, batman, mans}
 * Input               Output
 * ..........x
 * .........xa
 * ........xat         at
 * .......xaty
 * <skip>
 * ..xatypqrsb
 * .xatypqrsba
 * xatypqrsbat         bat, at
 * atypqrsbatm         atm
 * typqrsbatma
 * ypqrsbatman         batman, man
 * pqrsbatmans         mans
 * qrsbatmansw
 *
 * You may assume the following limits:
 * len(dict):  0 <= len(dict) < 1024
 * len(word):  0 < len(word) < 256
 *
 * Memory Limit: 256MB
 *
 * The method should be compilable on OpenJDK 1.8.0_92 without
 * any additional libraries.
 *
 * You are allowed to create additional methods to structure your code.
 * You may use the below method template.
 *
 * You are advised not to spend more than 60 minutes on this question.
 *
 * public void writeMatches(Set<String> dictionary, BufferedReader reader, BufferedWriter writer) throws IOException {
 * char c;
 * while ((c = (char) reader.read()) > 0) {
 * // WRITE YOUR CODE HERE
 * }
 * writer.flush();
 * }
 */
public class Question4 {

    public static void main(String[] args) throws Exception {
        Set<String> dictionary = new HashSet<>();
//    String in = "acacabcatghhellomvnsdb";
//    dictionary.add("aca");
//    dictionary.add("cat");
//    dictionary.add("hello");
//    dictionary.add("world");

        String in = "xatypqrsbatmansw";
        dictionary.add("at");
        dictionary.add("bat");
        dictionary.add("atm");
        dictionary.add("man");
        dictionary.add("batman");
        dictionary.add("mans");

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8))));
        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);
        emitMatches(dictionary, reader, writer);
        System.out.println(sw.getBuffer());
    }

    public static void emitMatches(Set<String> dictionary, BufferedReader reader, BufferedWriter writer) throws IOException {
        TrieNode root = TrieNode.buildTrie(dictionary);
        Queue<TrieNodeWithAccumulator> partialMatches = new LinkedList<>();

        int in;
        while ((in = reader.read()) > 0) {
            char c = (char) in;

            partialMatches.offer(new TrieNodeWithAccumulator(root, new StringBuilder()));

            boolean toBreak = false;
            while (!toBreak) {
                TrieNodeWithAccumulator partialMatch = partialMatches.poll();
                if (partialMatch.isEmpty())
                    toBreak = true;

                TrieNode node = partialMatch.getNode();
                if (node.isWord()) {
                    writer.write(partialMatch.getAccumulatorValue());
                    writer.write(System.lineSeparator());
                    writer.flush();
                }
                TrieNode childNode = node.getChildNode(c);
                if (childNode != null) {
                    partialMatch.setNode(childNode);
                    partialMatch.addChar(c);
                    partialMatches.offer(partialMatch);
                }
            }
        }
        writer.flush();
    }

    public static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        private boolean isWord = false;

        public static TrieNode buildTrie(Set<String> dictionary) {
            TrieNode root = new TrieNode();
            for (String word : dictionary)
                addWord(root, word);
            return root;
        }

        private static void addWord(TrieNode node, String word) {
            if (word == null)
                return;
            if (word.length() <= 0) {
                node.isWord = true;
                return;
            }

            char c = word.charAt(0);
            TrieNode childNode;
            if (!node.children.containsKey(c)) {
                childNode = new TrieNode();
                node.children.put(c, childNode);
            } else {
                childNode = node.children.get(c);
            }
            addWord(childNode, word.substring(1));
        }

        public TrieNode getChildNode(char c) {
            return children.get(c);
        }

        public boolean isWord() {
            return isWord;
        }
    }

    public static class TrieNodeWithAccumulator {
        private TrieNode node;
        private StringBuilder accumulator;

        public TrieNodeWithAccumulator(TrieNode node, StringBuilder accumulator) {
            this.node = node;
            this.accumulator = accumulator;
        }

        public TrieNode getNode() {
            return node;
        }

        public void setNode(TrieNode node) {
            this.node = node;
        }

        public boolean isEmpty() {
            return accumulator.length() <= 0;
        }

        public void addChar(char c) {
            accumulator.append(c);
        }

        public String getAccumulatorValue() {
            return accumulator.toString();
        }
    }

}
