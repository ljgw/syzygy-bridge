package com.winkelhagen.chess.syzygy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by laurens on 16-9-18 for frankwalter.
 * javac SyzygyBridge.java -h .
 *
 * g++ -std=c++14 -O2 -Wall -D TB_USE_ATOMIC -D TB_NO_HW_POP_COUNT -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -shared -o libJSyzygy.so tbprobe.c
 */
public class SyzygyBridge {

    private static final Logger LOG = LogManager.getLogger();
    private static boolean libLoaded = false;
    private static int tbLargest = 0;


    static {
        try {
            Path jarfile = Paths.get(SyzygyBridge.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File libFile = jarfile.getParent().resolve("libJSyzygy.so").toFile();
            LOG.info("looking for libJSyzygy.so at location {}", libFile);
            if (libFile.exists()) {
                System.load(libFile.getAbsolutePath());
                LOG.info("loaded libJSyzygy.so");
            } else {
                File classpathLibFile = Paths.get(SyzygyBridge.class.getClassLoader().getResource("libJSyzygy.so").toURI()).toFile();
                LOG.info("looking for libJSyzygy.so at location {}", classpathLibFile);
                if (classpathLibFile.exists()){
                    System.load(classpathLibFile.getAbsolutePath());
                    LOG.info("loaded libJSyzygy.so");
                } else {
                    LOG.info("looking for libJSyzygy.so at java.library.path: {}", System.getProperty("java.library.path"));
                    System.loadLibrary("JSyzygy");
                }
            }
            libLoaded = true;
        } catch (URISyntaxException | UnsatisfiedLinkError | RuntimeException e) {
            LOG.warn("unable to load JSyzygy library", e);
        }
    }

    /**
     *
     * @return true iff the JSyzygy JNI library is loaded.
     */
    public static boolean isLibLoaded(){
        return libLoaded;
    }

    /**
     * determine if Syzygy tablebases are available for the supplied number of pieces (including kings)
     * @param piecesLeft the number of pieces on the board
     * @return true if the JSyzygy JNI library is loaded and tablebases suitable for the supplied number of pieces are loaded.
     */
    public static boolean isAvailable(int piecesLeft){
        return libLoaded & piecesLeft <= tbLargest;
    }

    /**
     * Load the Syzygy tablebases (init in Fathom)
     * @param path the location of the tablebases
     * @return the supported size of the loaded tablebases
     */
    public synchronized static int load(String path){
        if (tbLargest>0){
            LOG.warn("Syzygy tablebases are already loaded");
            return tbLargest;
        }
        boolean result = init(path);

        if (result) {
            tbLargest = getTBLargest();
        } else {
            tbLargest = -1;
        }

        return tbLargest;
    }

    /**
     *
     * @return the supported size of the loaded tablebases
     */
    public static int getSupportedSize(){
        return tbLargest;
    }

    /**
     * Returns a result containing the Win/Draw/Loss characteristics of the position. Notes: assumes castling is no longer possible and that there is no 50 move rule.
     * @param white all white pieces (bitboard)
     * @param black all black pieces (bitboard)
     * @param kings all kings (bitboard)
     * @param queens all queens (bitboard)
     * @param rooks all rooks (bitboard)
     * @param bishops all bishops (bitboard)
     * @param knights all knights (bitboard)
     * @param pawns all pawns (bitboard)
     * @param ep the square where an En Passant capture can take place (or 0 if there is no En Passant)
     * @param turn true if white is to move, false if black is.
     * @return WDL result (see c code)
     */
    public static int probeSyzygyWDL(long white, long black, long kings, long queens, long rooks, long bishops, long knights, long pawns, int ep, boolean turn){
        return probeWDL(white, black, kings, queens, rooks, bishops, knights, pawns, ep, turn);
    }

    /**
     * Returns a result containing the distance to zero (and a move that will decrease the distance to zero). Note: assumes castling is no longer possible.
     * @param white all white pieces (bitboard)
     * @param black all black pieces (bitboard)
     * @param kings all kings (bitboard)
     * @param queens all queens (bitboard)
     * @param rooks all rooks (bitboard)
     * @param bishops all bishops (bitboard)
     * @param knights all knights (bitboard)
     * @param pawns all pawns (bitboard)
     * @param rule50 The 50-move half-move clock
     * @param ep the square where an En Passant capture can take place (or 0 if there is no En Passant)
     * @param turn true if white is to move, false if black is.
     * @return DTZ result (see c code)
     */
    public static int probeSyzygyDTZ(long white, long black, long kings, long queens, long rooks, long bishops, long knights, long pawns, int rule50, int ep, boolean turn){
        return probeDTZ(white, black, kings, queens, rooks, bishops, knights, pawns, rule50, ep, turn);
    }


    private static native boolean init(String path);
    private static native int getTBLargest();
    private static native int probeWDL(long white, long black, long kings, long queens, long rooks, long bishops, long knights, long pawns, int ep, boolean turn);
    private static native int probeDTZ(long white, long black, long kings, long queens, long rooks, long bishops, long knights, long pawns, int rule50, int ep, boolean turn);

}
