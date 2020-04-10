package es.upm.dit.adsw.practica2;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Esta clase implementa un grafo que representa posiciones geograficas representadas mediante vectores
 * @author mmiguel
 *
 */
public class GrafoPosiciones {

	private Map<Vector,Set<Tramo>> vecinos; // tramos que salen de una posicion
    private Map<Vector,Integer> entradas; // numero de entradas en una posicion

	/**
	 * Construye un grafo inicialmente vacio en el que habra que anadir tramos
	 */
	public GrafoPosiciones() {
		vecinos=new HashMap<Vector,Set<Tramo>>();
		entradas=new HashMap<Vector,Integer>();
	}
	
	/**
	 * Construye el grafo leyedo del scanner
	 * @param in scanner de entrada que describe el grafo
	 */
	public GrafoPosiciones(String f) {
		this();
        if (f == null) throw new IllegalArgumentException("Scanner de entrada null");
		Locale def = Locale.getDefault();
		Locale.setDefault(new Locale("en", "US"));
		Scanner in=null;
        try {
        	in=new Scanner(new FileInputStream(f));
            while (in.hasNext()) {
            	if (!in.next().equals("(")) throw new IllegalArgumentException("formato de entrada erroneo");
                double x = in.nextDouble();
            	double y = in.nextDouble();
            	if (!in.next().equals(")")) throw new IllegalArgumentException("formato de entrada erroneo");
            	Vector p1=new Vector(x,y);
            	if (!vecinos.keySet().contains(p1))
            		vecinos.put(p1,new HashSet<Tramo>());
               	if (!entradas.keySet().contains(p1))
            		entradas.put(p1,0);
            	if (!in.next().equals("(")) throw new IllegalArgumentException("formato de entrada erroneo");
                double x2 = in.nextDouble();
            	double y2 = in.nextDouble();
            	Vector p2=new Vector(x2,y2);
            	if (!in.next().equals(")")) throw new IllegalArgumentException("formato de entrada erroneo");
            	if (!vecinos.keySet().contains(p2))
            		vecinos.put(p2,new HashSet<Tramo>());
               	if (!entradas.keySet().contains(p2))
            		entradas.put(p2,0);
            	double tiempo = in.nextDouble();
                addVecino(new Tramo(p1, p2, tiempo));
            }
        }   
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("formato de entrada erroneo", e);
        } catch (FileNotFoundException e) {
        	throw new IllegalArgumentException("nombre de fichero erroneo", e);
		} finally {
        	Locale.setDefault(def);
        	if (in != null) in.close();
        }
	}
	
	/**
	 * Nos dice si una posición esta en el grafo
	 * @param v posicion buscada
	 * @return true cuando la posicion esta en el grafo
	 */
	public boolean estaIncluido(Vector v) {
		if (v == null)
			throw new IllegalArgumentException();
		return vecinos.keySet().contains(v);
	}

    /**
     * Nos dice cuantos tramos salen de una posicion
     * @param v posicion de la que salen los tramos
     * @return numero de caminos
     */
    public int numSalidas(Vector v) {
        return vecinos.get(v).size();
    }

    /**
     * Nos dice cuantos tramos llegan a una posicion
     * @param v posicion a la quellegan los tramos
     * @return numero de tramos
     */
    public int numEntradas(Vector v) {
    	return entradas.get(v);
    }
    
    /**
     * Introduce un tramo en el grafo, lo incluye entre los vecinos de b.desde()
     * @param b descripccion del tramo
     */
    public void addVecino(Tramo b) {
    	if (vecinos.get(b.desde()) == null)
    		vecinos.put(b.desde(),new HashSet<Tramo>());
    	if (vecinos.get(b.hasta()) == null)
    		vecinos.put(b.hasta(),new HashSet<Tramo>());
    	vecinos.get(b.desde()).add(b);
    	entradas.put(b.hasta(),entradas.get(b.hasta())+1);
    }
    
    /**
     * Devuelve el conjunto de tramos que llevan desde esta posición a otras posiciones colindantes
     * @param v posicion de origen
     * @return tramos a posiciones colindantes
     */
    public Tramo[] posicionesVecinas(Vector v) {
    	Tramo[] vecinas=new Tramo[vecinos.get(v).size()];
    	int i=0;
    	for (Tramo edge : vecinos.get(v))
    		vecinas[i++]=edge;
    	return vecinas;
    }
    
    /**
     * Nos devuelve el conjunto de tramos directos (no hay que pasar por otra posicion) para dos posiciones colindantes
     * @param origen posicion de origen de los tramos. Si no son vecinos el array tiene tamano 0
     * @param destino posicion destino de los tramos
     * @return conjunto de tramos
     */
    public Tramo[] caminosAVecino(Vector origen, Vector destino) {
    	Set<Tramo> tramos=new HashSet<Tramo>();
    	for (Tramo c : posicionesVecinas(origen))
    		if (c.hasta().equals(destino))
    			tramos.add(c);
    	Tramo[] a=new Tramo[tramos.size()];
    	tramos.toArray(a);
    	return a;
    }
    
    /**
     * Devuelve todas las posiciones del grafo
     * @return posiciones del grafo
     */
    public Vector[] posiciones() {
    	Vector[] a = new Vector[vecinos.keySet().size()];
    	vecinos.keySet().toArray(a);
    	return a;
    }
    
    private double scl=1.0;
    private static final int PASOS = 3;
    
    public synchronized void dibuja() {
    	double max=Double.NEGATIVE_INFINITY;
    	double min=Double.POSITIVE_INFINITY;
    	Vector[] poss=posiciones();
    	for (Vector v : poss) {
    		if (max < v.getX())
    			max=v.getX();
    		if (max < v.getY())
    			max=v.getY();
    		if (min > v.getX())
    			min=v.getX();
    		if (min > v.getY())
    			min=v.getY();
    	}
    	
    	if (max - min < 0.0001) {
    		max=max+10;
    		min=min-10;
    	}
    	StdDraw.setCanvasSize(720,720);
    	StdDraw.setScale(min, max);
    	scl=(max-min)*0.03;
		StdDraw.clear();
		StdDraw.show(0);
		
	    StdDraw.setPenColor(StdDraw.BLACK);
	    for (Vector p : posiciones())
	        for (Tramo c : posicionesVecinas(p)) {
	        	flecha(c.desde().getX(),c.desde().getY(),c.hasta().getX(),c.hasta().getY(),1,0.5);
	        }
		 for (Vector p : posiciones()) {
	        // StdDraw.setPenColor(StdDraw.WHITE);
	        // StdDraw.filledCircle(p.getX(),p.getY(), scl);
	        StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.circle(p.getX(),p.getY(),scl);
	    	StdDraw.text(p.getX(),p.getY()-scl,"("+p.getX()+","+p.getY()+")");
	    }
	    StdDraw.show(0);
	}
    
    private void flecha(double x1, double y1, double x2, double y2, double arrowWidth, double arrowHeight) {
        double xDistance = x2 - x1;
        double yDistance = y2 - y1;
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        double xm = distance - arrowWidth;
        double xn = xm;
        double ym = arrowHeight;
        double yn = -arrowHeight;
        double x;

        double sin = yDistance / distance;
        double cos = xDistance / distance;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        double[] xPoints = {x2, xm, xn};
        double[] yPoints = {y2, ym, yn};

        StdDraw.line(x1, y1, x2, y2);
        StdDraw.filledPolygon(xPoints, yPoints);
    }
    
    private Map<Vehiculo,Tramo> dibujadoEn=new HashMap<Vehiculo,Tramo>();
    private Map<Vehiculo,Vector> dibujadoEnPos=new HashMap<Vehiculo,Vector>();
    private Map<Vehiculo,Color> color=new HashMap<Vehiculo,Color>();
    private int ultimo=0;
    private final Color[] ultimoc= {Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};
    public synchronized void mueve(Vehiculo v, Tramo c) {
    	if (dibujadoEnPos.get(v) != null)
    		borraVehiculo(dibujadoEnPos.get(v));
    	Tramo cc;
    	if ((cc=dibujadoEn.get(v)) != null) {
    		flecha(cc.desde().getX(),cc.desde().getY(),cc.hasta().getX(),cc.hasta().getY(),1,0.5);    			
    	}
    	Vector pos;
    	if ((pos=dibujadoEnPos.get(v)) != null) {
    		if (vecinos.keySet().contains(pos))
    			StdDraw.circle(pos.getX(),pos.getY(),scl);
    		for (Set<Tramo> sc : vecinos.values())
    			for (Tramo unTramo : sc)
    				if (unTramo.hasta().equals(pos) || unTramo.desde().equals(pos))
    					flecha(unTramo.desde().getX(),unTramo.desde().getY(),unTramo.hasta().getX(),unTramo.hasta().getY(),1,0.5);

    	}
    	if (color.get(v) == null) {
    		color.put(v, ultimoc[ultimo]);
    		ultimo=(ultimo+1)%ultimoc.length;
    	}
    	
    	for (double i=1; i < PASOS; i++) {
    		Vector inter=intermedio(c,i/PASOS);
    		dibujaVehiculo(inter);
			try {
				Thread.sleep(Math.round(c.tiempo() / PASOS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		borraVehiculo(inter);
    		flecha(c.desde().getX(),c.desde().getY(),c.hasta().getX(),c.hasta().getY(),1,0.5);
    	}
    		
    	dibujaVehiculo(c.hasta());
    	dibujadoEn.put(v, c);
    	dibujadoEnPos.put(v,c.hasta());
		try {
			Thread.sleep(Math.round(c.tiempo() / PASOS));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private Vector intermedio(Tramo c, double i) {
    	return new Vector(c.desde().getX()+(c.hasta().getX()-c.desde().getX())*i,c.desde().getY()+(c.hasta().getY()-c.desde().getY())*i);
    }
    
    private void borraVehiculo(Vector v) {
    	StdDraw.setPenColor(StdDraw.WHITE);
    	StdDraw.filledRectangle(v.getX(), v.getY(), 0.6, 0.6);
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.show(0);
    }
    
    private void dibujaVehiculo(Vector v) {
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.filledRectangle(v.getX(), v.getY(), 0.5, 0.5);
    	StdDraw.show(0);
    }
}
