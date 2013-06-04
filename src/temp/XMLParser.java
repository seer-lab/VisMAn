package temp;



//import MutationData;
//import MutationVector;

import javax.swing.JFileChooser;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



/**
 * This class parses an xml file
 * 
 * @author Jeff Falkenham
 *
 */
public class XMLParser extends DefaultHandler{
	
	MutationVector<MutationData> prog = new MutationVector<MutationData>(true);
	StringBuffer textBuffer;
	private String tempVal;
	private int index = 0;
	private int testIndex = 0;
	private boolean currentTest = false;
	private boolean originalProgram = false;
	private String fileName = "";
	

	/**
	 * This constructor is used to add a reference to the object being used for
	 * holding the data and visualizing it
	 * 
	 * @param pDataContruct		The object that needs to hold the data
	 */
	XMLParser(MutationVector<MutationData> pDataContruct){
		prog = pDataContruct;
	}
	

	
	
	
	
	/**
	 * Finds and parses the xml file
	 * 
	 * 
	 */
	public void parseDocument(){
		SAXParserFactory parsefact = SAXParserFactory.newInstance();
		try {
		
			//creates a new SAXParser
			SAXParser pars = parsefact.newSAXParser();
			
			//Allows the user to choose an xml file
			JFileChooser openfile = new JFileChooser();
			int result = openfile.showOpenDialog(null);
			
			//parses the file
			if (result == JFileChooser.APPROVE_OPTION)
			{
				pars.parse(openfile.getSelectedFile(), this);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes)throws SAXException  {
		
		//gets attributes that are contained in the xml tags, and identifies the beginning
		//of a test tag, so that extra xml data can be properly retrieved
		
		tempVal = "";
		if(qName.equalsIgnoreCase("original_program")){
			originalProgram = true;
		}else if(qName.equalsIgnoreCase("source_code")){
			if(originalProgram){
				if(attributes.getValue("name") != null){
					fileName = attributes.getValue("name");
				}else{
					fileName = "";
				}
			}
			
		}else if(qName.equalsIgnoreCase("mutant_program")) {
			testIndex = 0;
			prog.add(new MutationData(attributes.getValue("name")));
			if(attributes.getValue("type") == null){
				prog.get(index).setType("");
			}else{
				prog.get(index).setType(attributes.getValue("type"));
			}
		}else if(qName.equalsIgnoreCase("modified_source")){
			if(attributes.getValue("name") != null){
				if(attributes.getValue("start_line") != null){
					if(attributes.getValue("end_line") != null){
						prog.get(index).setSourceFile(attributes.getValue("name"));
						prog.get(index).addLineNumbers(Integer.parseInt(attributes.getValue("start_line")), Integer.parseInt(attributes.getValue("end_line")));
						
					}
				}
			}else{
				prog.get(index).addLineNumber(-1);
			}
		}else if(qName.equalsIgnoreCase("test")){
			currentTest = true;
			prog.get(index).add(attributes.getValue("name"));
			prog.get(index).getXml().add(new XmlStorage());

			
		}else{
			
		}
	}
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		

		
		//obtains data inside the tags, and obtains extra xml data
		if(qName.equalsIgnoreCase("original_program")){
			originalProgram = false;
		}else if(qName.equalsIgnoreCase("source_code")){
			if(originalProgram){
				prog.addSource(tempVal, fileName);
				fileName = "";
			}
		}else if(qName.equalsIgnoreCase("mutant_program")) {
			index++;
		}else if(qName.equalsIgnoreCase("modified_source")){
			prog.get(index).setModifiedSource(tempVal);
			
		
		}else if(qName.equalsIgnoreCase("test")){
			testIndex++;
			currentTest = false;
		}else if(qName.equalsIgnoreCase("result")){
			prog.get(index).add(tempVal);
			if(currentTest){
				prog.get(index).getXml().get(testIndex).setXml(qName, tempVal);
			}
		}else{
			if(currentTest){
				prog.get(index).getXml().get(testIndex).setXml(qName, tempVal);
			}
		}
		tempVal = "";
		
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String temp = new String(ch,start,length);
		tempVal = tempVal + temp;
	}




}