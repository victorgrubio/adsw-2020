package es.upm.dit.adsw.practica1;

import static org.junit.Assert.*;

import org.junit.Test;

public class PruebaVehiculo {

	@Test
	public void testNullDetector() {
		Vehiculo vehiculo1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(10.0, 10.0), 10.0);
		assertEquals(vehiculo1.getDetector(), null);
	}
	
	@Test
	public void testVehiculoConDetector() {
		Vehiculo vehiculo1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(10.0, 10.0), 10.0);
		assertEquals(vehiculo1.getDetector(), null);
		DetectorVehiculos detector1 = new DetectorVehiculos(vehiculo1);
		assertEquals(vehiculo1, detector1.getV());
		assertEquals(vehiculo1.getDetector(), detector1);
	}

}
