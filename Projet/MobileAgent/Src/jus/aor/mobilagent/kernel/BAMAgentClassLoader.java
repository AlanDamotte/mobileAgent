package jus.aor.mobilagent.kernel;
import java.net.URLClassLoader;
import java.util.HashMap;

public class BAMAgentClassLoader{
	
	HashMap<String,Class<?>> classMap;

	public BAMAgentClassLoader(){
		super(new URL[]{});
		classMap = new HashMap<String,Class<?>>();
	}
	
	public BAMAgentClassLoader(ClassLoader loader) {
		super(new URL[]{},loader);
		classMap = new HashMap<String,Class<?>>();
	}

}
