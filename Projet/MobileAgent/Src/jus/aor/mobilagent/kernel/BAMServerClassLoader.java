package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class BAMServerClassLoader extends URLClassLoader{

	HashMap<String,Class<?>> classMap;
	
	public BAMServerClassLoader(){
		super(new URL[]{});
		classMap = new HashMap<String,Class<?>>();
	}
	
	public BAMServerClassLoader(ClassLoader loader) {
		super(new URL[]{},loader);
		classMap = new HashMap<String,Class<?>>();
	}
	
	public void addURL(URL url){
	}
}
