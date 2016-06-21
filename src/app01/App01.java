/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app01;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
// ╔═╦═╗    ╔═╗  ╔╗
// ║ ║ ║    ║ ║  ╚╝
// ╠═╬═╣    ╚═╝
// ║ ║ ║
// ╚═╩═╝
/**
 *
 * @author redist
 */
public class App01 {
    private static class coord {
        private int x;
        private int y;
    }
    private static class node_t {
        private int val;
        private int px, py;
        private boolean visited;
    }

    private static int field_width;
    private static int field_height;
    private static char field[][];
    private static node_t a[][];
    private static int  qt, qh;
    private static int qx[], qy[];
    
    private static final int[] kx = {0,  0, 1, -1};
    private static final int[] ky = {1, -1, 0,  0};
    private static final char[] walls = {'╬', '╔', '╗', '╝', '╚', '║', '═'};
    

    /**
     * Function set consol cursor in the specific coordinates
     * @param x - x coordinate
     * @param y - y coordinate
     */
    private static void gotoxy(int x, int y){
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, y, x));
    }
    
    private static void field_update(){
        gotoxy(1,1); 
        
        for ( int n = 1; n <= field_height; n++)
            for ( int m = 1; m <= field_width; m++) {
                if ( a[n][m].val == 0 && a[n][m].visited == true )
                    field[n][m] = '.';

                if ( a[n][m].val == 0x42 )
                    field[n][m] = 'o';
            }
        
        
        for ( char[] line : field )
            for ( char c : line )
                System.out.print(c);
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
         String INPUT = "/home/redist/workspace/netbeans/app01/src/app01/source.txt";  
         FileInputStream instream = new FileInputStream(INPUT);
        
        Scanner in = new Scanner(instream);
        field_height = in.nextInt();
        field_width = in.nextInt();
        
        //System.out.println(field_height);
        //System.out.println(field_width);
        
        field = new char [field_height+2][field_width+3];
        a = new node_t [field_height+2][field_width+2];
        qx = new int [field_height*field_width];
        qy = new int [field_height*field_width];

        field_init();

        int n, m;
        coord start = new coord();
        
        for ( n = 0; n <= field_height + 1; n++)
            for ( m = 0; m <= field_width + 1; m++) {
                a[n][m] = new node_t();
                a[n][m].visited = false;
                a[n][m].val = 1;
                a[n][m].px = 0;
                a[n][m].py = 0;
            }
                
                
        for ( n = 1; n <= field_height; n++){
            String ss = in.next();
            for ( m = 1; m <= field_width; m++) {
                field[n][m] = ss.charAt(m-1);
                
                if ( field[n][m] == '.' ){
                    a[n][m].val = 0;
                    field[n][m] = ' ';
                }
                if ( field[n][m] == '@') {
                    start.x = m;
                    start.y = n;
                    a[n][m].val = 2;
                }
                if ( field[n][m] == 'X') {
                    a[n][m].val = 3;
                }
                //System.out.print(field[n][m]);
            }
            //System.out.println();
        }
        
        field_update();        
        
        run_bfs(start);
    }

    private static void field_init() {
        int n, m;
        
        // fields
        for ( n = 1; n <= field_height; n++)
            for ( m = 1; m <= field_width; m++) 
                field[n][m] = ' ';
        
        // borders
        for ( n = 0; n <= field_height+1; n++) {
            field[n][0] = walls[5];
            field[n][field_width+1] = walls[5];
            field[n][field_width+2] = '\n';
        }
        for ( m = 1; m <= field_width; m++) {
            field[0][m] = walls[6];
            field[field_height+1][m] = walls[6];
        }
        
        // conners
        field[0][0] = walls[1];
        field[0][field_width+1] = walls[2];
        field[field_height+1][field_width+1] = walls[3];
        field[field_height+1][0] = walls[4];
    }

    private static void run_bfs( coord c ) {
        qh = 1;
        qt = 1;
        qy[qh] = c.y;
        qx[qh] = c.x;
        qh++;

        a[c.y][c.x].visited = true;
        
        int i;
        coord b;
        
        b = new coord();
        
        while ( qt != qh )
        {
            pop_q(b);
            
            for ( i = 0; i < 4; i++) {
                
                if ( push_q( b.y+ky[i], b.x+kx[i], b.y, b.x ) ){
                    qt = qh;
                    break;
                }
            }
            
            field_update();
        }
        
        b.x = a[qy[qh]][qx[qh]].px;
        b.y = a[qy[qh]][qx[qh]].py;
                
        while ( a[b.y][b.x].px != 0 && a[b.y][b.x].py != 0)
        {
            a[b.y][b.x].val = 0x42;
            b.y = a[b.y][b.x].py;
            b.x = a[b.y][b.x].px;
            field_update();
        }
        
     }

    private static void pop_q( coord b ) {
        b.y = qy[qt];
        b.x = qx[qt];
        qt ++;
    }

    private static boolean push_q(int by, int bx, int py, int px) {
        if ( a[by][bx].val     == 1    ) return false;
        if ( a[by][bx].visited == true ) return false;
        
        a[by][bx].px = px;
        a[by][bx].py = py;
        qx[qh] = bx;
        qy[qh] = by;

        if ( a[by][bx].val == 3 ) return true;
            
        a[by][bx].visited = true;
        qh++;
        
        return false;
    }
}
