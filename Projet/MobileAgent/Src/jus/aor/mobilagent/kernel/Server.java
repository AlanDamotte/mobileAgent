/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server implements _Server{
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera ataché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected int port=10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;
	
	protected BAMServerClassLoader loader;
	protected Class<?> classe;
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écuote du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			/* démarrage du server d'agents mobiles attaché à cette machine */
			loader = new BAMServerClassLoader(this.getClass().getClassLoader());
			agentServer = new AgentServer((BAMAgentClassLoader) loader,name,port);
			new Thread(agentServer).start();
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		logger.log(Level.FINE, "	Serveur: ajout service");
		//System.out.println("	Serveur: ajout service");
		_Service<?> service;
		try {
			loader.addURL(new URL("file:/"+System.getProperty("user.dir")+"/"+codeBase));
			classe = (Class<?>)Class.forName(classeName,true,loader);
			service = (_Service<?>) classe.getConstructor(Object[].class).newInstance(new Object[]{args});
			agentServer.addService(name,service);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		logger.log(Level.FINE, "	Serveur: deploy agent");
		//System.out.println("	Serveur: deploy agent");
		Class<_Agent> agentClasse;
		_Agent agent;
		try {
			BAMServerClassLoader loader = new BAMServerClassLoader(this.loader);
			loader.addURL(new URL("file:/"+System.getProperty("user.dir")+"/"+codeBase));
			
			agentClasse = (Class<_Agent>)Class.forName(classeName,true,loader);

			agent = (_Agent) agentClasse.getConstructor(Object[].class).newInstance(new Object[]{args});
			
			//Sylvain Windows
			//agent.init(loader,agentServer, name,new Jar("C:/Users/Sylvain/git/ricm4-projet-ar/hello.jar"));
			
			//Fabien Windows
			agent.init(loader,agentServer, name,new Jar(System.getProperty("user.dir")+"/"+codeBase));
			
			//Fabien Linux
			//agent.init(loader,agentServer, name,new Jar("/home/eloyf/Bureau/workspace/ricm4-projet-ar/hello.jar"));
			
			//Liz Linux
			//agent.init(loader,agentServer, name,new Jar("/home/liz/Documents/4A/Semestre4/AR/projet/hello.jar"));
			//agent.init(loader,agentServer, name,new Jar("/home/liz/Documents/4A/Semestre4/AR/ricm4-projet-ar/Hostel.jar"));
			
			for(int i=0; i<etapeAddress.size(); i++){
				Field f = agentClasse.getDeclaredField(etapeAction.get(i));
				f.setAccessible(true);
				_Action a = (_Action) f.get(agent);
				agent.addEtape(new Etape(new URI(etapeAddress.get(i)),a ));	
			}
			agentServer.runAgent(agent);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
}
