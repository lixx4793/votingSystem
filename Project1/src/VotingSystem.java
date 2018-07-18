import java.util.*;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.File;

/**
 * VotingSystem.java
 * <p>
 * This is the VotingSystem object class. It is the main driver class for this program. It
 * firstly reads user input, initializes the database, runs algorithm to process data, and
 * generates output and audit report files. It contains 4 static attributes, 3 member functions
 * that handle file I/Os and a main.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */

public class VotingSystem {
    private Database database;  // Database associates with this election
    private String filename;  // file name of the input file
    private final String OUTPUT = "output.txt";  // String of the output file name
    private final String REPORT = "audit_report.txt";  // String of the audit report file name

    /**
     * The main method that takes 1 input parameter , which is the file name that contains the voting
     * data. In this method, it asks and reads user input parameters, parses information from the csv
     * files, updates the database it associates with, runs algorithms, writes output and audit report
     * files and generates correct election results. Possible exceptions are handled.
     *
     * @param args user input csv file name
     */
    public static void main(String[] args) {
        // initiate a new voting system
        VotingSystem vs = new VotingSystem();

        // valid input argument
        if(args.length == 0) {
            System.out.println("ERROR: Input file is required.");
            System.exit(-1);
        }
        else if(args.length != 1) {
            System.out.println("ERROR: Please only enter 1 input file.");
            System.exit(-1);
        }
        vs.filename = args[0];

        // start to read user input parameters, and catch handle exceptions
        Scanner scan = new Scanner(System.in);
        try {
            System.out.print("Do you want to run this program in Test Mode (Yes [1] or No [0]): ");
            int mode = scan.nextInt();

            if(mode != 0 && mode != 1) {
                System.out.println("ERROR: Invalid mode.");
                System.exit(-1);
            }

            System.out.print("Please enter number of candidate(s): ");
            int candidates = scan.nextInt();
            if(candidates <= 0) {
                System.out.println("ERROR: Invalid input. Number of candidate(s) must a positive number.");
                System.exit(-1);
            }

            System.out.print("Please enter number of winner(s): ");
            int winner = scan.nextInt();
            if(winner <= 0) {
                System.out.println("ERROR: Invalid input. Number of winner(s) must a positive number.");
                System.exit(-1);
            } else if(candidates < winner) {
                System.out.println("ERROR: Number of winner(s) is greater than number of candidate(s).");
                System.exit(-1);
            }

            System.out.print("Please enter number of voter(s): ");
            int voters = scan.nextInt();
            if(voters <= 0) {
                System.out.println("ERROR: Invalid input. Number of voter(s) must a positive number.");
                System.exit(-1);
            }

            System.out.print("Please choose algorithm (Plurality [0] or Droop Quota [1]): ");
            int alg = scan.nextInt();
            if(alg != 0 && alg != 1) {
                System.out.println("ERROR: Invalid algorithm.");
                System.exit(-1);
            }

            vs.database = new Database(winner, candidates, voters, alg, mode);
        } catch(Exception e) {
            System.out.println("ERROR: Invalid input. Input must be an integer.");
            System.exit(-1);
        }
        scan.close();

        // Read input CSV file and write to database
        try {
            vs.read_file();
        } catch (Exception e ) {
            System.out.println("ERROR: Fail to read input file. " + e);
            System.exit(-1);
        }

        // Validate user input data
        if(vs.database.get_num_candidates() != vs.database.candidate_list.size()) {
            vs.database.set_num_candidates(vs.database.candidate_list.size());
        }
        if(vs.database.get_num_voters() != vs.database.votes.size()) {
            vs.database.set_num_voters(vs.database.votes.size());
        }
        
        // Run algorithm
        if(vs.database.get_algorithm() == 0) {
            Algorithm alg = new Plurality(vs.database);
            alg.run();
        } else if(vs.database.get_algorithm() == 1) {
            Algorithm alg = new DroopQuota(vs.database);
            alg.run();
        }

        // Write output file
        try {
            vs.write_output();
        } catch (Exception e) {
            System.out.println("ERROR: Fail to write input file. " + e);
            System.exit(-1);
        }

        // Write audit report
        try {
            vs.write_report();
        } catch (Exception e) {
            System.out.println("ERROR: Fail to write audit report. " + e);
            System.exit(-1);
        }
    }

    /**
     * Reads the user input csv file that contains the voting data, and updates
     * the database structure, so this database structure that will be passed is
     * ready for the algorithm to be accessed.
     *
     * @throws Exception On file reading errors
     */
    /* reads the input file and writes data to database */
    public void read_file() throws Exception {
        File file = new File("./" + filename);
        Scanner scan = new Scanner(file);
        String[] candidates  = scan.nextLine().split(","); // reads the first line that contains candidates names
        
        for(int i = 0; i < candidates.length; i++) {
            database.candidate_list.add(new Candidate(i, candidates[i]));
        }
        
        int voter_id = 0;
        while(scan.hasNextLine()) {
            String[] line = scan.nextLine().split(",");

            PriorityQueue<Vote> queue = new PriorityQueue<>(new VoteComparator());
            for(int i = 0; i < line.length; i++) {
                if(line[i].equals("")) {
                    continue;
                }
                queue.add(new Vote(i, Integer.parseInt(line[i])));
            }

            database.votes.put(voter_id, queue);
            voter_id++;
        }
        scan.close();
    }

    /**
     * Retrieves data from the database and writs the output file that contains major
     * election information and winner of the election
     *
     * @throws Exception On file writing errors
     */
    /* wirtes the output txt file */
    public void write_output() throws Exception {
        PrintWriter writer = new PrintWriter(OUTPUT, "UTF-8");
        writer.println(" ======= Election Information ======= ");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        writer.println(" Date: " + formatter.format(date));

        String algo = (database.get_algorithm() == 1 ? "Droop Quota" : "Plurality");
        writer.println(" Algorithm used: " + algo);
        writer.println(" Candidates : " + database.get_num_candidates());
        writer.println(" Seats to Fill: " + database.get_num_winners());
        writer.println(" Voters Participated: " + database.get_num_voters());
        writer.println();
        writer.println();

        writer.println(" ======= Election Result ======= ");
        writer.print(" Winner(s) of Election: ");
        Iterator itr = database.winners.listIterator();
        while(itr.hasNext()) {
            writer.print(((Candidate)itr.next()).get_name() +  " ");
        }
        writer.close();
    }

    /**
     * Retrieves data from the database and writes the audit report file that
     * contains major election information and votes received by each candidate.
     *
     * @throws Exception On file writing errors
     */
    /* writes the audit report txt file */
    public void write_report() throws Exception{
        PrintWriter writer = new PrintWriter(REPORT, "UTF-8");
        writer.println(" ======= Audit Report ======= ");
        writer.println();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        writer.println(" This report was generated on: " + formatter.format(date));

        String algo = (database.get_algorithm() == 1 ? "Droop Quota" : "Plurality");
        writer.println(" Algorithm used: " + algo);
        writer.println(" Candidates : " + database.get_num_candidates());
        writer.println(" Seats to Fill: " + database.get_num_winners());
        writer.println(" Voters Participated: " + database.get_num_voters());
        writer.println();
        writer.println();

        writer.println("Candidate: id of voter(s) who has voted for this candidate");

        Iterator i = database.candidate_list.listIterator();
        while(i.hasNext()) {
            Candidate c = (Candidate) i.next();

            writer.print(" " + c.get_name() + ": ");
            Iterator itr = c.get_voter_id_list().listIterator();
            while(itr.hasNext()) {
                writer.print(itr.next() + " ");
            }
            writer.println();
        }
        writer.close();
    }

    /* The following getter and setter methods are not used in the main. They are mainly for the purpose of testing */
    /**
     * Gets the Database associates with the voting system
     *
     * @return the database
     */
    public Database get_database() {
        return database;
    }

    /**
     * Gets the file name of the user input csv file
     *
     * @return the file name
     */
    public String get_filename() {
        return filename;
    }

    /**
     * Gets the static output file name
     *
     * @return static output file name
     */
    public String get_output() {
        return OUTPUT;
    }

    /**
     * Gets the static audit report file name
     *
     * @return static audit report file name
     */
    public String get_report() {
        return REPORT;
    }

    /**
     * Sets the Database of this voting system to a input database
     *
     * @param database input database
     */
    public void set_database(Database database) {
        this.database = database;
    }

    /**
     * Sets the user input csv file name of this voting system to a input file name
     *
     * @param filename input string of file name
     */
    public void set_filename(String filename) {
        this.filename = filename;
    }
}
