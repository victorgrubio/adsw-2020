package es.upm.dit.adsw.practica2;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PruebaVehiculo {

	@Test (timeout=500)
	public void test9(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
			assertTrue("Error al probar la clase Vehiculo",veh1.getDetector() == dv1);
		} catch(Throwable t) {
			fail("Error al probar la clase Vehiculo");
		}
	}
	
	@Test (timeout=500)
	public void test10(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			assertTrue("Error al probar la clase Vehiculo",veh1.getDetector() == null);
		} catch(Throwable t) {
			fail("Error al probar la clase Vehiculo");
		}
	}
}
