package complex;

public final class ComplexMath
{
    private static final double EPSILON = 1e-10;

    private ComplexMath() {}

    public static Complex add(Complex a, Complex b)
    {
        return new Complex(a.real() + b.real(), a.im() + b.im());
    }
    public static Complex sub(Complex a, Complex b)
    {
        return new Complex(a.real() - b.real(), a.im() - b.im());
    }
    public static Complex mul(Complex a, Complex b) {
        double real = a.real() * b.real() - a.im() * b.im();
        double im = a.real() * b.im() + a.im() * b.real();
        return new Complex(real, im);
    }
    public static Complex div(Complex a, Complex b) {
        double bSquare = b.real()*b.real() + b.im()*b.im();
        if (bSquare < EPSILON * EPSILON) {
            throw new IllegalArgumentException("Деление на ноль");
        }
        return new Complex((a.real()*b.real() + a.im()*b.im()) / bSquare, (a.im()*b.real() - a.real()*b.im()) / bSquare);
    }
    public static double abs(Complex a) {
        return Math.hypot(a.real(), a.im());
    }
}