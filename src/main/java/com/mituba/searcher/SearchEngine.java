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


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
// import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class SearchEngine{
    private String kindOfBirthmark;
    private String filename;
    private String birthmark;
    private int portNum;
    private int coreNum;
    private Double threshold;
    private long AllSearchTime = 0;

    public SearchEngine(String kindOfBirthmark, int portNum, String filename, String birthmark, int coreNum, String threshold){
        this.kindOfBirthmark = kindOfBirthmark;
        this.portNum = portNum;
        this.filename = filename;
        this.birthmark = birthmark;
        this.coreNum = coreNum;
        this.threshold = Double.parseDouble(threshold);
    }

    public void searchAndCompare(){
        try{
            performSearch(initUrl(coreNum)).stream()
                .forEach(n -> n.performCompare());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void runOnlySearch(){
        try{
            performOnlySearch(initUrl(coreNum));
        }catch(Exception e){
            System.out.println(e + ":SearchEngine");
            // return new ArrayList<String[]>();
        }
    }

    public SolrQuery createQuery(String birthmark, String fl, String rows, String sort){
        SolrQuery query = new SolrQuery().setSort(sort, SolrQuery.ORDER.desc);
        query.set("q", birthmark);
        query.set("fl", fl);
        query.set("rows", rows);
        return query;
    }

    public void performOnlySearch(String url) throws IOException, UnsupportedEncodingException{
        try{
            SolrClient server = new HttpSolrClient.Builder("http://localhost:"+portNum+"/solr/birth_"+kindOfBirthmark+""+coreNum).build();
            SolrQuery query = createQuery(birthmark, "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data", "2000", "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)");
            // query.set("q", birthmark);
            // query.set("fl", "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data"); // 返すフィールドの指定
            // query.set("rows", "2000");
            // query.setSort("strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)",SolrQuery.ORDER.desc );
            QueryResponse response = server.query(query);
            SolrDocumentList list = response.getResults();
            for(SolrDocument doc : list)
                System.out.println(doc.get("filename") + "," + doc.get("lev") + "," + doc.get("data"));
        }catch(Exception e){
            System.out.println(e + ":solrj");
        }
        // return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines().parallel()
        //     .distinct()
        //     .map(i -> i.split(",",3))
        //     .filter(i -> i != null)
        //     .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev") && Double.parseDouble(i[1]) >= 0.0)
        //     .collect(Collectors.toList());
    }


    public Map<String, String> initMap() throws UnsupportedEncodingException{
        Map<String, String> map = new HashMap<>();
        map.put("q", URLEncoder.encode(birthmark, "UTF-8"));
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
        try{
            // SolrClient server = new HttpSolrClient.Builder("http://localhost:"+portNum+"/solr/birth_"+kindOfBirthmark+""+coreNum).build();
            // SolrQuery query = createQuery(birthmark, "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data", "2000", "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)");
            // QueryResponse response = server.query(query);
            // SolrDocumentList list = response.getResults();
            // List<CompareEngine> CompareEngineList = new ArrayList<>();
            // for(SolrDocument doc : list){
            //     if((float)doc.get("lev") <= threshold)
            //         break;
            //     CompareEngineList.add(new CompareEngine(filename, birthmark, doc.get("filename").toString(), doc.get("lev").toString(), doc.get("data").toString()));
            //     // System.out.println(doc.get("filename") + "," + doc.get("lev") + "," + doc.get("data"));
            // }
            // return CompareEngineList;
            //

//            String path;
//            if(coreNum == 0)
//                path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "/query";
//            else
//                path = "http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/query";
//        	HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
//        	try {
//
//        	    HttpPost request = new HttpPost(path);
//        	    StringEntity params =new StringEntity("{"
//        	    		+ "query:\""+URLEncoder.encode(birthmark, "UTF-8")+"\","
//        	    		+ "sort:\""+"strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit) desc"+"\","
//        	    		+ "limit:"+"2010"+","
//        	    		+ "fields:"+"[\"filename\",\"lev\":strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit),\"data\""+"]"
//        	    		+ "} ");
////        	    request.addHeader("content-type", "application/x-www-form-urlencoded");
//        	    request.setEntity(params);
//        	    HttpResponse response = httpClient.execute(request);
//        	    System.out.println("hello");
//        	    new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
//        	    	.lines().forEach(System.out::println);
//        	    return new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
//        	    	.lines()
//                    .distinct().parallel()
//                    .map(i -> i.split(",",3))
//                    .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev") && Double.parseDouble(i[1]) >= 0.5)
//                    .map(n -> new CompareEngine(filename, birthmark, n[0], n[1], n[2]))
//                    .collect(Collectors.toList());
//        		Process r = Runtime.getRuntime().exec("curl http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/query?fl=filename,data -d {"
        		Process r = Runtime.getRuntime().exec("curl http://localhost:"+portNum+"/solr/birth_" + kindOfBirthmark + "" + coreNum + "/query"
        				// + "?fl=filename,lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8")+"\",edit),data&q="+URLEncoder.encode(birthmark, "UTF-8")+"&sort="+"strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc"+" -d {"
        				+ "?fl=filename,lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8")+"\",edit),data&sort="+"strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc"+" -d {"
        				+ "params:{"
//        	    		+ "q:"+URLEncoder.encode(birthmark, "UTF-8")+","
        	    		+ "q:\""+birthmark.replace(" ", "+")+"\","
        	    		+ "rows:"+2010+","
        	    		+ "wt:\"csv\""
        	    		+ "}"
        	    		+ "}");

        		return new BufferedReader(new InputStreamReader(r.getInputStream())).lines()
        			.map(n -> n.split(",",3))
                    .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev") && Double.parseDouble(i[1]) >= 0.75)
                    .map(n -> new CompareEngine(filename, birthmark, n[0], n[1], n[2]))
                    .collect(Collectors.toList());

//        	}catch (Exception ex) {
//        		return null;
//        	    //handle exception here
//
//        	}
//        return new BufferedReader(new InputStreamReader(((HttpURLConnection) new URL(url).openConnection()).getInputStream())).lines()
//            .distinct().parallel()
//            .map(i -> i.split(",",3))
//            .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev") && Double.parseDouble(i[1]) >= 0.5)
//            .map(n -> new CompareEngine(filename, birthmark, n[0], n[1], n[2]))
//            .collect(Collectors.toList());
        }catch(Exception e){
            System.out.println(e + ":solrj");
            List<CompareEngine> tmpList = new ArrayList<>();
            return tmpList;
        }
    }
}
