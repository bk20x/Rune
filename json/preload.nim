import tables, json, more_sugar, strutils, macros


type
    CustomType = object
      name,accessor,filePath: string

var registeredTypes {.compileTime.}: seq[CustomType] 


func newCustomType(name, accessor, filePath : string): --> CustomType =
  CustomType(name : name, accessor : accessor, filePath : filePath)

proc jsonToTable(jsonData, accessor : string): Table[string,JsonNode] =
  result = initTable[string,JsonNode]()
  data <- parseJson jsonData
  data.elems.each elem:
    accessTag <- unescape ($elem[accessor])
    result[accessTag] = elem

  
macro initData() =
  result = newStmtList()
  const types = staticRead("types.txt").split("\r\n")
  
  types.each line:
    let
       split = line.split(',')
       name = split[0] -> strip
       accessor = split[1] -> strip
       file = split[2] -> strip
    registeredTypes.add newCustomType(name,accessor,file)

    if registeredTypes.len == 1:
      echo "Ok, 1 type registered"
    else:
      echo "Ok, ", registeredTypes.len, " types registered"

    let 
      fileData = genSym nskConst 
      table = newIdentNode name
      queryProc = newIdentNode "query" & name
    result.add quote do:
      const `fileData` = staticRead `file`
      let `table` = jsonToTable(`fileData`, `accessor`)
      proc `queryProc`(elementName : cstring): cstring {.cdecl, exportc, dynlib.} = #Exported for so, dll
        return cstring($(`table`[$(elementName)]))

initData()
proc NimMain() {.cdecl, importc.}
proc libraryInit() {.exportc, dynlib, cdecl.} =
  NimMain()