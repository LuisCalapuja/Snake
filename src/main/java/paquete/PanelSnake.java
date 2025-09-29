package paquete;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelSnake extends JPanel {
    Color colorsnake = Color.blue;
    Color colorcomida=Color.red;
    int tammax, tam, can, res;   // tammax de panel, can cantidad de cuadros.
    List<int[]> snake= new ArrayList<>();
    int[] comida=new int[2];
    String direccion="de";
    String direccionProxima="de";
    
    Thread hilo;
    Caminante camino;
    
    public PanelSnake(int tammax, int can){
        this.tammax=tammax;
        this.can=can;
        this.tam=tammax/can;
        this.res=tammax%can;
        int[] a={can/2-1,can/2-1};
        int[] b={can/2,can/2-1};
        snake.add(a);
        snake.add(b);
        generarComida();
        
        camino=new Caminante(this);
        hilo=new Thread(camino);
        hilo.start();
    }
    
    @Override
    public void paint(Graphics pintor){
        super.paint(pintor);    //sobre escribe la imagen
        pintor.setColor(colorsnake);
        // pintando serpiente
        for(int[] par:snake){
            pintor.fillRect(res/2+par[0]*tam, res/2+par[1]*tam, tam-1, tam-1);
        }
        //pintando comida
        pintor.setColor(colorcomida);
        pintor.fillRect(res/2+comida[0]*tam, res/2+comida[1]*tam, tam-1, tam-1);
        
    }
    
    public void avanzar(){
        igualarDir();
        int[] ultimo=snake.get(snake.size()-1);
        int agregarX=0;
        int agregarY=0;
        switch(direccion){
            case "de": agregarX=1; break;
            case "iz": agregarX=-1; break;
            case "ar": agregarY=-1; break;
            case "ab": agregarY=1; break;
        }
        // Math.floorMod avanza hasta la posicion can y regresa al inicio de la colum
        // igual con la fila.
        int[] nuevo={Math.floorMod(ultimo[0]+agregarX, can), Math.floorMod(ultimo[1]+agregarY, can)};
        
        // funcionalidad para choques
        boolean existe=false;
        for(int i=0; i<snake.size(); i++){
            if(nuevo[0]==snake.get(i)[0] && nuevo[1]==snake.get(i)[1]){
                existe=true;
                break;
            }
        }
        if(existe){
            JOptionPane.showMessageDialog(this, "Haz perdido");
        }else{
            if(nuevo[0]==comida[0] && nuevo[1]==comida[1]){
                snake.add(nuevo);
                generarComida();
            }else{
                snake.add(nuevo);   //avance de la cabeza
                snake.remove(0);    //borra la cola
            }
        }
    }
    
    public void generarComida(){
        boolean existe=false;
        int a=(int)(Math.random()*can);
        int b=(int)(Math.random()*can);
        
        for(int[] par:snake){
            if(par[0]==a && par[1]==b){
                existe=true;
                generarComida();
                break;
            }
        }
        if(!existe){
            this.comida[0]=a;
            this.comida[1]=b;
        }
    }
    
    public void cambiarDireccion(String dir){
        // condicionamos el cambio de direccion
        if((this.direccion.equals("de")||this.direccion.equals("iz"))&&(dir.equals("ar")||dir.equals("ab"))){
            this.direccionProxima=dir;
        }
        if((this.direccion.equals("ar")||this.direccion.equals("ab"))&&(dir.equals("de")||dir.equals("iz"))){
           this.direccionProxima=dir;
        }
    }
    
    public void igualarDir(){
        this.direccion=this.direccionProxima;
    }
}
