/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel.BAMServerClassLoader;
import jus.aor.mobilagent.kernel.Etape;
import jus.aor.mobilagent.kernel.Jar;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Agent;
import jus.aor.mobilagent.kernel._Service;
import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.AgentServer;
import jus.aor.mobilagent.kernel.BAMAgentClassLoader;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server implements _Server{
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera ataché le service du bus à agents mobiles. Par défaut on prendra le port 10140 */
	protected int port=10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;

	protected BAMAgentClassLoader loader;
	protected BAMServerClassLoader serverLoader;
	protected Agent agent;
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
			loader = new BAMAgentClassLoader(this.getClass().getClassLoader());
			serverLoader = new BAMServerClassLoader(this.getClass().getClassLoader());
			agentServer = new AgentServer(loader,name,port);
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
			serverLoader.addURL("Projet/MobileAgent/"+codeBase);
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
			Class<?> agentClass;
			_Agent agent;

			try {
				BAMAgentClassLoader loader = new BAMAgentClassLoader(this.loader);
				loader.integrateCode(new Jar("Projet/MobileAgent/"+codeBase));
				
				agentClass = (Class<?>)Class.forName(classeName,true,loader);
				Constructor<?> cons = agentClass.getConstructor(Object[].class);
				
				agent = (Agent) cons.newInstance(new Object[]{args});
				
				agent.init(loader,agentServer, name,new Jar("/home/romain/Documents/RICM4/S2/AR/mobileAgent/Projet/MobileAgent/"+codeBase));
				
				for(int i=0; i<etapeAddress.size(); i++){
					Field f = agentClass.getDeclaredField(etapeAction.get(i));
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
