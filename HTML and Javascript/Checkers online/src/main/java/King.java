public class King extends Piece {
    public Piece getBase() {
        return base;
    }

    public void setBase(Piece base) {
        this.base = base;
    }

    public Piece getHat() {
        return hat;
    }

    public void setHat(Piece hat) {
        this.hat = hat;
    }

    private Piece base, hat;

    public King(Piece p) {
        super(p.getId(), p.getX(), p.getY());
        base = p;
        hat = null;
    }

    public King(Piece base, Piece hat) {
        super(base.getId(), base.getX(), base.getY());
        this.base = base;
        this.hat = hat;
    }
}
