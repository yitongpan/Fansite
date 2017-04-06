import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

import fansite.Fansite;


/**
 * Created by Ellie on 4/1/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String input = args[0];
        String hostFn = args[1];
        String resourceFn = args[2];
        String hoursFn = args[3];
        String blockFn = args[4];
        Boolean overlapHours = Boolean.parseBoolean(args[5]);

        BufferedReader in = new BufferedReader(new FileReader(input));
//        BufferedReader in = new BufferedReader(new FileReader("log1.txt"));
        Fansite fanSite = new Fansite(10, 10, 10, overlapHours);

        try {
            String line = "";
            while ((line = in.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    fanSite.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        } catch (IOException e) {
            System.out.println("Null Exception");
            return;
        }

        if (overlapHours) {
            fanSite.getHours().finalize();
        }



        in.close();

        PrintStream output;
        output = new PrintStream(hostFn);
//        output = new PrintStream("1.txt");
        StringJoiner hosts = new StringJoiner("\n");
        output.print(fanSite.getMaxiHost().printWithValue(","));

//        output = new PrintStream(resourceFn);
        output = new PrintStream("2.txt");
        output.print(fanSite.getMaxiResource().printName());

//        output = new PrintStream(hoursFn);
        output = new PrintStream("3.txt");
        output.print(fanSite.getHours().print(","));

        output = new PrintStream(blockFn);
//        output = new PrintStream("4.txt");
        String block = "";
        for (String b : fanSite.getBlockRecord()) {
            block = block + b + "\n";
        }
        output.print(block);
    }


}
