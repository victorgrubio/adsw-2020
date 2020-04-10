package es.upm.dit.adsw.practica3;

public interface Navegador {

	/**
	 * Devuelve el vehiculo asociado al navegador
	 * @return vehiculo del navegador
	 */
	Vehiculo getVehiculo();
	
	/**
	 * Genera el camino entre el origen del vehiculo 
	 * y el destino. Se reprenta como la secuencia de las posciones/vectores
	 * del camino. 
	 * Si la posicion actual del vehiculo no esta en el mapa, el origen del camino
	 * será la posición del mapamás próxima a la posición actual del vehiculo 
	 * @param destino destino final de la ruta deseada
	 * @throws lava.lang.NoSuchElementException si no se puede llegar a ese destino
	 * @throws java.lang.IllegalArgumentException si el desnino no esta en el mapa
	 */
	void setSecuenciaMovimientos(Vector destino);
	
	/**
	 * Comprueba si se ha recorrido la ruta. Ya se ha llegado al destino.
	 * @return true si no hay mas movimientos que recorrer
	 * @throws NoSuchElementException si no se ha fijado todavia la ruta
	 */
	boolean finMovimiento();
	
	/**
Devuelve el siguiente tramo de la ruta
	 * @return siguiente tramo
	 * @throws NoSuchElementException si hemos llegado al final de la ruta 
	 * y no hay mas tramos o si no se ha fijado ruta
	 */
	Tramo siguienteMovimiento();

	/**
	 * Indica el mapa con el que trabaja el navegador
	 * @return el mapa representado con un grafo de posiciones y tramos
	 */
	GrafoPosiciones getMapa();
}

