import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.*; 
import java.nio.file.Paths;

public class Reader {

    public List<Chunk> LoadTextToChunks() {

        List<Chunk> retChunks =  new ArrayList<Chunk>();
        Chunk chunk=new Chunk();
        int lineCounter=0;
        int cnkCounter=-1;
        try {
            File file = new File(Paths.get("").toAbsolutePath()+"\\BigText.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {               
                String data=reader.nextLine();
                    ++lineCounter;
                    AddLine(chunk.cnkText, data);
                    if (lineCounter==1000) {
                        chunk.cnkLength=Long.valueOf(chunk.cnkText.toString().length());
                        chunk.cnkNum=++cnkCounter;
                        if (chunk.cnkNum>0) {
                            chunk.cnkLength+=retChunks.get(chunk.cnkNum-1).cnkLength;
                        }
                        
                        retChunks.add(chunk);
                        lineCounter=0;
                        chunk=new Chunk();
                     }
            }
            retChunks.add(chunk);
            reader.close();
        } catch (final FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return retChunks;
    }

    private void AddLine(StringBuilder chunk, final String data) {
        chunk.append(data.toLowerCase());
        chunk.append("\n");
    } 
}