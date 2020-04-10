package es.upm.dit.adsw.practica3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PruebaIntegracion {

    public static void main(String[] arg) {
    	String f="grafo.txt";
    	if (arg.length > 0)
    		f=arg[0];
		try {
			GrafoPosiciones g = new GrafoPosiciones(new Scanner(new FileInputStream(f)));
			g.dibuja();
			Handler h = new ConsoleHandler();
			Filter filter = new Filter() {
				@Override
				public boolean isLoggable(LogRecord record) {
					return record.getLevel() == Level.INFO && record.getMessage().contains(" entra en  ") && record.getParameters().length == 3 && record.getParameters()[0].equals("id000");
				}
			};
			h.setFilter(filter);
			Vehiculo.setLogger(h, Level.FINE);
			
			Vehiculo v1=new Vehiculo("id000",new Vector(-10.0,0.0),-1.0,new Vector(0.0,0.0),0.0);
			v1.setNavegador(new NavegadorImpl(v1,g));
			Vehiculo v2=new Vehiculo("id001",new Vector(-10.0,30.0),-1.0,new Vector(0.0,30.0),0.0);
			v2.setNavegador(new NavegadorImpl(v2,g));
			Vehiculo v3=new Vehiculo("id002",new Vector(30.0,30.0),-1.0,new Vector(40.0,30.0),0.0);
			v3.setNavegador(new NavegadorImpl(v3,g));
			Thread t1=v1.arrancaVehiculo(new Vector(0.0,30.0),2);
			Thread t2=v2.arrancaVehiculo(new Vector(40.0,30.0),2);
			Thread t3=v3.arrancaVehiculo(new Vector(40.0,0.0),2);
			t1.join();
			t2.join();
			t3.join();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
