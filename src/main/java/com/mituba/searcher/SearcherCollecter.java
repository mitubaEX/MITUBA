package com.mituba.searcher;

import com.mituba.searcher.SearchEngine;
import com.mituba.searcher.TextReader;
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

import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

class SearcherCollecter{
    private String kindOfBirthmark;
    private String filename;
    private String birthmark;
    private int portNum;

    public SearcherCollecter(String kindOfBirthmark, int portNum, String filename, String birthmark){
        this.kindOfBirthmark = kindOfBirthmark;
        this.portNum = portNum;
        this.filename = filename;
        this.birthmark = birthmark;
    }

    public Stream<SearchEngine> collectSearcher(){
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("2gram", 69);
        map.put("3gram", 544);
        map.put("4gram", 499);
        map.put("5gram", 471);
        map.put("6gram", 805);
        map.put("uc", 516);
        Stream.Builder<SearchEngine> builder = Stream.builder();
        for(int i = 0; i <= map.get(kindOfBirthmark); i++)
            builder.add(new SearchEngine(kindOfBirthmark, portNum, filename, birthmark, i));

        // Stream<SearchEngine> stream = builder.build();
        return builder.build();
        // return IntStream.rangeClosed(0, 1000)
        //     .boxed()
        //     .mapToObj(new SearchEngine(kindOfBirthmark, portNum, filename, birthmark));
    }
}
