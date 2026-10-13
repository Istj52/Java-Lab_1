package complex;

public record Complex(double real, double im) {
    private static final double EPSILON = 1e-10;


    @Override
    public String toString() {
        if (isZero(real) && isZero(im)) {
            return "0";
        }
        else if (isZero(real)) {
            return formatIm(im);
        }
        else if (isZero(im)) {
            return format(real);
        }
        else {
            String sign = im > 0 ? "+" : "";
            return format(real) + sign + formatIm(im);
        }
    }

    private boolean isZero(double n) {
        return Math.abs(n) < EPSILON;
    }

    private static String formatIm(double n) {
        if (Math.abs(n + 1) < EPSILON || Math.abs(n - 1) < EPSILON) {
            return (n > 0) ? "i" : "-i";
        }
        String sign = n > 0 ? "" : "-";
        n = Math.abs(n);
        return sign + format(n) + "i";
    }
    private static String format(double n) {
        if (n == Math.floor(n)) {
            return (long) n + "";
        }
        return n + "";
    }
}
