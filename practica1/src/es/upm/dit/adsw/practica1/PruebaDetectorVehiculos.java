package es.upm.dit.adsw.practica1;

import static org.junit.Assert.*;

import org.junit.Test;

public class PruebaDetectorVehiculos {
	
	@Test
	public void test1() {

		Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, 
											new Vector(0.0, 10.0), 1.0);
		DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
		Vehiculo vs[] = new Vehiculo [5];
		for (int i=0; i<vs.length; i++) {
			vs[i] = new Vehiculo("id"+i, new Vector(0.0,100.0+20.0*i), 0,
										 new Vector(0.0,100.0+20.0*i+2*(i+1)), 1.0);
			new DetectorVehiculos(vs[i]);
		}
		for(int i=vs.length-1; i>=0; i--) {
			dv1.addVehiculo(vs[i]);
		}
		veh1.mover(2.0);
		assertEquals(veh1.getPos(), new Vector(0.0, 20.0));
	}
	
	@Test
	public void test2() {

		Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, 
											new Vector(0.0, 10.0), 1.0);
		DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
		Vehiculo vs[] = new Vehiculo [5];
		for (int i=0; i<vs.length; i++) {
			vs[i] = new Vehiculo("id"+i, new Vector(0.0,100.0+20.0*i), 0,
										 new Vector(0.0,100.0+20.0*i+2*(i+1)), 1.0);
			new DetectorVehiculos(vs[i]);
		}
		for(int i=vs.length-1; i>=0; i--) {
			dv1.addVehiculo(vs[i]);
		}
		veh1.mover(2.0);
		assertEquals(veh1.getPos(), new Vector(0.0, 20.0));
	}
	
	@Test
	public void test3() {

		Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, 
											new Vector(0.0, 10.0), 1.0);
		DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
		Vehiculo vs[] = new Vehiculo [5];
		for (int i=0; i<vs.length; i++) {
			vs[i] = new Vehiculo("id"+i, new Vector(0.0,100.0+20.0*i), 0,
										 new Vector(0.0,100.0+20.0*i+2*(i+1)), 1.0);
			new DetectorVehiculos(vs[i]);
		}
		for(int i=vs.length-1; i>=0; i--) {
			dv1.addVehiculo(vs[i]);
		}
		veh1.mover(2.0);
		assertEquals(veh1.getPos(), new Vector(0.0, 20.0));
	}
	
	@Test
	public void test4() {
		Vehiculo vehiculo1 = new Vehiculo("001", new Vector(0.0, 150.0), 0.0, 
											new Vector(0.0, 155.0), 1.0);
		DetectorVehiculos detectorVehiculo1 = new DetectorVehiculos(vehiculo1);
		Vehiculo vehiculos[] = new Vehiculo [5];
		for (int i=0; i<vehiculos.length; i++) {
			vehiculos[i] = new Vehiculo("id"+i, new Vector(0.0, 50.0+20.0*i), 0,
										 new Vector(0.0,50.0+20.0*i+2*(i+1)), 1.0);
			new DetectorVehiculos(vehiculos[i]);
		}
		for(int i=vehiculos.length-1; i>=0; i--) {
			detectorVehiculo1.addVehiculo(vehiculos[i]);
		}
		vehiculos[4].mover(5.0);
		assertEquals(vehiculos[4].getPos(), new Vector(0.0, 180.0));
	}
	
	
}
