


let outp = gorgeEx("nim c -d:release --app:lib --passL:-static --mm:atomicArc preload.nim")
echo outp.output
