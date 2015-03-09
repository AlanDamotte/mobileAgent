package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 
 * @author Romain Barthelemy, Alan Damotte
 *
 */
public class AgentInputStream extends ObjectInputStream {

	BAMAgentClassLoader loader;

	public AgentInputStream(InputStream inputStream, BAMAgentClassLoader BAMAgent) throws IOException {
		super(inputStream);
		this.loader=BAMAgent;
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		return loader.loadClass(desc.getName());
	}

	
}