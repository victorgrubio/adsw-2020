package es.upm.dit.adsw.practica2;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class PruebaDetectorVehiculos {

	@Test (timeout=500)
	public void test1(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
	
			Vehiculo[] vs = new Vehiculo [12];
			for (int i = 0; i < vs.length; i++) {
				vs[i] = new Vehiculo ("id" + i, new Vector(200.0*(i+2), 0), 0, new Vector(200.0*(i+2)-100.0,100.0), 10.0);
				new DetectorVehiculos(vs[i]);
			}
			for (int i = vs.length-1; i >= 0 ; i--) {
				dv1.addVehiculo(vs[i]);
			}
			veh1.mover(12.0);
			for (Vehiculo v : dv1.getVehiculos(new SelectorVehiculoTrue())) {
				DetectorVehiculos dv;
				if ((dv=v.getDetector()) != null)
					assertTrue("Error moviendo un vehiculo. No actualiza correctamente vehiculos detectados",dv.getVehiculos(new SelectorVehiculoTrue()).contains(veh1));
			}
		} catch(Throwable e) {
			fail("Error moviendo un vehiculo");
		}
	}
	
	@Test (timeout=500)
	public void test2(){
		try {
			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
	
			Vehiculo[] vs = new Vehiculo [12];
			for (int i = 0; i < vs.length; i++) {
				vs[i] = new Vehiculo ("id" + i, new Vector(200.0*(i+2), 0), 0, new Vector(200.0*(i+2)-100.0,100.0), 10.0);
				new DetectorVehiculos(vs[i]);
			}
			for (int i = vs.length-1; i >= 0 ; i--) {
				dv1.addVehiculo(vs[i]);
			}
			// veh1.mover(12.0)
			Vector nuevo = new Vector (veh1.getPos().getX() + veh1.getVelocidad().getX()* (12.0 - veh1.getT()),
					veh1.getPos().getY() + veh1.getVelocidad().getY()* (12.0 - veh1.getT()));
			veh1.setPos0(veh1.getPos());
			veh1.setT0(veh1.getT());
			veh1.setPos(nuevo);
			veh1.setT(12.0);
			List<Vehiculo> ordenados=dv1.vehiculoSeMueve(12.0);
			int i=0;
			for (Vehiculo v : ordenados) {
				assertTrue("Error moviendo un vehiculo. No es correcto el orden de los avisos ",v.getId().equals("id"+i));
				i++;
			}
		} catch(Throwable e) {
			fail("Error moviendo un vehiculo");
		}
	}

	@Test (timeout=500)
	public void test3(){
		try {
			Vehiculo[] vs = new Vehiculo [12];
			for (int i = 0; i < vs.length; i++) {
				vs[i] = new Vehiculo ("id" + i, new Vector(200.0+i*200, 200.0+i*200), 0, new Vector(200.0+i*200+(vs.length-i)*1.5,200.0+i*200+(vs.length-i)*1.5), 4.0);
				new DetectorVehiculos(vs[i]);
			}
			for (int i = 0; i < vs.length; i++) {
				DetectorVehiculos unDV;
				if ((unDV=vs[i].getDetector()) != null)
					for (int j = 0; j < vs.length; j++)
						if (i != j)
							unDV.addVehiculo(clonar(vs[j]));
			}

			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
			for (int i = 0; i < vs.length; i++) {
				dv1.addVehiculo(clonar(vs[vs.length-i-1]));
			}

			for(double r=12.0; r < 20.0; r+=2.0) {
				veh1.mover(r);
			}
			for (Vehiculo v : dv1.getVehiculos(new SelectorVehiculoTrue())) {
				DetectorVehiculos dv;
				if((dv=v.getDetector()) != null)
					assertTrue("Error moviendo un vehiculo. No actualiza correctamente vehiculos detectados",dv.getVehiculos(new SelectorVehiculoTrue()).contains(veh1));
			}
		} catch(Throwable e) {
			fail("Error moviendo un vehiculo");
		}
	}

	@Test (timeout=500)
	public void test4(){
		try {
			Vehiculo[] vs = new Vehiculo [12];
			for (int i = 0; i < vs.length; i++) {
				vs[i] = new Vehiculo ("id" + i, new Vector(200.0+i*200, 200.0+i*200), 0, new Vector(200.0+i*200+(vs.length-i)*5,200.0+i*200+(vs.length-i)*5), 4.0);
				new DetectorVehiculos(vs[i]);
			}
			for (int i = 0; i < vs.length; i++) {
				DetectorVehiculos unDV;
				if ((unDV=vs[i].getDetector()) != null)
					for (int j = 0; j < vs.length; j++)
						if (i != j)
							unDV.addVehiculo(clonar(vs[j]));
			}

			Vehiculo veh1 = new Vehiculo("000", new Vector(0.0, 0.0), 0.0, new Vector(100.0, 100.0), 10.0);
			DetectorVehiculos dv1 = new DetectorVehiculos(veh1);
			// 000 va a una velocidad de 10
			// Es una fila india. Del 0 al 3 van mas rápido que el 000, a velocidad cada vez mas lenta
			// Del 6 al 11 van a una velocidad mas lenta los otros va a una velocidad parecida
			for (int i = 0; i < vs.length; i++) {
				dv1.addVehiculo(clonar(vs[vs.length-i-1]));
			}

			for(double r=12.0; r < 18.0; r+=2.0) {
				veh1.mover(r);
			}
			Vector nuevo = new Vector (veh1.getPos().getX() + veh1.getVelocidad().getX()* (18.0 - veh1.getT()),
					veh1.getPos().getY() + veh1.getVelocidad().getY()* (18.0 - veh1.getT()));
			veh1.setPos0(veh1.getPos());
			veh1.setT0(veh1.getT());
			veh1.setPos(nuevo);
			veh1.setT(18.0);
			List<Vehiculo> ordenados=dv1.vehiculoSeMueve(18.0);
			int i=11;
			for (Vehiculo v : ordenados) {
				assertTrue("Error moviendo un vehiculo. No es correcto el orden de los avisos ",v.getId().equals("id"+i));
				if (i == 7)
					break;
				i--;
			}

		} catch(Throwable e) {
			fail("Error moviendo un vehiculo");
		}
	}
	
	private static Vehiculo clonar(Vehiculo v) {
		Vehiculo vc=new Vehiculo(v.getId(),
				new Vector(v.getPos().getX(),v.getPos().getX()),v.getT(),
				new Vector(v.getPos0().getX(),v.getPos0().getY()),v.getT0());
		if (v.getDetector() != null)
			new DetectorVehiculos(vc);
		return v;
	}
}
