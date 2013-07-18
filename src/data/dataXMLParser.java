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

public class dataXMLParser {
	
	private dataManager manager;
	private dataMutant mutant;
	private dataTest test;
	
	
	public dataXMLParser(dataManager _manager)
	{
		manager = _manager;
	}
	
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
						mutant = new dataMutant();
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
						
					}
					
					if (startElement.getName().getLocalPart() == "test")
					{
						test = new dataTest();
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
