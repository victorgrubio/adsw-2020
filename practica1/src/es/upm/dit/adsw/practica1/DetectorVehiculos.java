package es.upm.dit.adsw.practica1;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * @author juancarlosduenaslopez
 * @author mmiguel
 *
 */
public class DetectorVehiculos {

	private Vehiculo v;
	private List<Vehiculo> detectados;
	private int n;
	
	/**
	 * Construye un DetectorVehiculos para el vehiculo v 
	 * @param v vehiculo en el que se encuentra el detector
	 */
	public DetectorVehiculos(Vehiculo v) {
		this.v = v;
		this.detectados = new ArrayList<Vehiculo> (); 
		this.n = 0;
		this.v.setDetector(this);
	}

	/**
	 * Devuelve el vehiculo respecto del que se hacen las detecciones
	 * @return vehiculo del detector
	 */
	public Vehiculo getV() {
		return this.v;
	}
		
	@Override
	public String toString() {
		return "DetectorVehiculos [v=" + v + ", detectados=" + detectados + "]";
	}
	
	/**
	 * Incluye un vehiculo como detectado
	 * @param v nuevo vehiculo detectado
	 */
	public void addVehiculo (Vehiculo v) {
		this.detectados.add(v);
	}

	/**
	 * Busca un vehiculo entre los detectados, teniedo en cuenta que la 
	 * busqueda se hace a partir de los identificadores
	 * @param veh vehiculo a buscar
	 * @return datos que tenemos del vehiculo encontrado. Null si no está entre los detectados
	 */
	public Vehiculo buscaVehiculo(Vehiculo veh) {
		if (this.detectados.contains(veh))
			return this.detectados.get(this.detectados.indexOf(veh));
		return null;
	}

	
	/**
	 * Obtiene el conjunto de vehiculos detectados que cumplen el selector
	 * @param s selector de filtrado de vehiculos
	 * @return conjunto de detectados que cumplen la seleccion
	 */
	public List<Vehiculo> getVehiculos (SelectorVehiculo s) {
		ArrayList<Vehiculo> vs = new ArrayList<Vehiculo> ();
		for (Vehiculo v : this.detectados) {
			if (s.seleccionar(v))
				vs.add(v);
		}
		return vs;
	}
	
	/**
	 * Anade una nueva deteccion comprobado si estaba anteriormente detectado. Para
	 * ver si estaba anteriormente detectado, filtra los detectados para ver si tienen una
	 * posicion compatible con v, si encuentra alguno, actualiza su posicion, y si no
	 * lo anade como nueva deteccion
	 * @param v posicion del vehiculo detectado
	 * @param t instante de la deteccion
	 */
	public void addDeteccion(Vector v, double t) {
		SelectorVehiculo s = new SelectorVehiculoCompatible(v, t);
		List<Vehiculo> compatibles = this.getVehiculos(s);
		if (compatibles.size() > 0) {
			compatibles.get(0).mover(v, t);
		}
		else {
			Vehiculo veh = new Vehiculo("AUTO" + this.n, v, t, v, t);
			this.n ++;
			this.addVehiculo(veh);
		}
	}
	
	private enum Orden {Antes,Igual,Despues};
	// Nos dice si v1 va a impactar con nuestro vehiculo antes, igual o despues que v2
	private Orden impactoRelativoAEsteVehiculo(Vehiculo v1, Vehiculo v2) {
		double t1=v.impacto(v1);
		double t2=v.impacto(v2);
		if (t1 == t2)
			return Orden.Igual;
		if (t1 == Double.POSITIVE_INFINITY)
			return Orden.Despues;
		if (t2 == Double.POSITIVE_INFINITY)
			return Orden.Antes;
		if (t1 > 0.0)
			if (t2 > 0.0)
				if (t1 < t2)
					return Orden.Antes;
				else
					return Orden.Despues;
			else
				return Orden.Antes;
		else
			if (t2 < 0.0)
				if (t1 < t2)
					return Orden.Antes;
				else
					return Orden.Despues;
			else
				return Orden.Despues;
	}
	
	private List<Vehiculo> sortByPeligrosidad(List<Vehiculo> vehiculos){
		for (int index = 0; index < vehiculos.size(); index ++) {
			Vehiculo current = vehiculos.get(index);
			while (index > 0 && impactoRelativoAEsteVehiculo(current, vehiculos.get(index-1)) == Orden.Despues) {
				vehiculos.set(index, vehiculos.get(index-1));
				index -= 1;
			}
			vehiculos.set(index, current);
		}
		return vehiculos;
	}
	
	
	public List<Vehiculo> vehiculoSeMueve(double t){
		SelectorVehiculoTrue s = new SelectorVehiculoTrue();
		List<Vehiculo> vehiculosDetectados = this.getVehiculos(s);
		List<Vehiculo> copyVehiculosDetectados = new ArrayList<Vehiculo>(vehiculosDetectados);
		for (Vehiculo vehiculo: copyVehiculosDetectados) {
			vehiculo.actualizaMover(t);
		}
		List<Vehiculo> sortedCopyVehiculosDetectados = this.sortByPeligrosidad(copyVehiculosDetectados);
		for (Vehiculo vehiculo: sortedCopyVehiculosDetectados) {
			DetectorVehiculos searchedDetector = vehiculo.getDetector();
			if(searchedDetector.buscaVehiculo(this.v) != null) {
				searchedDetector.addDeteccion(this.v.getPos(), t);
			}else{
				searchedDetector.addVehiculo(this.v);
			}
		}
		return sortedCopyVehiculosDetectados;
	}
}
