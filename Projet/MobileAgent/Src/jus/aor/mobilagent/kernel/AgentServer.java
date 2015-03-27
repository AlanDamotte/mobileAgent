package jus.aor.mobilagent.kernel;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


/**
 * Décrit la partie opérationnelle du fonctionnement de ce serveur.
* Elle contient la boucle (méthode run) de réception des agents mobiles.
* 
 * @author Romain Barthelemy, Alan Damotte
 *
 */
public class AgentServer implements Runnable{

	BAMAgentClassLoader loader;
	HashMap<String,_Service<?>> mesServices;
	String name;
	ServerSocket socketServer;
	int port;
	
	AgentServer(BAMAgentClassLoader loader,String name, int port){
		this.loader=loader;
		this.name=name;
		this.port=port;
		mesServices= new HashMap<String,_Service<?>>();
	}
	
	void addService(String name,_Service<?> s){
		mesServices.put(name,s);
	}
	
	public _Service<?> getService(String name){
		return mesServices.get(name);
	}
	
	public String toString(){
		return name;
	}
	
	/**
	 * Réception des agents mobiles
	 */
	public void run(){
		System.out.println("Run agent serveur");
		Jar jar;
		Socket socketClient;
		_Agent ag;
		
		try {
			socketServer = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		while(true){
			try {
				socketClient = socketServer.accept();
				ObjectInputStream ois = new ObjectInputStream(socketClient.getInputStream());
				jar = (Jar) ois.readObject();
				BAMAgentClassLoader BAMAgent = new BAMAgentClassLoader(loader);
				BAMAgent.integrateCode(jar);
				AgentInputStream ais = new AgentInputStream(socketClient.getInputStream(),BAMAgent);
				ag = (_Agent) ais.readObject();
				ag.init(BAMAgent, this, this.name,jar);
				runAgent(ag);
				
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public URI site() {
		URI uri=null;
		try {
			uri= new URI("mobilagent://localhost:"+port+"/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}

	public void runAgent(_Agent ag) {
		Thread t = new Thread(ag);
		t.start();
	}
		
}