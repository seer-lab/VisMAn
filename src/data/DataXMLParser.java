package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * This class represents an XML parser which will take the XML file
 * generated through the mutation and testing process and create DataMutants
 * that are held in the DataManager.
 * @author David Petras
 *
 */
public class DataXMLParser {
	
	private DataManager manager;
	private DataMutant mutant;
	private DataTest test;
	
	/**
	 * Constructor used to create the XMLParser with a DataManager to
	 * hold the created DataMutants.
	 * @param _manager the DataManager that will hold the created DataMutants
	 */
	public DataXMLParser(DataManager _manager)
	{
		manager = _manager;
	}
	
	/**
	 * This method will parse the XML file passed to it and create proper instances
	 * of DataMutant objects from the data.
	 * @param xmlFile the XML file created through the mutation and testing process
	 */
	public void parseDocument(File xmlFile)
	{
		try 
		{
			XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
			InputStream inStream = new FileInputStream(xmlFile);
			XMLEventReader eventReader = xmlFactory.createXMLEventReader(inStream);
			
			while (eventReader.hasNext())
			{
				XMLEvent event = eventReader.nextEvent();
				
				if (event.isStartElement())
				{
					StartElement startElement = event.asStartElement();
					
					if (startElement.getName().getLocalPart() == "mutant_program")
					{
						mutant = new DataMutant();
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext())
						{
							Attribute attribute = attributes.next();
							if (attribute.getName().toString() == "name")
							{
								mutant.setName(attribute.getValue());
							}
							else if (attribute.getName().toString() == "type")
							{
								mutant.setType(attribute.getValue());
							}
						}
					}
					
					if (startElement.getName().getLocalPart() == "modified_source")
					{
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext())
						{
							Attribute attribute = attributes.next();
							if (attribute.getName().toString() == "name")
							{
								mutant.setModifiedSourceName(attribute.getValue());
							}
							else if (attribute.getName().toString() == "start_line")
							{
								mutant.setLine(Integer.parseInt(attribute.getValue()));
							}
							else if (attribute.getName().toString() == "end_line")
							{
								//Does nothing right now.
							}
						}
						
						event = eventReader.nextEvent();
						mutant.setModifiedSource(event.asCharacters().getData());
						
					}
					
					if (startElement.getName().getLocalPart() == "test")
					{
						test = new DataTest();
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext())
						{
							Attribute attribute = attributes.next();
							if (attribute.getName().toString() == "name")
							{
								test.setName(attribute.getValue());
							}
						}
					}
					
					if (startElement.getName().getLocalPart() == "result")
					{
						event = eventReader.nextEvent();
						test.setResult(event.asCharacters().getData());
						mutant.addTest(test);
					}
				}
				
				if (event.isEndElement())
				{
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == "mutant_program")
					{
						manager.addMutant(mutant);
					}
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (XMLStreamException e) 
		{
			e.printStackTrace();
		}
			

	}


}
