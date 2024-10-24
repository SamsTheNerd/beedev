package com.samsthenerd.beedev.language;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// a symbol used in comb language
public interface CombSym {

    // period separated
    String getNamespace();

    String getSpecifier();

    default String asString(){
        if(getNamespace().isEmpty()){
            return getSpecifier();
        }
        return getNamespace() + ":" + getSpecifier();
    }

    // format "some.name.space:id
    static CombSym of(String str){
        String[] spl = str.split(":");
        if(spl.length == 1){
            return new Basic("", spl[0]);
        }
        return new Basic(spl[0], spl[1]);
    }

    // just for neatness
    static CombSym of(String namespace, String specifier){
        return new Basic(namespace, specifier);
    }

    static CombSym arbitrary(){
        return new Basic("internal", UUID.randomUUID().toString());
    }

    record Basic(String namespace, String label) implements CombSym{
        public String getNamespace(){
            return namespace();
        }

        public String getSpecifier(){
            return label();
        }

        @Override
        public String toString(){
            return asString();
        }
    }
}
