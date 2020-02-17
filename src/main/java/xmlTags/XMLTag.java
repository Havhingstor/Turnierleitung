package xmlTags;

import java.util.ArrayList;

public class XMLTag {
	private ArrayList<XMLChildTag>childTags=new ArrayList<XMLChildTag>();
	
	public XMLChildTag addChildTag(String name) {
		XMLChildTag childTag=new XMLChildTag(this,name);
		childTags.add(childTag);
		return childTag;
	}
	
	public ArrayList<XMLChildTag>getChildTags(){
		return childTags;
	}
	
	public boolean isChildTag() {
		return false;
	}
}
