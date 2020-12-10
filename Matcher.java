
import java.util.*;
/**
 * Matcher
 */
public class Matcher{

    public void Match(final Chunk chunk,final Long prevCnkLength ,final String names) {
        HashSet<String> wordsHash = new HashSet();  
        try {
            final var namesArr = Arrays.asList(names.split(","));
            var curChunk = NormelizeChunk(chunk.cnkText.toString());
            final var allWords = Arrays.asList(curChunk.split(" "));
            for (final var word : allWords) {
                
                 if (!wordsHash.contains(word.toLowerCase())) {
                    wordsHash.add(word.toLowerCase());
                }
            } 
    
            for (String name : namesArr) {
                //Classic for extention for "contains", not supported in java
                if (wordsHash.contains(name)||wordsHash.contains(name+",")|| wordsHash.contains(name+"'s")) {
                    ApplyResults(chunk,prevCnkLength,name);
                }
            }
        } catch (Exception e) {
            //TODO: 
        }
    }

    private String NormelizeChunk(String curChunk) {
        curChunk=curChunk.replace(","," ");
        curChunk=curChunk.replace("\n"," ");
        return curChunk;
    }

    private void ApplyResults(Chunk chunk,Long prevCnkLength,String name) {
        Integer nextIndex=0;
        String searchText=chunk.cnkText.toString().toLowerCase();
        while (searchText.indexOf(name,nextIndex)>0) {
            AddCordinate(chunk,prevCnkLength, name,nextIndex);
            nextIndex=chunk.cnkText.indexOf(name,nextIndex)+1;   
        }
    }

    private void AddCordinate(Chunk chunk,Long prevCnkLength, String name,Integer nextIndex) {
        List<Cordinate> matches=new ArrayList<Cordinate>();
        Cordinate cor=new Cordinate();
        //Char offset inside the block 
        cor.charOffset= Long.valueOf(chunk.cnkText.indexOf(name,nextIndex))+prevCnkLength;
        cor.lineOffset=CalculateLine(chunk,name,chunk.cnkText.indexOf(name,nextIndex))+chunk.cnkNum*1000;
        matches.add(cor);
        //lock
        synchronized (this) {
            name=name.substring(0, 1).toUpperCase()+ name.substring(1);
            if(Aggregator.finalMapper.get(name)==null){
                Aggregator.finalMapper.put(name, matches);
            }
            else
            {
                Aggregator.finalMapper.get(name).addAll(matches);
            }
        }
    }

    private Integer CalculateLine(Chunk chunk, String name, Integer charOffset) {
        String curBlock=chunk.cnkText.substring(0,charOffset);
        return curBlock.split("\\n").length;
    }
}

