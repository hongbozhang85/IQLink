package comp1110.ass2;

/**
 * Created by Shijie on 2016/8/31.
 */
public enum  Unit {
    BALL1("B",1), BALL2("B",2), RING0("R",0), RING1("R",1), RING2("R",2);

    String type;
    int open;

    /**
     * By Shijie
     * constructor
     * @param type
     * @param open
     */
    Unit(String type, int open){
        this.type = type;
        this.open = open;
    }
}