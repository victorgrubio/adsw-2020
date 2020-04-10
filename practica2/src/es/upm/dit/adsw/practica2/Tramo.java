package es.upm.dit.adsw.practica2;

/**
 * Este es un tramo entre dos posiciones del grafo
 * 
 * @author mmiguel
 *
 */
public class Tramo {
	private Vector desde;
	private Vector hasta;
	private double tiempo;
	
	/**
	 * Este es un constructor de tramo, que puede no ser una linea recta, desde una posicion hasta otra posicion  
	 * @param desde posicion de origen
	 * @param hasta posicion destino
	 * @param tiempo tiempo que se tarda en recorrer el tramo
	 */
	public Tramo(Vector desde, Vector hasta, double tiempo) {
		if (desde == null || hasta == null || Double.isNaN(tiempo) || tiempo < 0)
			throw new IllegalArgumentException();
		this.desde=desde;
		this.hasta=hasta;
		this.tiempo=tiempo;
	}
	
	/**
	 * Devuelve el origen del tramo
	 * @return Origen del tramo
	 */
	public Vector desde() {
		return desde;
	}
	
	/**
	 * Devuelve el destino del tramo
	 * @return Destino del tramo
	 */
	public Vector hasta() {
		return hasta;
	}
	
	/**
	 * Devuelve cuanto tiempo lleva hacer este tramo
	 * @return longitud del tramo
	 */
	public double tiempo() {
		return tiempo;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Tramo))
			return false;
		Tramo other=(Tramo) o;
		return this.desde().equals(other.desde()) && this.hasta().equals(other.hasta());
	}
}
