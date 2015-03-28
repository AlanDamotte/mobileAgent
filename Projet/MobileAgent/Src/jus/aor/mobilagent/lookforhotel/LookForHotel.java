/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

package jus.aor.mobilagent.lookforhotel;

import java.util.Collection;
import java.util.LinkedList;

import jus.aor.mobilagent.kernel._Service;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;


/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
@SuppressWarnings("serial")
public class LookForHotel extends Agent{
	/** le critère de localisaton choisi */
	private String localisation;
	private LinkedList<String> listeH = new LinkedList<String>();
	private LinkedList<String> listeN = new LinkedList<String>();
	private long time;
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String... args){
		time = call();
		localisation = args[0];
	}
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		return System.currentTimeMillis();
	}
	
	protected _Action findHotel = new _Action(){

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void execute() {
			
			_Service<?> service = server.getService("Hotels");
			listeH.addAll((Collection<String>)service.call(localisation));
			System.out.println(call());
		}
		
	};

	protected _Action printTime = new _Action() {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			long timeFin = call();
			for(int i=0; i<listeN.size(); i++){
				System.out.println("Hotel: "+listeH.get(i)+" numero:"+listeN.get(i));
			}
			System.out.println("Temps de recherche: "+(timeFin-time)+" ms");			
		}
	};
	
	protected _Action findTelephone = new _Action(){

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void execute() {
			_Service<?> service = server.getService("Telephones");
			listeN = (LinkedList<String>) service.call(listeH);
		}
		
	};
}
