package es.upm.dit.adsw.practica2;

public interface Navegador {

	/**
	 * Devuelve el vehiculo con el que trabaja el navegador
	 * @return vehiculo del navegador
	 */
	Vehiculo getVehiculo();
	
	/**
	 * Actualiza la ruta de movimientos para llegar desde la posicion actual del vehiculo del navegador a un destino dado.
	 * Si la posicion actual del vehiculo no esta en el mapa, la ruta partira de la posicion del mapa mas proxima a la
	 * posicion actual del vehiculo
	 * @param destino destino final de la ruta buscada
	 * @throws lava.lang.NoSuchElementException si no se puede llegar a ese destino
	 * @throws java.lang.IllegalArgumentException si el desnino no esta en el mapa
	 */
	void setSecuenciaMovimientos(Vector destino);
	
	/**
	 * Nos dice si hemos recorrido ya toda la ruta
	 * @return true si no hay mas movimientos que recorrer
	 * @throws NoSuchElementException si no se ha fijado todavia la ruta
	 */
	boolean finMovimiento();
	
	/**
	 * Nos devuelve el siguiente movimiento de la ruta
	 * @return siguiente movimiento
	 * @throws NoSuchElementException si hemos llegado al final de la ruta y no hay mas tramos o si no se ha fijado ruta
	 */
	Tramo siguienteMovimiento();

	/**
	 * Nos dice el mapa con el que trabaja el navegador
	 * @return el mapa representado con un grafo de posiciones
	 */
	GrafoPosiciones getMapa();
}
