package com.mituba.searcher;

import com.mituba.searcher.TextReader;
import com.mituba.searcher.SearcherCollecter;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

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
    //     try{
    //         return initUrl(coreNum).stream()
    //             .map(n -> n.performCompare())
    //             .map(n -> n.split(","));
    //     }catch(Exception e){
    //         System.out.println(e);
    //         return null;
    //     }
    // }
    public List<String[]> run2(){
        try{
            String a = initUrl2(coreNum);
            // long start = System.currentTimeMillis();
            return performSearch2(a);
            // long end = System.currentTimeMillis();
            // AllSearchTime += (end - start);
            // return AllSearchTime;
            // System.out.println("searchTime:"+AllSearchTime+"ms");
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<String[]>();
        }
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


    // public List<CompareEngine> initUrl(int coreNum) throws UnsupportedEncodingException, IOException{
    //     String path;
    //     if(coreNum == 0)
    //         path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "/select";
    //     else
    //         path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/select";
    //     StringJoiner url = new StringJoiner("&", path + "?", "");
    //     initMap().forEach((key, value) -> url.add(key + "=" + value));
    //     return performSearch(url.toString());
    // }

    public String initUrl2(int coreNum) throws UnsupportedEncodingException, IOException{
        String path;
        if(coreNum == 0)
            path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "/select";
        else
            path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/select";
        StringJoiner url = new StringJoiner("&", path + "?", "");
        initMap().forEach((key, value) -> url.add(key + "=" + value));
        return url.toString();
    }

    // public List<CompareEngine> performSearch(String url) throws IOException, UnsupportedEncodingException{
    //     return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines().parallel()
    //         .distinct().parallel()
    //         .map(i -> i.split(",",3))
    //         .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev"))
    //         .map(n -> new CompareEngine(filename, birthmark, n[0], n[1], n[2]))
    //         .collect(Collectors.toList());
    // }
    public List<String[]> performSearch2(String url) throws IOException, UnsupportedEncodingException{
        return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines().parallel()
            .distinct().parallel()
            .map(i -> i.split(",",3))
            .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev"))
            .collect(Collectors.toList());
    }
}
