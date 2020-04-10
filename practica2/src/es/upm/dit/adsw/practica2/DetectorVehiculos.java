package es.upm.dit.adsw.practica2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



/**
 * @author juancarlosduenaslopez
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
		v.setDetector(this);
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
	
	private Vehiculo[] clonar(List<Vehiculo> detectados, double t) {
		Vehiculo[] clonada=new Vehiculo[detectados.size()];
		int i=0;
		for (Vehiculo d : detectados) {
			if (d == null) throw new RuntimeException("detectados no puede tener vehiculos null");
			Vehiculo v = new Vehiculo (d.getId(), d.getPos0(), d.getT0(), d.getPos(), d.getT());
			v.mover(t);
			DetectorVehiculos dv=d.getDetector();
			if (dv != null)
				v.setDetector(dv);
			clonada[i++]=v;
		}
		return clonada;
	}

	public List<Vehiculo> vehiculoSeMueve(double t) {
		Vehiculo[] ordenada=clonar(detectados,t);
//		ordenada.sort(new Comparator<Vehiculo>() {
		ordenar(ordenada,new Comparator<Vehiculo>() {
			@Override
			public int compare(Vehiculo o1, Vehiculo o2) {
				double t1=v.impacto(o1);
				double t2=v.impacto(o2);
				if (t1 == t2)
					return 0;
				if (t1 == Double.POSITIVE_INFINITY)
					return 1;
				if (t2 == Double.POSITIVE_INFINITY)
					return -1;
				if (t1 > 0.0)
					if (t2 > 0.0)
						if (t1 < t2)
							return -1;
						else
							return 1;
					else
						return -1;
				else
					if (t2 < 0.0)
						if (t1 < t2)
							return -1;
						else
							return 1;
					else
						return 1;
			}
		});

		List<Vehiculo> resultado=new ArrayList<Vehiculo>();
		for (Vehiculo veh : ordenada) {
			DetectorVehiculos dv=veh.getDetector();
			if (dv != null) {
				Vehiculo encontrado=dv.buscaVehiculo(v);
				if (encontrado != null) {
					encontrado.mover(v.getPos(),v.getT());
				} else {
					dv.addVehiculo(v);
				}
			}
			resultado.add(veh);
		}
		return resultado;
	}

    private static void juntar(Vehiculo[] a, Vehiculo[] aux, Comparator<Vehiculo> comp, int min, int med, int max) {
        int i = min, j = med;
        for (int k = min; k < max; k++) {
            if      (i == med) aux[k] = a[j++];
            else if (j == max) aux[k] = a[i++];
            else if (comp.compare(a[j],a[i]) < 0) aux[k] = a[j++];
            else aux[k] = a[i++];
        }

        // copiar los ordenados
        for (int k = min; k < max; k++)
            a[k] = aux[k];
    }

    private static void ordenar(Vehiculo[] a, Vehiculo[] aux, Comparator<Vehiculo> comp, int min, int max) {
        // ejecución base
        if (max - min <= 1) return;

        // ordenar recursivo
        int med = min + (max - min) / 2;
        ordenar(a, aux, comp, min, med);
        ordenar(a, aux, comp, med, max);

        juntar(a, aux, comp, min, med, max);
    }

    protected static void ordenar(Vehiculo[] a, Comparator<Vehiculo> comp) {
        int n = a.length;
        Vehiculo[] aux = new Vehiculo[n];
        ordenar(a, aux, comp, 0, n);
    }
}
