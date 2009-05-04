package edu.indiana.cs.docsim;

public class LSASim {

    // C/C++ function declarations
    //
    // bool LoadProjMatrix(char* filename);
    // bool LoadVocabulary(char * filename);
    // double LSAsimilarity(const char * fstr1, const char * fstr2);
    // double COSsimilarity(const char * fstr1, const char * fstr2);

    //Native method declaration
    public native double LSAsimilarity(String fstr1, String fstr2);
    public native double COSsimilarity(String fstr1, String fstr2);
    public native boolean loadProjMatrix(String filename);
    public native boolean loadVocabulary(String filename);

    private boolean loadAlready = false;

    public LSASim() {
        loadData();
    }

    public void loadData() {
        if (!loadAlready) {
            System.out.println("loading data.....");
            // System.out.println(System.getProperty("java.library.path"));
            loadProjMatrix();
            loadVocabulary();
            loadAlready = true;
            System.out.println("loading data ends");
        }
    }

    public boolean loadVocabulary() {
        return loadVocabulary(vocabularyFileName);
    }

    public boolean loadProjMatrix() {
        return loadProjMatrix(matrixFileName);
    }

    private static String libName = "lsaquery";
    private static String matrixFileName =
        "/u/jiayzhu/wmProj/Data/shared_data/ProjMatrix.dat";

    private static String vocabularyFileName =
        "/u/jiayzhu/wmProj/Data/shared_data/SharedVocab.txt";

    //Load the library
    static {
        System.loadLibrary(libName);
    }

    // public static void main(String args[]) {
    //     LSASim sim = new LSASim();
    //     double v = sim.LSAsimilarity("a", "b");
    //     System.out.print(v);
    // }
}

