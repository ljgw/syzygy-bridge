package com.winkelhagen.chess.syzygy;
import org.junit.BeforeClass;

import java.io.Console;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by laurens on 21-11-18 for syzygy-bridge.
 */
public class SyzygyBridgeTest {

    private static int loadResult;

    @org.junit.BeforeClass
    public static void setup(){
        URL testSyzygyDirectory = SyzygyBridgeTest.class.getClassLoader().getResource("syzygy");
        String testSyzygyPath = testSyzygyDirectory.getPath().replaceAll("%20", " ");
        loadResult = SyzygyBridge.load(testSyzygyPath);
    }

    @org.junit.Test
    public void isLibLoaded() {
        assertTrue("JSyzygy library should have been loaded", SyzygyBridge.isLibLoaded());
    }

    @org.junit.Test
    public void isAvailable() {
        assertTrue("JSyzygy should be available for 3 pieces endgames", SyzygyBridge.isAvailable(3));
        assertFalse("JSyzygy should not be available for 4 pieces endgames", SyzygyBridge.isAvailable(4));
    }

    @org.junit.Test
    public void load() {
        assertEquals("expected tablebase should match actual tablebase", 3, loadResult);

    }

    @org.junit.Test
    public void getSupportedSize() {
        assertEquals("supported size for the tablebase is expected to be 3", 3, loadResult);

    }

    @org.junit.Test
    public void probeSyzygyWDL() {
        assertEquals("KRvK should be a win in this position", SyzygyConstants.TB_WIN, SyzygyBridge.probeSyzygyWDL(3L, 4096L, 4097L, 0L, 2L, 0L, 0L, 0L, 0, true ));

    }

    @org.junit.Test
    public void probeSyzygyDTZ() {
        int result = SyzygyBridge.probeSyzygyDTZ(3L, 4096L, 4097L, 0L, 2L, 0L, 0L, 0L, 0, 0, true );
        assertEquals("KRvK should win in this position", 19924244, result);
        assertEquals("KRvK DTZ result should indicate a rook move in this position", 1, SyzygyConstants.fromSquare(result));
        assertEquals("KRvK DTZ result should indicate a rook move to the third rank in this position", 17, SyzygyConstants.toSquare(result));
        assertEquals("KRvK DTZ Result should indicate no promotion", 0, SyzygyConstants.promoteInto(result));
        assertEquals("KRvK DTZ Result should indicate 19 ply", 19, SyzygyConstants.distanceToZero(result));
        assertEquals("KRvK DTZ Result should indicate a win in this position", SyzygyConstants.TB_WIN, SyzygyConstants.winDrawLoss(result));

    }

}