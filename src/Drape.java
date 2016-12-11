import java.io.*;

/**
 * Created by Cai on 2016/12/11.
 */
public class Drape {

    private static final int VERTEX_COUNT = 0;
    private static final int FACE_COUNT = 1;
    private static final int EDGE_COUNT = 2;
    private float[] mVertices;

    private int[] mIndices;

    private int[] mMeshInfo;

    public void readOff(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        try {
            String line = reader.readLine();
            if(line != null && line.equalsIgnoreCase("OFF")){
                line = reader.readLine();
                String[] info = line.split(" ");
                mMeshInfo = new int[info.length];
                for(int i = 0; i < info.length; i++){
                    mMeshInfo[i] = Integer.parseInt(info[i]);
                }
                mVertices = new float[mMeshInfo[VERTEX_COUNT] * 3];
                mIndices = new int[mMeshInfo[FACE_COUNT] * 3];
                int i = 0, j = 0;
                while((line = reader.readLine()) != null){
                    String[] ele = line.split(" ");
                    if(ele.length == 3){
                        mVertices[i++] = Float.parseFloat(ele[0]);
                        mVertices[i++] = Float.parseFloat(ele[1]);
                        mVertices[i++] = Float.parseFloat(ele[2]);
                    }
                    else if(ele.length == 4){
                        mIndices[j++] = Integer.parseInt(ele[1]);
                        mIndices[j++] = Integer.parseInt(ele[2]);
                        mIndices[j++] = Integer.parseInt(ele[3]);
                    }
                }
            }
            else{
                System.err.println("File Format error!");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    /* defaut triangle mesh */
    public native void importHumanMesh(float[] vertices, int[] indices);

    public native void modifyHumanMesh();

    public void storeOFF(String fileName){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        }catch(IOException ex){
            ex.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("OFF\n");
        sb.append(mMeshInfo[VERTEX_COUNT] + " " + mMeshInfo[FACE_COUNT] + " " + mMeshInfo[EDGE_COUNT] + "\n");
        for(int i = 0; i < mMeshInfo[VERTEX_COUNT]; i++){
            sb.append(mVertices[3*i] + " " + mVertices[3*i+1] + " " + mVertices[3*i+2] + "\n");
        }
        for(int i = 0; i < mMeshInfo[FACE_COUNT]; i++){
            sb.append("3 " + mIndices[3*i] + " " + mIndices[3*i+1] + " " + mIndices[3*i+2] + "\n");
        }
        try {
            writer.write(sb.toString());
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){
        Drape drape = new Drape();
        drape.readOff("cloth.off");
        drape.importHumanMesh(drape.mVertices, drape.mIndices);
        drape.modifyHumanMesh();
        drape.storeOFF("clothret.off");
    }
}
