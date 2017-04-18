package com.mituba.searcher;

import com.mituba.searcher.SearchEngine;
import com.mituba.searcher.SearcherCollecter;
import com.mituba.searcher.CompareEngine;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class TextReader{
    private String filename;
    private String kindOfBirthmark;
    private int portNum;
    private int maxCoreNum;
    private String threshold;

    public TextReader(String inputFileName, String kindOfBirthmark, String portNum, String maxCoreNum, String threshold){
        this.filename = inputFileName;
        this.kindOfBirthmark = kindOfBirthmark;
        this.portNum = Integer.parseInt(portNum);
        this.maxCoreNum = Integer.parseInt(maxCoreNum);
        this.threshold = threshold;
    }

    public TextReader(String inputFileName){
        this.filename = inputFileName;
    }
    public Stream<String[]> readFile() throws FileNotFoundException{
        return new BufferedReader(new FileReader(filename)).lines()
            .map(i -> i.split(",", 4))
            .filter(i -> i.length >= 4);
    }

    public Stream<SearcherCollecter> createSearcherCollecter(){
        try{
            // return new BufferedReader(new FileReader(filename)).lines()
            //     .map(i -> i.split(",", 4))
            //     .filter(i -> i.length >= 4)
            return readFile()
                .map(n -> new SearcherCollecter(kindOfBirthmark, portNum, maxCoreNum, n[0], n[3], threshold));
                // .collect(Collectors.toList());
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}
