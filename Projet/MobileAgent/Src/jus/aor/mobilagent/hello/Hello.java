package jus.aor.mobilagent.hello;

import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
@SuppressWarnings("serial")
public class Hello extends Agent{

	private String maRoute;
	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		 maRoute="Route effectuée:\n";
	 }
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			maRoute = maRoute+"Helloword sur "+route.get()+"\n";
			System.out.println("Helloworld");
		}
		
	};
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	protected _Action retour = new _Action(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			System.out.println(maRoute);
		}
	};
}
