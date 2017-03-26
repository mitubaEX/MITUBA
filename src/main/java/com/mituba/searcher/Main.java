package com.mituba.searcher;

import com.mituba.searcher.SearchEngine;
import com.mituba.searcher.TextReader;
import com.mituba.searcher.SearcherCollecter;
import com.mituba.searcher.CompareEngine;

import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

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

class Main{
    private List<Double> thresholdList = new ArrayList<>();
    private List<Long> comparisonTimeList = new ArrayList<>();
    private int sim1 = 0;
    private int sim2 = 0;
    private int sim3 = 0;
    private int sim4 = 0;
    private int sim5 = 0;
    private int sim6 = 0;
    private int sim7 = 0;
    private int sim8 = 0;
    private int sim9 = 0;
    private int sim10 = 0;
    private long allTime = 0;

    public Main(String[] args){
        try{
            Map<String, String> optionMap = new ArgumentsChecker().checkArguments(args);
            if(Objects.equals(args[0], "search"))
                searchMain(optionMap.get("input"), optionMap.get("birthmark"), optionMap.get("port"), optionMap.get("core"));
            else if(Objects.equals(args[0], "compare"))
                compareMain(optionMap.get("input"), optionMap.get("birthmark"), optionMap.get("port"), optionMap.get("core"));
            else if(Objects.equals(args[0], "eachCompare"))
                eachCompare(optionMap.get("input"), optionMap.get("dir"), optionMap.get("birthmark"));
        }catch(Exception e){
            System.out.println(e + ":main");
        }
    }

    // searchOnly
    public void searchMain(String input, String kindOfBirthmark, String port, String core){
        try{
            new TextReader(input, kindOfBirthmark, port, core).createSearcherCollecter()
                .forEach(n -> onlySearch(n.collectSearcher()));
        }catch(Exception e){}
    }

    public void onlySearch(Stream<SearchEngine> stream){
        long start = System.currentTimeMillis();
        stream.forEach(n -> simCheck(n.runOnlySearch()));
        long end = System.currentTimeMillis();
        allTime += (end - start);
        System.out.println(allTime + "ms");
        // System.out.println(sim10 + " "
        //         + " " + sim9
        //         + " " + sim8
        //         + " " + sim7
        //         + " " + sim6
        //         + " " + sim5
        //         + " " + sim4
        //         + " " + sim3
        //         + " " + sim2
        //         + " " + sim1
        //         );
        // sim10 = 0;
        // sim9 = 0;
        // sim8 = 0;
        // sim7 = 0;
        // sim6 = 0;
        // sim5 = 0;
        // sim4 = 0;
        // sim3 = 0;
        // sim2 = 0;
        // sim1 = 0;
    }

    public void simCheck(List<String[]> sim){
        sim.stream()
            .forEach(n -> System.out.println(n[0] + "," + n[1] + "," + n[2]));
        // sim.stream()
        //     .map(n -> Double.parseDouble(n[1]))
        //     .forEach(i -> {
        //         if(1.0 >= i && i > 0.9)
        //             sim10++;
        //         else if(0.9 >= i && i > 0.8)
        //             sim9++;
        //         else if(0.8 >= i && i > 0.7)
        //             sim8++;
        //         else if(0.7 >= i && i > 0.6)
        //             sim7++;
        //         else if(0.6 >= i && i > 0.5)
        //             sim6++;
        //         else if(0.5 >= i && i > 0.4)
        //             sim5++;
        //         else if(0.4 >= i && i > 0.3)
        //             sim4++;
        //         else if(0.3 >= i && i > 0.2)
        //             sim3++;
        //         else if(0.2 >= i && i > 0.1)
        //             sim2++;
        //         else if(0.1 >= i && i >= 0.0)
        //             sim1++;
        //     });
    }

    // searchAndCompare
    public void compareMain(String input, String kindOfBirthmark, String port, String core) throws FileNotFoundException{
        try{
            new TextReader(input, kindOfBirthmark, port, core).createSearcherCollecter()
                .forEach(n -> searchAndCompare(n.collectSearcher()));
        }catch(Exception e){}
    }
    public void searchAndCompare(Stream<SearchEngine> stream){
        stream.forEach(n -> n.searchAndCompare());
    }

    // eachcompare
    public void eachCompare(String input, String dir, String kindOfBirthmark) throws FileNotFoundException{
        List<String[]> stream1 = new TextReader(input).readFile().collect(Collectors.toList());
        Arrays.stream(new File(dir).listFiles())
            .filter(i -> i.getName().contains(kindOfBirthmark))
            .forEach(n -> {
                try{
                    compareStream(stream1, new TextReader(n.getAbsolutePath()).readFile().collect(Collectors.toList()));
                }catch(Exception e){System.out.println(e + ":eachCompare");}
            });
    }

    public void compareStream(List<String[]> a, List<String[]> b) throws FileNotFoundException{
        a.stream().forEach(n -> compareStringStream(n, b));
    }

    public void compareStringStream(String[] a, List<String[]> b){
        b.stream().forEach(i -> new CompareEngine(a[0], a[3], i[0], "0.0", i[3]).performCompare());
    }


    public static void main(String[] args){
        new Main(args);
        System.out.println("exit!");
    }
}
