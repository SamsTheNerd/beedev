package com.samsthenerd.beedev.language;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// a symbol used in comb language
public interface CombSym {

    List<String> getNamespace();

    // format "some.name.space:id
    static CombSym of(String str){
        String[] spl = str.split(":");
        if(spl.length == 1){
            return new Basic(new ArrayList<>(), str);
        }
        return new Basic(List.of(spl[0].split("\\.")), spl[1]);
    }

    static CombSym arbitrary(){
        return new Basic(List.of("internal"), UUID.randomUUID().toString());
    }

    record Basic(List<String> namespace, String label) implements CombSym{
        public List<String> getNamespace(){
            return new ArrayList<>();
        }

        @Override
        public String toString(){
            StringBuilder strB = new StringBuilder();
            for(int i = 0; i < namespace.size(); i++){
                strB.append(namespace.get(i));
                if(i < namespace.size() -1) strB.append(".");
            }
            if(!strB.isEmpty()) strB.append(':');
            strB.append(label);
            return strB.toString();
        }
    }
}
