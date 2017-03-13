package com.mituba.searcher;

import com.mituba.searcher.TextReader;
import com.mituba.searcher.SearcherCollecter;
import com.mituba.searcher.CompareEngine;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class SearchEngine{
    private String kindOfBirthmark;
    private String filename;
    private String birthmark;
    private int portNum;
    private int coreNum;
    private long AllSearchTime = 0;

    public SearchEngine(String kindOfBirthmark, int portNum, String filename, String birthmark, int coreNum){
        this.kindOfBirthmark = kindOfBirthmark;
        this.portNum = portNum;
        this.filename = filename;
        this.birthmark = birthmark;
        this.coreNum = coreNum;
    }

    // public Stream<String[]> run(){
    public void run(){
        try{
            performSearch(initUrl(coreNum)).stream()
                .forEach(n -> n.performCompare());
                // .map(n -> n.split(","));
        }catch(Exception e){
            System.out.println(e);
            // return null;
        }
    }
    public List<String[]> runOnlySearch(){
        try{
            // long start = System.currentTimeMillis();
            // System.out.println("hello");
            return performOnlySearch(initUrl(coreNum));
            // long end = System.currentTimeMillis();
            // AllSearchTime += (end - start);
            // return AllSearchTime;
            // System.out.println("searchTime:"+AllSearchTime+"ms");
        }catch(Exception e){
            System.out.println(e + ":SearchEngine");
            return new ArrayList<String[]>();
        }
    }
    public List<String[]> performOnlySearch(String url) throws IOException, UnsupportedEncodingException{
        return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines().parallel()
            .distinct()
            .map(i -> i.split(",",3))
            .filter(i -> i != null)
            .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev"))
            .collect(Collectors.toList());
    }


    public Map<String, String> initMap() throws UnsupportedEncodingException{
        Map<String, String> map = new HashMap<>();
        map.put("q", "data:"+URLEncoder.encode(birthmark, "UTF-8"));
        map.put("sort", "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc");
        map.put("rows", "2010");
        map.put("fl", "filename,lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit),data");
        map.put("wt", "csv");
        return map;
    }


    public String initUrl(int coreNum) throws UnsupportedEncodingException, IOException{
        String path;
        if(coreNum == 0)
            path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "/select";
        else
            path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/select";
        StringJoiner url = new StringJoiner("&", path + "?", "");
        initMap().forEach((key, value) -> url.add(key + "=" + value));
        return url.toString();
    }


    public List<CompareEngine> performSearch(String url) throws IOException, UnsupportedEncodingException{
        return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines()
            .distinct().parallel()
            .map(i -> i.split(",",3))
            .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev") && Double.parseDouble(i[1]) >= 0.25)
            .map(n -> new CompareEngine(filename, birthmark, n[0], n[1], n[2]))
            .collect(Collectors.toList());
    }
}
