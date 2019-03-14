package construct2kongregate;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(final String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: construct2kongregate.jar <construct.zip> <kongregate.zip>");
            return;
        }

        final File sourceArchive = new File(args[0]);
        final File targetArchive = new File(args[1]);
        if (!sourceArchive.exists() || !sourceArchive.isFile()) {
            System.out.println(args[0] + " must be existing zip file");
            return;
        }
        if (targetArchive.exists()) {
            System.out.println(args[1] + " is already exists");
            return;
        }

        final File unpackedDir = ConstructService.unpack(sourceArchive);
        KongregateService.pack(unpackedDir, targetArchive);
        log.info("Cleaning up temporary files...");
        FileUtils.deleteDirectory(unpackedDir);
        log.info("All done!");
    }
}
