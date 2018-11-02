/* Andrew Yaros
 * CS 361 Assignment 1
 * Part 2
 * MULTITHREADED VERSION
 *//* Mandelbrot thread class */

public class mandelbrot extends Thread {
    private final double xlo, xhi, ylo, yhi;
    private final int index, wdt, hgt, threshold;

    mandelbrot(String NAME, int INDEX, double XLO, double XHI,
               double YLO, double YHI, int WDT, int HGT, int THRESHOLD) {
        super(NAME); this.index = INDEX;
        this.xlo = XLO; this.xhi = XHI;
        this.ylo = YLO; this.yhi = YHI;
        this.wdt = WDT; this.hgt = HGT;
        this.threshold = THRESHOLD;
    }

    /* adapted from provided example maple code */
    private static int compute(double xc, double yc, int threshold) {
        double x = 0.0; double y = 0.0;
        double xt, yt, xx, yy; int i;
        for(i = 0; i < threshold; i++) {
            xx = x*x; yy = y*y;
            if(xx + yy < 2) {
                xt = xx - yy + xc; //temp x
                yt = (2*x*y) + yc; //temp y
                x = xt; y = yt;
            } else return i;
        } return i;
    }

    /* adapted from provided example maple code */
    public void run() {
        //local vars
        double pixelArray[][][];
        double xc,yc, iters, color, segment, temp;
        int x, y;

        //array for monochrome pixels, and for rgb values
        //so I don't have two separate arrays
        //index 0 is for mono; indices 1-3 are for RGB
        pixelArray = new double[this.wdt][this.hgt][4];

        //constants
        final double THREE = 3.0;
        final double ONE_THIRD = 1.0/THREE;
        final double TWO_THIRDS = 2.0/THREE;
        final double MIN_COLOR_VALUE = 0.1;
        final double WHITE = 1.0;
        final double BLACK = 0.0;

        //to avoid extra operations/casting
        double xdif = this.xhi - this.xlo;
        double ydif = this.yhi - this.ylo;
        double width = (double) this.wdt;
        double height = (double) this.hgt;
        double thrsh = (double) this.threshold;

        for(int w = 0; w < this.wdt; w++) { //width
            for(int h = 0; h < this.hgt; h++) { //height
                x = w; y = h;

                xc = this.xlo + (xdif * ((double) x / width));
                yc = this.ylo + (ydif * ((double) y / height));
                iters = compute(xc, yc, this.threshold);

                //1-bit color
                if (iters < this.threshold) color = BLACK;
                else color = WHITE;
                pixelArray[x][y][0] = color;

                //calculate RGB color values
                segment = iters / thrsh;
                if (segment <= ONE_THIRD) {
                    temp = segment * THREE;
                    if(temp > MIN_COLOR_VALUE)
                         pixelArray[x][y][1] = temp;
                    else pixelArray[x][y][1] = MIN_COLOR_VALUE;
                } else if (segment <= TWO_THIRDS) {
                    temp = (segment - ONE_THIRD) * THREE;
                    if(temp > MIN_COLOR_VALUE)
                         pixelArray[x][y][2] = temp;
                    else pixelArray[x][y][2] = MIN_COLOR_VALUE;
                } else {
                    temp = (segment - TWO_THIRDS) * THREE;
                    if(temp > MIN_COLOR_VALUE)
                         pixelArray[x][y][3] = temp;
                    else pixelArray[x][y][3] = MIN_COLOR_VALUE;
                }
            }
        }

        //Move data to static variable in main
        Main.results[this.index] = pixelArray;
    }
}
