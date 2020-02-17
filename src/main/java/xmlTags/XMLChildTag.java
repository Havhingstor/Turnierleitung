package xmlTags;

import java.util.ArrayList;

public class XMLChildTag extends XMLTag {
	private String name;
	private XMLTag parentTag;
	private boolean closed=false;
	private ArrayList<String>attributes=new ArrayList<String>();
	
	public XMLChildTag(XMLTag newParentTag,String name) {
		this.parentTag=newParentTag;
		this.name=name;
	}
	
	public void setClosed(boolean closed) {
		this.closed=closed;
	}
	
	public boolean getClosed() {
		return closed;
	}
	
	@Override
	public boolean isChildTag() {
		return true;
	}
	
	public XMLTag getParentTag() {
		return parentTag;
	}
	
	public void addAttribute(String attribute) {
		attributes.add(attribute);
	}
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}
	
	public String getName() {
		return name;
	}
}
