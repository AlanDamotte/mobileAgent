package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import jus.aor.mobilagent.kernel.Etape;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.AgentServer;
import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel.Jar;
import jus.aor.mobilagent.kernel.Route;

public class Agent implements _Agent{

	

	private static final long serialVersionUID = 1L;
	private boolean first=false;
	protected transient BAMAgentClassLoader loader;
	protected transient AgentServer server;
	protected transient String serverName;
	protected transient Jar jar;
	protected Route route;
	
	@Override
	public void run() {
		// On execute l'action a effectuer sur le serveur actuel: l'etape est supprimee en meme temps
		if(route.hasNext() && first){
			route.get().action.execute();
			route.next();
		}

		if(route.hasNext()){
			
			// On passe sur le prochain serveur:
			// On recupere l'URI du serveur destination dont on extrait l'host et le port pour creer le socket de connexion au serveur
				first=true;
				Socket socket;
				try {
					socket = new Socket(route.get().server.getHost(),route.get().server.getPort());
					OutputStream os=socket.getOutputStream();
					// Flux utilise pour la serialisation
					ObjectOutputStream oosJar = new ObjectOutputStream(os);
					
					//Envoi du jar au serveur par serialisation
					oosJar.writeObject(jar);
					

					ObjectOutputStream oosAg = new ObjectOutputStream(os);
					oosAg.writeObject(this);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		this.server = agentServer;
		this.serverName = serverName;
		if(route==null){
			route=new Route(new Etape(server.site(),_Action.NIHIL));
		}
	}

	@Override
	public void init(BAMAgentClassLoader loader, AgentServer server,
			String serverName) {
		this.server = server;
		this.serverName = serverName;
		this.loader=loader;
		if(route==null){
			route=new Route(new Etape(server.site(),_Action.NIHIL));
		}
		
	}

	@Override
	public void init(BAMAgentClassLoader loader, AgentServer server,
			String serverName, Jar jar) {
		this.server = server;
		this.loader=loader;
		this.serverName = serverName;
		this.jar=jar;
		if(route==null){
			route=new Route(new Etape(server.site(),_Action.NIHIL));
		}
		
	}

	@Override
	public void addEtape(Etape etape) {
		this.route.add(etape);
		
	}

}
