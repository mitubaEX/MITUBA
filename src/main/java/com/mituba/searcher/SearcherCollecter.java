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
    private int maxCoreNum;

    public SearcherCollecter(String kindOfBirthmark, int portNum, int maxCoreNum, String filename, String birthmark){
        this.kindOfBirthmark = kindOfBirthmark;
        this.portNum = portNum;
        this.maxCoreNum = maxCoreNum;
        this.filename = filename;
        this.birthmark = birthmark;
    }

    public Stream<SearchEngine> collectSearcher(){
        Stream.Builder<SearchEngine> builder = Stream.builder();
        for(int i = 0; i <= maxCoreNum; i++)
            builder.add(new SearchEngine(kindOfBirthmark, portNum, filename, birthmark, i));
        return builder.build();
    }
}
