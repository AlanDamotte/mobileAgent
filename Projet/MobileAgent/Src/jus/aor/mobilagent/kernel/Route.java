/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import jus.aor.mobilagent.kernel.Etape;

/**
 * Définit la feuille de route que l'agent va suivre
 * @author  Morat
 */
public class Route implements Iterable<Etape>, Serializable{
	private static final long serialVersionUID = 9081294824590167316L;
	/** la liste des étapes à parcourir autres que la dernière */
	protected List<Etape> route;
	/** la dernière étape de la feuille de route de l'agent qui désigne le serveur de départ. */
	protected Etape retour;
	/** Indique si la feuille de route est épuisée ou non. */
	protected boolean hasNext;
	/**
	 * Construction d'une route.
	 * @param retour  le server initial et de retour.
	 */
	public Route(Etape retour) {
		route = new LinkedList<Etape>();
		this.retour = retour;
		hasNext=true;
	}
	/**
	 * Ajoute une étape en fin de route.
	 * @param e l'étape à ajouter
	 */
	public void add(Etape e) { route.add(route.size(),e);}
	/**
	 * Restitue la prochaine étape ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	public Etape get() throws NoSuchElementException {
		if(route.size()!=0){
			return route.get(0);
		} else {
			throw new NoSuchElementException();
		}
	}
	/**
	 * Restitue la prochaine étape et élimine de la route ou la dernière qui est la base de départ.
	 * @return la prochaine étape.
	 */
	public Etape next() throws NoSuchElementException {
		if(this.hasNext()){
			Etape etape = route.get(0);
			route.remove(0);
			if(route.size()==0){
				hasNext = false;
			}
			return etape;
		} else {
			throw new NoSuchElementException();
		}
	}
	/**
	 * Il y a-t-il encore une étape à parcourir.
	 * @return vrai si une étape est possible.
	 */
	public boolean hasNext() { return hasNext;}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Etape> iterator(){return route.iterator();}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {return route.toString().replaceAll(", ","->");}
}
