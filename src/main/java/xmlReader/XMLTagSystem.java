package xmlReader;

import java.io.BufferedReader;
import java.io.IOException;

import xmlTags.XMLChildTag;
import xmlTags.XMLTag;

public class XMLTagSystem {
	BufferedReader br;
	int nCI=0;
	XMLTag firstTag=new XMLTag();
	XMLTag currentTag;
	String line;
	
	public XMLTagSystem(BufferedReader br) throws XMLException, IOException {
		this.br=br;
		currentTag=firstTag;
		readFirstLine();
	}
	
	public XMLTag getTagSystem() {
		return firstTag;
	}
	
	private void readFirstLine() throws XMLException,IOException {
		try {
			line=br.readLine();
		} catch (IOException e) {
			throw new IOException("failure with the buffered reader");
		}
		nCI=0;
		if(line.charAt(nCI)!='<') {
			throw new XMLException("failure in XML-structure");
		}else {
			useLine();
		}
	}
	
	private void readNewLine() throws IOException, XMLException {
		try {
			line=br.readLine();
		} catch (IOException e) {
			throw new IOException("failure with the buffered reader");
		}
		nCI=0;
		useLine();
	}
	
	private void useLine() throws IOException, XMLException {
		if(line!=null) {
			while(line.length()>nCI) {
				if(line.charAt(nCI)=='<') {
					tagInformation();
				}else {
					attributeInformation();
				}
			}
			readNewLine();
		}
	}
	
	private void attributeInformation() throws XMLException {
		String attribute=getStringUntil('<',false);
		if(currentTag.isChildTag()) {
			XMLChildTag ct=(XMLChildTag)currentTag;
			ct.addAttribute(attribute);
		}
	}
	
	private void tagInformation() throws XMLException{
		nCI++;
		if(line.charAt(nCI)=='/') {
			nCI++;
			getStringUntil('>',true);
			if(currentTag.isChildTag()) {
				XMLChildTag ct=(XMLChildTag)currentTag;
				ct.setClosed(true);
				currentTag=ct.getParentTag();
			}else {
				throw new XMLException("failure in XML-structure");
			}
		}else {
			String name=getStringUntil('>',true);
			XMLChildTag newTag=currentTag.addChildTag(name);
			currentTag=newTag;
		}
	}
	
	private String getStringUntil(char end,boolean exception)throws XMLException{
		String string="";
		if(line.length()>nCI) {
			char newChar=line.charAt(nCI);
			nCI++;
			if(newChar!=end) {
				string+=newChar;
				string+=getStringUntil(end,exception);
			}
		}else {
			if(exception) {
				throw new XMLException("failure in XML-structure");
			}
		}
		return string;
	}
}
