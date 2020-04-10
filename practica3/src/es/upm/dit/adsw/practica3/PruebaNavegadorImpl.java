package es.upm.dit.adsw.practica3;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PruebaNavegadorImpl {

	private static String GRAFO="grafo.txt";
	private GrafoPosiciones gp;
	private Vehiculo v;
	private InputStream f;
	
	@Before
	public void setUp() throws Exception {
		gp = new GrafoPosiciones(new Scanner(f=new FileInputStream(GRAFO)));
		v=new Vehiculo("id000",new Vector(-10.0,0.0),0.0,new Vector(-9.0,0.0),1.0);
		v.setNavegador(new NavegadorImpl(v,gp));
	}

	@After
	public void tearDown() throws Exception {
		f.close();
	}

	@Test (timeout=500)
	public void test1(){
		try {
			Vector hasta = new Vector(0.0,30.0);
			boolean pasadoPorX40=false;
			int nc=0;
			v.getNavegador().setSecuenciaMovimientos(hasta);
			while (!v.getNavegador().finMovimiento()) {
				Tramo c = v.getNavegador().siguienteMovimiento();
				if (c.hasta().getX() == 40) {
					pasadoPorX40=true;
				}
				nc++;
			}
			assertTrue("El tramo por el que hemos pasado esta mal",pasadoPorX40);
			assertTrue("Error en la cantidad de tramos devuelto",nc == 11);
		} catch(Throwable e) {
			fail("Error calculando movimientos y recuperando los caminos");
		} 
	}
	
	@Test (timeout=500)
	public void test2(){
		try {
			v.mover(new Vector(0.0,30.0),2);
			Vector hasta = new Vector(0.0,0.0);
			boolean pasadoPorX010=false;
			int nc=0;
			v.getNavegador().setSecuenciaMovimientos(hasta);
			while (!v.getNavegador().finMovimiento()) {
				Tramo c = v.getNavegador().siguienteMovimiento();
				if (c.hasta().getX() == 0 && c.hasta().getY() == 10.0) {
					pasadoPorX010=true;
				}
				nc++;
			}
			assertTrue("Deberiamos haber pasado por utima columna y no lo hemos hecho",pasadoPorX010);
			assertTrue("Error en la cantidad de tramos devuelto",nc == 3);
		} catch(Throwable e) {
			fail("Error calculando movimientos y recuperando los caminos");
		} 
	}
	
	@Test (timeout=500)
	public void test3(){
		try {
			v.mover(new Vector(0.0,30.0),2);
			Vector hasta = new Vector(40.0,30.0);
			boolean pasadoPorX1030=false;
			int nc=0;
			v.getNavegador().setSecuenciaMovimientos(hasta);
			while (!v.getNavegador().finMovimiento()) {
				Tramo c = v.getNavegador().siguienteMovimiento();
				if (c.hasta().getX() == 10.0 && c.hasta().getY() == 30.0) {
					pasadoPorX1030=true;
				}
				nc++;
			}
			assertTrue("Deberiamos haber pasado por utima columna y no lo hemos hecho",pasadoPorX1030);
			assertTrue("Error en la cantidad de tramos devuelto",nc == 4);
		} catch(Throwable e) {
			fail("Error calculando movimientos y recuperando los tramos");
		} 
	}
	
	@Test (timeout=500)
	public void test4(){
		try {
			v.mover(new Vector(0.0,0.0),2);
			Vector hasta = new Vector(0.0,0.0);
			int nc=0;
			v.getNavegador().setSecuenciaMovimientos(hasta);
			while (!v.getNavegador().finMovimiento()) {
				v.getNavegador().siguienteMovimiento();
				nc++;
			}
			assertTrue("Me muevo cuando no deberia hacerlo",nc == 0);
		} catch(Throwable e) {
			fail("Error calculando movimientos y recuperando los caminos");
		} 
	}
	
}
