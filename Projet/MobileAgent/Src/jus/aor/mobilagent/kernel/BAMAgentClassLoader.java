package jus.aor.mobilagent.kernel;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import jus.aor.mobilagent.kernel.Jar;

public class BAMAgentClassLoader extends URLClassLoader{

	HashMap<String,Class<?>> classMap;

	public BAMAgentClassLoader(){
		super(new URL[]{});
		classMap = new HashMap<String,Class<?>>();
	}
	
	public BAMAgentClassLoader(ClassLoader loader) {
		super(new URL[]{},loader);
		classMap = new HashMap<String,Class<?>>();
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void integrateCode(Jar jar) {
		
		Iterator<Entry<String,byte[]>> iterator =jar.classIterator().iterator();
		Class<?> c;
		
		while(iterator.hasNext()){
			Entry<String,byte[]> e = iterator.next();
			c = defineClass(e.getValue(),0,e.getValue().length);
			classMap.put(e.getKey(),c);
			
		}
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		String formatedClassName = className.replace(".","/")+".class";

		
		if(classMap.containsKey(formatedClassName)){
			return classMap.get(formatedClassName);
		}
		else throw new ClassNotFoundException(className);
	}

}
