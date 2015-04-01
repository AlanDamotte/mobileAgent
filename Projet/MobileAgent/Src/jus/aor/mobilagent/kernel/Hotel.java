/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package jus.aor.mobilagent.kernel;

import java.io.Serializable;

import jus.aor.mobilagent.lookforhotel.Numero;

/**
 * Un hotel qui est caractérisé par son nom et sa localisation.
 * @author Morat 
 */
@SuppressWarnings("serial")
public class Hotel implements Serializable{
	/** la localisation de l'hôtel */
	public String localisation;
	/** le nom de l'hôtel */
	public String name;
	private Numero numero;
	/**
	 * Définition d'un hôtel par son nom et sa localisation.
	 * @param name le nom de l'hôtel
	 * @param localisation la localisation de l'hôtel
	 */
	public Hotel(String name, String localisation) { this.name=name; this.localisation=localisation;}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {return "Hotel{"+name+","+localisation+"}";}
	
	public void addTel(Numero num){
		this.numero = num;
	}
}
