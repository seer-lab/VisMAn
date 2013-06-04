package temp;

import java.util.Vector;


/**
 * This class is used to hold extra data in the xml file
 * 
 * @author Jeff Falkenham
 *
 */
public class XmlStorage {
	private Vector<String> xmlTag = new Vector<String>();
	private Vector<String> xmlData = new Vector<String>();
	
	/**
	 * Constructor, does nothing.
	 * 
	 */
	XmlStorage(){

	}
	
	/**
	 * Adds a tag and the data contained in the tag to the object
	 * 
	 * @param tag		The xml tag
	 * @param data		The data contained between the tags
	 */
	public void setXml(String tag, String data){
		xmlTag.add(tag);
		xmlData.add(data);
	}
	
	/**
	 * Get method for the size of the object
	 * 
	 * @return		returns the size of the object
	 */
	public int getSize(){
		return xmlTag.size();
	}
	
	/**
	 * get method for the xml tag contained at the specified index
	 * 
	 * @param index		the index of the tag the user wants
	 * @return			returns the tag at the specified index
	 */
	public String getXmlTag(int index){
		return xmlTag.get(index);
	}
	
	
	/**
	 * Get method for the data contained at the specified index.
	 * 
	 * @param index		the index of the data the user wants
	 * @return			returns the data contained at the specified index
	 */
	public String getXmlData(int index){
		return xmlData.get(index);
	}
}
