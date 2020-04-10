package es.upm.dit.adsw.practica3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.upm.dit.adsw.practica3.GrafoPosiciones;
import es.upm.dit.adsw.practica3.NavegadorImpl;
import es.upm.dit.adsw.practica3.Vector;
import es.upm.dit.adsw.practica3.Vehiculo;

public class PruebaVehiculo {
	private static String GRAFO="grafo.txt";
	private GrafoPosiciones gp;
	private Vehiculo v;
	
	@Before
	public void setUp() throws Exception {
     	gp = new GrafoPosiciones(GRAFO);
		v=new Vehiculo("id000",new Vector(-10.0,0.0),0.0,new Vector(-9.0,0.0),1.0);
		v.setNavegador(new NavegadorImpl(v,gp));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test (timeout=60000)
	public void test7() {
		Vector hasta = new Vector(0.0,30.0);
		v.irA(hasta);
		assertEquals("No me encuentro el lugar al que he ido",hasta,v.getPos());
	}
	
	@Test (timeout=6000)
	public void test8() {
		Vector hasta = new Vector(0.0,30.0);
		v.mover(hasta,2);
		hasta = new Vector(40.0,30.0);
		v.irA(hasta);
		assertEquals("No me encuentro el lugar al que he ido",hasta,v.getPos());

	}
	
	@Test (timeout=500)
	public void test9(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
			DetectorVehiculos dv2 = new DetectorVehiculos(veh1);
			assertTrue("Error al probar la clase Vehiculo",veh1.getDetector().contains(dv2) && veh1.getDetector().contains(dv1));
		} catch(Throwable t) {
			fail("Error al probar la clase Vehiculo");
		}
	}
	
	@Test (timeout=500)
	public void test10(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			assertTrue("Error al probar la clase Vehiculo",veh1.getDetector().size() == 0);
		} catch(Throwable t) {
			fail("Error al probar la clase Vehiculo");
		}
	}
}
