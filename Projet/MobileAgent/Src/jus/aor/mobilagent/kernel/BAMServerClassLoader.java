package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMServerClassLoader extends URLClassLoader{

	
	public BAMServerClassLoader(){
		super(new URL[]{});
	}
	
	public BAMServerClassLoader(ClassLoader loader) {
		super(new URL[]{},loader);
	}
	
	public void addURL(URL url){
		super.addURL(url);
	}
}
