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


import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;



class ArgumentsChecker{
    @Option(name="-i", metaVar="FILENAME", usage="Input birthmark csv filename")
    private String inputFileName;

    @Option(name="-o", metaVar="FILENAME", usage="Output compare result filename ")
    private String outputFileName;

    @Option(name="-b", metaVar="BIRTHMARK", usage="Input kind of birthmark(ex. 2-6gram, uc)")
    private String kindOfBirthmark;

    @Option(name="-p", metaVar="PORTNUM", usage="Input port number of search engine")
    private String portNum;

    @Option(name="-c", metaVar="MAXCORENUM", usage="Input max core number of search engine")
    private String maxCoreNum;

    @Option(name="-dir", metaVar="DIRECTORY", usage="Input directory with birthmark")
    private String directory;

    @Argument
    private static List<String> arguments = new ArrayList<>();

    @SuppressWarnings("deprecation")
	public Map<String, String> checkArguments(String[] args){
        CmdLineParser parser = new CmdLineParser(this);
        try{
            parser.parseArgument(args);
            if(inputFileName == null && outputFileName == null && kindOfBirthmark == null && portNum == null
                    && maxCoreNum == null)
                throw new CmdLineException(parser, "No argument is given");
        }
        catch(CmdLineException e){
            System.out.println(e);
            parser.printUsage(System.err);
            System.out.println();
        }
        return createMap(inputFileName, outputFileName, kindOfBirthmark, portNum);
    }

    public Map<String, String> createMap(String input, String output, String kindOfBirthmark, String portNum){
        Map<String, String> map = new HashMap<>();
        map.put("input", input);
        map.put("output", output);
        map.put("birthmark", kindOfBirthmark);
        map.put("port", portNum);
        map.put("core", maxCoreNum);
        map.put("dir", directory);
        return map;
    }
}
