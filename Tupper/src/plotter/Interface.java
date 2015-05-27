package plotter;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class Interface extends JFrame{
	
	JPanel plot;
	int ptsize = 10;
	int plotx = 106 * ptsize;
	int ploty = 17 * ptsize;
	
	JPanel bottom;
	JButton enter;
	JTextField number;
	JTextField out;
	Boolean isPlotting = false;
	
	
	  /**
     * Creates new form Interface
     */
    public Interface() {
        this.setTitle("Tupper Plotter");
        initComponents();
    }
    
    
    private void initComponents() {
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	JPanel mainpan = new JPanel();
    	plot = new JPanel(){
    		protected void paintComponent(Graphics g){
    			plotForK(g, number.getText());
    		}
    	};
    	plot.setPreferredSize(new Dimension(plotx,ploty));
    	
    	bottom = new JPanel();
    	enter = new JButton("Enter");
    	number = new JTextField();
    	out = new JTextField();
    	out.setEditable(false);
    	
    	enter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				plot.repaint();
								
			}
		});
    	
    	bottom.setLayout(new BorderLayout());
    	bottom.add(new JLabel("  K:  "), BorderLayout.WEST);
    	bottom.add(number, BorderLayout.CENTER);
    	bottom.add(enter, BorderLayout.EAST);
    	bottom.add(out, BorderLayout.SOUTH);
    	    	
    	mainpan.setLayout(new BorderLayout());    	
    	mainpan.add(plot, BorderLayout.NORTH);
    	mainpan.add(bottom, BorderLayout.SOUTH);
    	
    	this.setLayout(new BorderLayout());
    	this.add(mainpan, BorderLayout.CENTER);
    	
    	
    	pack();
    }
    
    Integer count = 0; 
    
    public void plotForKThreaded(final Graphics g, final String k){
    	long time = System.currentTimeMillis();

    	if(k.length()==0){
    		return;
    	}
    	Thread[] threads = new Thread[18];
    	for(int y = 0; y < 17; y++){
    			final int yiter = y;
    			threads[y] = new Thread(new Runnable(){
    			
					@Override
					public void run() {	
						for(int x= 0; x < 106; x++){
							TupperCalculator tp = new TupperCalculator(k);
							g.setColor(tp.getPoint2(x, yiter)?Color.red:Color.blue);
							plotPoint(g, 105-x, yiter);
							synchronized(count){
								count ++;
								System.out.println(count + " of 1802");
							}
						}
					}
    				
    			});    	
    			threads[y].start();
    	}
    	for(int y = 0; y < 17; y++){
    		try {
				threads[y].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	System.out.println("Unthreaded: " + (System.currentTimeMillis()-time)/1000 + "s");
    	
    }
    
    public void plotForK(final Graphics g, final String k){
    	long time = System.currentTimeMillis();
    	int count = 0;
    	for(int y = 0; y < 17; y++){
    		for(int x= 0; x < 106; x++){
    			TupperCalculator tp = new TupperCalculator(k);
    			g.setColor(tp.getPoint2(x, y)?Color.red:Color.blue);
				plotPoint(g, 105-x, y);
				count ++;
				System.out.println(count + " of 1802");
    		}
    	}
    	System.out.println("Unthreaded: " + (System.currentTimeMillis()-time)/1000 + "s");
	}
    
    private void plotPoint(Graphics g, int x, int y){
    	if(x<0 || y > 16 || y < 0 || x> 105){
    		return;		// error;
    	}
    	g.fillRect(ptsize*x, ptsize*y, ptsize, ptsize);
    }
    
    
    
    
    public static void main(String args[]) {
    	
   	
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }
}
