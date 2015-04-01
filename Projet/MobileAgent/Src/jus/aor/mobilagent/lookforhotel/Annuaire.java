package jus.aor.mobilagent.lookforhotel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jus.aor.mobilagent.kernel.Hotel;
import jus.aor.mobilagent.kernel._Annuaire;
import jus.aor.mobilagent.kernel._Service;

import org.w3c.dom.Document;


public class Annuaire implements _Annuaire,_Service<HashMap<String,Numero>>{
	
	public HashMap<String,Numero> annuaireT = new HashMap<String,Numero>();
	public String annuaire;
	
	public Annuaire(Object... args){
		this.annuaire =(String) args[0];
		
		DocumentBuilder docBuilder = null;
		Document doc=null;
		
		try{
		docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			doc = docBuilder.parse(new File(annuaire));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String name, numero;
		NodeList list = doc.getElementsByTagName("Telephone");
		NamedNodeMap attrs;
		/* acquisition de toutes les entrées de l'annuaire */
		for(int i =0; i<list.getLength();i++) {
			attrs = list.item(i).getAttributes();
			name=attrs.getNamedItem("name").getNodeValue();
			numero=attrs.getNamedItem("numero").getNodeValue();
			
			annuaireT.put(name,new Numero(numero));
		}
	}


	@Override
	public Numero get(String abonne) {
		return annuaireT.get(abonne);
	}

	@Override
	public HashMap<String,Numero> call(Object... params)
			throws IllegalArgumentException {
		Object[] arg = params;
		@SuppressWarnings("unchecked")
		LinkedList<Hotel> liste  = (LinkedList<Hotel>) arg[0];
		
		HashMap<String,Numero> hotels = new HashMap<String,Numero>();
		
		for(int i=0; i<liste.size(); i++){
			
			Hotel h = liste.get(i);
			
			if(annuaireT.containsKey(h.name)){
				hotels.put(h.name,get(h.name));
			}
		}
		
		return hotels;
	}

}
