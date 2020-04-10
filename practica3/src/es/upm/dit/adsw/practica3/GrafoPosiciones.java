package es.upm.dit.adsw.practica3;

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
 * Esta clase implementa un grafo que representa posiciones geograficas representadas 
 * mediante vectores y tramos diregidos que los comunica
 * @author mmiguel
 *
 */
public class GrafoPosiciones {

	private Map<Vector,Set<Tramo>> vecinos; // tramos que salen de una posicion
    private Map<Vector,Integer> entradas; // numero de entradas en una posicion
    private boolean dibujando=false;

	/**
	 * Construye un grafo inicialmente vacio en el que habra que anadir tramos
	 */
	public GrafoPosiciones() {
		vecinos=new HashMap<Vector,Set<Tramo>>();
		entradas=new HashMap<Vector,Integer>();
	}
	
	/**
	 * Construye el grafo leyedo de un fichero
	 * @param f camino del fichero que contiene el grafo
	 */
	public GrafoPosiciones(String f) {
		this();
        if (f == null) throw new IllegalArgumentException("Scanner de entrada null");
		Locale def = Locale.getDefault();
		Locale.setDefault(new Locale("en", "US"));
		Scanner in=null;
        try {
        	in=new Scanner(new FileInputStream(f));
        	leeFichero(in);
        } catch (FileNotFoundException e) {
        	throw new IllegalArgumentException("nombre de fichero erroneo", e);
		} finally {
        	Locale.setDefault(def);
        	if (in != null) in.close();
        }
	}
	
	/**
	 * Construye el grafo a partir de un Scanner. El lenguaje del empleado en la lectura debe se US
	 * @param in scanner de entrada que describe el grafo
	 * @param f el fichero de entrada que describe el grafo
	 */
	public GrafoPosiciones(Scanner in) {
		this();
        if (in == null) throw new IllegalArgumentException("Scanner de entrada null");
        leeFichero(in);
	}
	
	private void leeFichero(Scanner in) {
        try {
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
        }
	}

	/**
	 * Indica si una posición esta en el grafo
	 * @param v la posicion buscada
	 * @return true cuando la posicion esta en el grafo
	 */
	public boolean estaIncluido(Vector v) {
		if (v == null)
			throw new IllegalArgumentException();
		return vecinos.keySet().contains(v);
	}

    /**
     * Retorna el número de tramos que salen de una posicion
     * @param v posicion de la que salen los tramos
     * @return numero de caminos
     */
    public int numSalidas(Vector v) {
        return vecinos.get(v).size();
    }

    /**
     * Retorna el número de tramos que llegan a una posicion
     * @param v posicion a la que llegan los tramos
     * @return numero de tramos
     */
    public int numEntradas(Vector v) {
    	return entradas.get(v);
    }
    
    /**
     * Añada un tramo en el grafo. 
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
     * Retorna el conjunto de tramos que llevan desde la posición de origen a las posiciones colindantes
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
     * Retorna el conjunto de tramos directos (no hay que pasar por otra posicion) para dos posiciones colindantes
     * @param origen posicion de origen de los tramos. Si no son vecinos el array tiene tamano 0
     * @param destino posicion destino de los tramos
     * @return conjunto de tramos
     */
    public Tramo[] caminosAVecino(Vector origen, Vector destino) {
    	Set<Tramo> caminos=new HashSet<Tramo>();
    	for (Tramo c : posicionesVecinas(origen))
    		if (c.hasta().equals(destino))
    			caminos.add(c);
    	Tramo[] a=new Tramo[caminos.size()];
    	caminos.toArray(a);
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
    
    /**
     * Este método gestiona la representación gráfica y animación del movimiento
     * de un vehículo en el grafo
     */
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
	    dibujando=true;
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
    private final Color[] ultimoc= {Color.BLACK,Color.GREEN,Color.RED,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.YELLOW};
    /**
     * Representa la animacion del movimiento de un vehiculo por un tramo en el grafo
     * @param v vehiculo que se mueve
     * @param c tramo por el que nos movemos
     */
    public void mueve(Vehiculo v, Tramo c) {
    	if (!dibujando)
			try {
				Thread.sleep(Math.round(c.tiempo()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				return;
			}
    	synchronized(this) {
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
    	}
    	for (double i=1; i < PASOS; i++) {
    		Vector inter=intermedio(c,i/PASOS);
    		dibujaVehiculo(inter,color.get(v));
    		dibujadoEnPos.put(v, inter);
			try {
				Thread.sleep(Math.round(c.tiempo() / PASOS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized(this) {
	    		borraVehiculo(inter);
	        	for (Vehiculo unV : dibujadoEnPos.keySet())
	        		if (!unV.equals(v) && dibujadoEnPos.get(unV).equals(inter))
	        			dibujaVehiculo(inter,color.get(unV));
	    		flecha(c.desde().getX(),c.desde().getY(),c.hasta().getX(),c.hasta().getY(),1,0.5);
			}
    	}
    		
    	dibujaVehiculo(c.hasta(),color.get(v));
    	synchronized(this) {
    		dibujadoEn.put(v, c);
    		dibujadoEnPos.put(v,c.hasta());
    	}
		try {
			Thread.sleep(Math.round(c.tiempo() / PASOS));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private static Vector intermedio(Tramo c, double i) {
    	return new Vector(c.desde().getX()+(c.hasta().getX()-c.desde().getX())*i,c.desde().getY()+(c.hasta().getY()-c.desde().getY())*i);
    }
    
    private synchronized void borraVehiculo(Vector v) {
    	StdDraw.setPenColor(StdDraw.WHITE);
    	StdDraw.filledRectangle(v.getX(), v.getY(), 0.6, 0.6);
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.show(0);
    }
    
    private synchronized void dibujaVehiculo(Vector v,Color c) {
    	StdDraw.setPenColor(c != null ? c : StdDraw.BLACK);
    	StdDraw.filledRectangle(v.getX(), v.getY(), 0.5, 0.5);
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.show(0);
    }
}
