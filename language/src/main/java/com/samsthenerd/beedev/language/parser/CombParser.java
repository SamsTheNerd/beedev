package com.samsthenerd.beedev.language.parser;

import com.samsthenerd.beedev.language.core.FExpr;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.text.ParseException;

public class CombParser {
    private static StreamTokenizer makeTokenizer(Reader reader){
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        return tokenizer;
    }

    private static String parseProgram(StreamTokenizer tokenizer){

    }


    @FunctionalInterface
    public interface ParseSort<T>{
        T parse(StreamTokenizer tokenizer) throws ParseException;
    }
}
