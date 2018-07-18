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
 *
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.1
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
		// initiate a scanner to parse user input
		Scanner scan = new Scanner(System.in);
    // initialize database
    vs.database = new Database();

		// valid input argument
		if (args.length == 0) {
			System.out.println("Please enter the file name: ");
			vs.filename = scan.nextLine();
		} else if (args.length == 1) {
			// the argument could be either the developer flag or the file name
      if(args[0].equals("d")) {
        vs.database.set_shuffle(false);
        System.out.println("Developer mode: shuffle option is off.");
        System.out.println("Please enter the file name: ");
        vs.filename = scan.nextLine();
      } else {
        vs.filename = args[0];
      }
		} else if (args.length == 2) {
		  if(args[0].equals("d")) {
		    vs.database.set_shuffle(false);
		    vs.filename = args[1];
      } else {
		    System.out.println("ERROR: Invalid first argument!");
		    System.exit(-1);
      }
    } else {
			System.out.println("ERROR: Invalid number of argument.");
			System.exit(-1);
		}
		scan.close();

		// Read input CSV file and write to database
		try {
			vs.read_file();
		} catch (Exception e) {
			System.out.println("ERROR: Fail to read input file. " + e);
			System.exit(-1);
		}

		// Run algorithm
		if (vs.database.get_algorithm() == 0) {
			Algorithm alg = new Plurality(vs.database);
			alg.run();
		} else if (vs.database.get_algorithm() == 1) {
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
		if (file.exists() && !file.isDirectory()) {
			Scanner scan = new Scanner(file);
			try {
				// Parse number of winners and algorithm information for using input csv file
				int num_winners = Integer.parseInt(scan.nextLine().split(",")[1]);
				int alg = Integer.parseInt(scan.nextLine().split(",")[1]);
				if(alg != 0 && alg != 1) {
					System.out.println("ERROR: Invalid input algorithm value.");
					System.exit(-1);
				}

				// Update database
				database.set_num_winners(num_winners);
				database.set_algorithm(alg);
				if(alg == 0) {
				  database.set_shuffle(false);
        }
			} catch (Exception e) {
				System.out.println("ERROR: Fail to parse number of winners or algorithm information from input csv file.");
				System.exit(-1);
			}

			String[] candidates = scan.nextLine().split(","); // reads the first line that contains candidates names

			// Add candidate to the database
			for (int i = 0; i < candidates.length; i++) {
				database.candidate_list.add(new Candidate(i, candidates[i]));
			}

			int voter_id = 0;
      while (scan.hasNextLine()) {
        String[] line = scan.nextLine().split(",");

        PriorityQueue<Vote> queue = new PriorityQueue<>(new VoteComparator());
        for (int i = 0; i < line.length; i++) {
          if (line[i].equals("")) {
            continue;
          }
          queue.add(new Vote(i, Integer.parseInt(line[i])));
        }

        database.votes.put(voter_id, queue);
        voter_id++;
      }
			scan.close();

			// Update database
			database.set_num_voters(database.votes.size());
			database.set_num_candidates(database.candidate_list.size());
			if(database.get_num_winners() > database.get_num_candidates()) {
				System.out.println("ERROR: Number of winner is greater than number of candidates.");
				System.exit(-1);
			}
		} else {
			System.out.println("ERROR: File does not exist.");
			System.exit(-1);
		}
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
		writer.print(" Candidates : ");
		Iterator i = database.candidate_list.listIterator();
		while(i.hasNext()) {
		  Candidate c = (Candidate) i.next();
		  writer.print(c.get_name() + " ");
    }
    writer.println();
		writer.println(" Seats to Fill: " + database.get_num_winners());
		writer.println(" Voters Participated: " + database.get_num_voters());
		writer.println();
		writer.println();

		writer.println(" ======= Election Result ======= ");
		writer.print(" Winner(s) of Election: ");
		Iterator itr = database.winners.listIterator();
		while (itr.hasNext()) {
			writer.print(((Candidate) itr.next()).get_name() + " ");
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
	public void write_report() throws Exception {
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

		writer.println(" Full list of voters (ids) that participated in the election");
		writer.println(" NOTE: The voters are displayed in the order as the their ballot got distributed.");
    Iterator it = database.shuffled_list.listIterator();
    while(it.hasNext()) {
      writer.print(" "  + it.next() + " ");
    }
    writer.println();
    writer.println();

		if(get_database().get_algorithm() == 1) {
      writer.println(" Candidate: id of voter(s) who has voted for this candidate");
      writer.println(" NOTE: vote from voters with '*' has been re-distributed.");
      Iterator i = database.candidate_list.listIterator();
      while (i.hasNext()) {
        Candidate c = (Candidate) i.next();

        writer.print(" " + c.get_name() + ": ");
        while (!c.get_voter_id_list().isEmpty()) {
          writer.print(c.get_voter_id_list().poll() + " ");
        }
        if(!c.get_dis_voter_id_list().isEmpty()) {
          Iterator itr = c.get_dis_voter_id_list().listIterator();
          while(itr.hasNext()) {
            writer.print("*" + itr.next() + " ");
          }
        }
        writer.println();
      }
      writer.println();
      writer.println();

      writer.print(" Invalidated voter(s) id: ");
      i = database.invalidated_voter_list.listIterator();
      while (i.hasNext()) {
        writer.print(i.next() + " ");
      }
    } else {
      writer.println(" Candidate: id of voter(s) who has voted for this candidate");
      Iterator i = database.candidate_list.listIterator();
      while (i.hasNext()) {
        Candidate c = (Candidate) i.next();

        writer.print(" " + c.get_name() + ": ");
        while (!c.get_voter_id_list().isEmpty()) {
          writer.print(c.get_voter_id_list().poll() + " ");
        }
        writer.println();
      }
      writer.println();
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
