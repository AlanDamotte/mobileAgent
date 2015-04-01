package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.jar.JarException;

public class BAMServerClassLoader extends URLClassLoader{

	HashMap<String,Class<?>> classMap;
	Jar jar;
	
	public BAMServerClassLoader(){
		super(new URL[]{});
		classMap = new HashMap<String,Class<?>>();
	}
	
	public BAMServerClassLoader(ClassLoader loader) {
		super(new URL[]{},loader);
		classMap = new HashMap<String,Class<?>>();
	}
	
	public BAMServerClassLoader(URL[] urls, ClassLoader loader) {
		super(urls,loader);
		classMap = new HashMap<String,Class<?>>();
	}
	
	public void addURL(String url){
			try {
				jar = new Jar(url);
			} catch (JarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			integrateCode(jar);
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
