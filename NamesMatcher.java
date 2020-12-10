
import java.io.*;
import java.util.*;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; 

public class NamesMatcher {

    public static String GetConfigValue(String key) {
        Properties prop = new Properties();
        var fileName = Paths.get("").toAbsolutePath() + "\\app.config";
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {

        }
        try {
            prop.load(is);
        } catch (IOException ex) {

        }
        return prop.getProperty(key);
    }

    public static void main(final String[] args) {

        var reader = new Reader();
        var matcher = new Matcher();
        Integer cnkCounter=0;
        List<Chunk> lstChunks = new ArrayList<Chunk>();
        try {
            var names = GetConfigValue("NAMES");
            lstChunks = reader.LoadTextToChunks();
            ExecutorService executor = Executors.newCachedThreadPool();

            for (Chunk cnk : lstChunks) {
                Long prevCnkLength = CalculatePrecCnkLength(cnkCounter, lstChunks);
                executor.submit(() -> {
                    matcher.Match(cnk,prevCnkLength, names.toLowerCase());
                });
                ++cnkCounter;
            }

            PrintResults(names);
        } catch (Exception e) {
            
        }
        
    }

    private static Long CalculatePrecCnkLength(Integer cnkCounter, List<Chunk> lstChunks) {
        Long prevCnkLength;
        if (cnkCounter>0){
            prevCnkLength=lstChunks.get(cnkCounter-1).cnkLength;
        }
        else{
            prevCnkLength=0L;
        }
        return prevCnkLength;
    }

    private static void PrintResults(String names) {
        //It would be better to use here json serialization, not supported in java native
        for (var name : names.split(",")) {
            if(Aggregator.finalMapper.get(name)!=null)
            {
                System.out.println(name + "-->[" );
                PrintLocations(Aggregator.finalMapper.get(name));
                System.out.println("]" );
            }
        }
    }

    private static void PrintLocations(List<Cordinate> locations) {
        for (var loc : locations) {

            System.out.println("[lineOffset=" + loc.lineOffset + ",charOffset="+ loc.charOffset + "]");
        }
    }
}