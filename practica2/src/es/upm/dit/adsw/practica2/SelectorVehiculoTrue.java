package es.upm.dit.adsw.practica2;

public class SelectorVehiculoTrue implements SelectorVehiculo {
	public boolean seleccionar(Vehiculo v) {
		return (v != null);
	}
}
