/**
 * Algorithm.java
 * <p>
 * This is the Algorithm interface class. It contains only one member method, run(),
 * that simply starts to process the voting data and generates the election results.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */
public interface Algorithm {
    /**
     * This method is used to process the voting data. It is implemented differently by
     * differently algorithms.
     */
    void run();

    /**
     * This method prints the results of the election. It is implemented the same by
     * the two algorithms in this program.
     */
    void print();
}
