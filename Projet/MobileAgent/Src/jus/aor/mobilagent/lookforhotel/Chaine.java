package jus.aor.mobilagent.lookforhotel;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel.Hotel;
import jus.aor.mobilagent.kernel._Chaine;
import jus.aor.mobilagent.kernel._Service;



public class Chaine implements _Chaine, _Service<Collection<Hotel>>{
	
	public Collection<Hotel> hotels;
	public String listeHotel;
	
public Chaine(Object... args){
		
		this.listeHotel = (String) args[0];
		
		DocumentBuilder docBuilder = null;
		Document doc=null;
		hotels = new LinkedList<Hotel>();
		
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

		
			doc = docBuilder.parse(new File(listeHotel));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String name, localisation;
		NodeList list = doc.getElementsByTagName("Hotel");
		NamedNodeMap attrs;
		
		for(int i =0; i<list.getLength();i++) {
			attrs = list.item(i).getAttributes();
			name=attrs.getNamedItem("name").getNodeValue();
			localisation=attrs.getNamedItem("localisation").getNodeValue();
			hotels.add(new Hotel(name,localisation));
			
		}
		
	}
	
	@Override
	public Collection<Hotel> call(Object... params)
			throws IllegalArgumentException {
		return get(params[0].toString());
		
	}
	
	@Override
	public List<Hotel> get(String localisation) {
		LinkedList<Hotel> liste = new LinkedList<Hotel>();
		Iterator<Hotel> it = hotels.iterator();
	
		while(it.hasNext()) {

		    Hotel element = it.next(); 
		    if((element.localisation).equals(localisation)){
				liste.add(element);
			}
		} 
		return liste;
	}
}
