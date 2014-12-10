package at.jku.ssw.cmm.preprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.jku.ssw.cmm.DebugShell;
import at.jku.ssw.cmm.DebugShell.Area;
import at.jku.ssw.cmm.DebugShell.State;
import at.jku.ssw.cmm.gui.exception.IncludeNotFoundException;
import at.jku.ssw.cmm.gui.file.FileManagerCode;

public class Preprocessor {

	public static String expand( String sourceCode, String workingDirectory, List<Object[]> codeRegister, List<Integer> breakpoints ) throws IncludeNotFoundException{
		// Debug message
		DebugShell.out(State.LOG, Area.COMPILER, "Starting preprocessor");
	
		// Init new SourceCode storage
		String newSourceCode = "";
		
		// Reset code register list
		codeRegister.clear();
		
		// Reset breakpoints list
		breakpoints.clear();
		
		Map<String, Integer> defines = new HashMap<>();
		
		newSourceCode = parseFile(sourceCode, workingDirectory, codeRegister, breakpoints, defines, 0, "main");

		/*for(Object[] codePart : codeRegister) {
			System.out.println(codePart[0] + "|" + codePart[1] + "|" + codePart[2]);
			
		}*/
		
		return newSourceCode;
	}
	
	public static String parseFile( String sourceCode, String workingDirectory, List<Object[]> codeRegister, List<Integer> breakpoints, Map<String, Integer> defines, int offset, String file) throws IncludeNotFoundException{
		// Debug message
		DebugShell.out(State.LOG, Area.COMPILER, "parse File");
		
		// Init new SourceCode storage
		String newSourceCode = "";
		
		// is parser inside comment?
		boolean insideComment = false;
    	
		// TODO check if code inside string
		//boolean insideString = false;
		
		List<Boolean> ifConditions = new ArrayList();
		
		int lastCodeRegisterInsert = offset;
		
		// parse code
		int line = offset;		
		for( String s : sourceCode.split("\n") ){
			line ++;
			String commentString = null;
			
			boolean parseAllCode = true;
			
			// if one of the preprocessor if is not true, parsing of all code is not possible
			for(int i=0; i< ifConditions.size(); i++) {
				if(ifConditions.get(i) == false)
					parseAllCode = false;
			}
			
			if(s.contains("//")) {
				commentString = s.substring(s.indexOf("//"));
				s = s.substring(0, s.indexOf("//"));
			}
			
			// check if code is inside comment
			if(insideComment) {
				// check if comment end in this line
				if(s.contains("*/")) {
					insideComment = false;
					s += s.substring(0, s.indexOf("*/"));
					s = s.substring(0, s.indexOf("*/")+2);
				} else {
					if(parseAllCode == false)
						newSourceCode += addString("// ign " + s,commentString);
					else
						newSourceCode += addString(s,commentString);
					continue;
				}
			}

			// parse multiline comments
			while(s.contains("/*")) {
				if(s.contains("*/")) {
					String newS = s.substring(0, s.indexOf("/*"));
					newS += s.substring(s.indexOf("*/")+2);
					s = newS;
				} else {
					insideComment = true;
					if(commentString != null)
						commentString = s.substring(s.indexOf("/*")) + commentString;
					else
						commentString = s.substring(s.indexOf("/*")) ;
					s = s.substring(0, s.indexOf("/*"));
				}
			}
			
			if(s.matches("^\\s*#.*$")) {
				String preString = s.substring(s.indexOf("#")+1);
				s = "//## pre-command: " + s;
				
				if(preString.isEmpty()) {
					// TODO 
					// not known preprocessor command
				} else if(preString.matches("^\\s*(ifdef|ifndef)\\s.*$")) {
					Matcher m = Pattern.compile("^\\s*ifdef\\s*(\\w+)\\s*$").matcher(preString);
					if(m.matches()) {
						if(defines.containsKey(m.group(1)) && defines.get(m.group(1)) != 0)
							ifConditions.add(true);
						else
							ifConditions.add(false);
					} else {	
						m = Pattern.compile("^\\s*ifndef\\s*(\\w+)\\s*$").matcher(preString);
						if(m.matches()) {
							if(defines.containsKey(m.group(1)) && defines.get(m.group(1)) != 0 )
								ifConditions.add(false);
							else
								ifConditions.add(true);
						} else {
							// TODO incorrect rule
						}
					}
				} else if(preString.matches("^\\s*else\\s*$")) {
					if(ifConditions.size() != 0) {
						// toggle state of condition
						// TODO detect multible else statements
						ifConditions.set(ifConditions.size()-1, !ifConditions.get(ifConditions.size()-1).booleanValue());
					}
				} else if(preString.matches("^\\s*endif\\s*$")) {
					// remove last element
					if(ifConditions.size() != 0) {
						ifConditions.remove(ifConditions.size()-1);
					} else {
						// TODO to much endif
					}
				} else if(parseAllCode == false) {
					// ignore other possible preprocessor commands
				} else if(preString.matches("^\\s*define\\s.*$")) {
					Matcher m = Pattern.compile("^\\s*define\\s*(\\w+)\\s*(\\w*)\\s*$").matcher(preString);
					if(m.matches()) {
						int value = 1;
						
						try {
						// parse value
						if(m.group(2) != "")
							value = Integer.parseInt(m.group(2));
						}
						catch ( java.lang.NumberFormatException e) {
							// TODO cannot parse value
						}
						
						defines.put(m.group(1), value);
					} else {
						// TODO incorrect rule
					}
				} else if(preString.matches("^\\s*undef\\s.*$")) {
					Matcher m = Pattern.compile("^\\s*undef\\s*(\\w+)\\s*$").matcher(preString);
					if(m.matches()) {
						defines.remove(m.group(1));
					} else {
						// TODO incorrect rule
					}
				} else if(preString.matches("^\\s*include.*$")) {
					String path = null;
					boolean localDirectory = true;
					if(preString.matches("^\\s*include\\s*<.*>\\s*$")) {
						// clib include
						Matcher m = Pattern.compile("^\\s*include\\s*<(.*)>\\s*$").matcher(preString);
						if(m.matches()) {
							path = m.group(1);
							//System.out.println("clib include: " + path);
						}
					} else if(preString.matches("^\\s*include\\s*\".*\"\\s*$")) {
						localDirectory = false;
						// normal include
						Matcher m = Pattern.compile("^\\s*include\\s*\"(.*)\"\\s*$").matcher(preString);
						if(m.matches()) {
							path = m.group(1);
							//System.out.println("normal include: " + path);
						}
					}
					
					if(path != null) {
						String includeCode;
						if( localDirectory )
							path = "clib/" + path;
					    else
					    	path = workingDirectory + "/" + path;
						
						includeCode = FileManagerCode.readSourceCode(new File(path));
						
						if( includeCode != null ){
							// add include tag
							newSourceCode += addString(s,commentString);
							
							// parse file
							String newSourceCodeHelp = parseFile(includeCode, workingDirectory, codeRegister, breakpoints, defines, line, path);
							
							// copy source-code
							newSourceCode += newSourceCodeHelp;
							
							Object[] newCodeInsert = {lastCodeRegisterInsert+1, line, file};
					    	codeRegister.add(newCodeInsert);
					    	
							// calculate new line
							line += countLines(newSourceCodeHelp);
					    	
					    	lastCodeRegisterInsert = line;
						}
					    else{
					    	// TODO, better exception
					    	DebugShell.out(State.WARNING, Area.COMPILER, "File not found: " + path);
					    	throw new IncludeNotFoundException(path, line);
					    }
						continue;
					} else {
						// TODO 
						// incorrect include
					}
				} else if(preString.matches("^\\s*(pause|wait)\\s*$")) {
					// TODO 
					// add breakpoint
					breakpoints.add(line);
				} else {
					// TODO 
					// not known preprocessor command
				}
			}
			
			if(parseAllCode == false) {
				newSourceCode += addString("// ign " + s,commentString);
				continue;
			}
			
			newSourceCode += addString(s,commentString);
		}

		if(!ifConditions.isEmpty()) {
			// TODO not all preprocessor if-conditions have ended
		}
		
		Object[] newCodeInsert = {lastCodeRegisterInsert+1, line, file};
    	codeRegister.add(newCodeInsert);
    	
		return newSourceCode;
	}
	
	public static String addString(String s, String commentString) {
		if(commentString != null) 
			return s + commentString + "\n";
		else
			return s + "\n";
	}

	// http://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string
	public static int countLines(String str){
	   String[] lines = str.split("\r\n|\r|\n");
	   return  lines.length;
	}

	public static Object[] returnFileAndNumber(int codeLine, List<Object[]> codeRegister) {
		// init vars
		Object[] returnObj = {"unknow", 0};
		
		String objName = null;
		int objLine = 0;

		// get name of codepart
		for(Object[] curObj : codeRegister) {
			if(codeLine >= Integer.parseInt(curObj[0].toString()) && codeLine <= Integer.parseInt(curObj[1].toString())) {
				objName = curObj[2].toString();
			}
		}
		
		// calculate linenumber
		if(objName != null) {
			for(Object[] curObj : codeRegister) {
				if(curObj[2].toString() == objName) {
					if(codeLine > Integer.parseInt(curObj[1].toString())) {
						objLine += Integer.parseInt(curObj[1].toString()) - Integer.parseInt(curObj[0].toString()) + 1;
					} else if(codeLine >= Integer.parseInt(curObj[0].toString()) && codeLine <= Integer.parseInt(curObj[1].toString())) {
						objLine += codeLine - Integer.parseInt(curObj[0].toString()) + 1;
					}
				}
			}

			returnObj = new Object[] {objName, objLine};
		}
		
		return returnObj;
	}
}
