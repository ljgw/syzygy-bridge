[![Build Status](https://travis-ci.org/ljgw/syzygy-bridge.svg?branch=master)](https://travis-ci.org/ljgw/syzygy-bridge)
### syzygy-bridge

The SyzygyBridge project aims to provide a JNI bridge for a java chess engine to access the Excellent Syzygy Tablebases developed by Ronald de Man.

### included Windows and Linux libraries
The syzygy-bridge project is accompanied by two compiled libraries of a fork of Fathom project (https://github.com/ljgw/Fathom)
The Fathom project:
* It was created by basil00
* It was based on the work of Ronald de Man
* My fork is based on the fork of jdart1

For more information, please refer to https://github.com/ljgw/Fathom

### Use in a java program

This jar file can be included in any java chess engine and takes care of the loading the JSyzygy library.
The JSyzygy library should be placed in the java library path (or, on linux, it can be placed next to the chess-engine jar).
All functionality in the jar file is located in the SyzygyBridge class in the form of six static functions.
In addition to the `probeSyzygyWDL` and `probeSyzygyWDL` functions there are some support functions: 
* `isLibLoaded` (determine if the libJSyzygy.so library was succesfully loaded)
* `load` (loads the tablebases)
* `isAvailable` (determine if the tablebases for the specified size are loaded)
* `getSupportedSize` (returns TB_LARGEST from tbprobe.h)

The SyzygyConstants class contains the various constants normally exposed by the tbprobe.h header file.

The chess-engine FrankWalter makes use of the syzygy-bridge. See: https://github.com/ljgw/frankwalter