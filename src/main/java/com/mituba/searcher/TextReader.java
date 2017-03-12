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

    public TextReader(String[] args){
        this.filename = args[0];
        this.kindOfBirthmark = args[1];
        this.portNum = Integer.parseInt(args[2]);
    }

    public Stream<SearcherCollecter> readFile() throws FileNotFoundException{
        try{
            return new BufferedReader(new FileReader(filename)).lines()
                .map(i -> i.split(",", 4))
                .filter(i -> i.length >= 4)
                .map(n -> new SearcherCollecter(kindOfBirthmark, portNum, n[0], n[3]));
                // .collect(Collectors.toList());
        }catch(Exception e){
            System.out.println(e);
            return null;

        }
    }
}
