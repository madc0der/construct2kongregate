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

        final File sourceArchive = new File("Roof_Garbage.zip");
        final File targetArchive = new File("kongregate-" + System.currentTimeMillis() + ".zip");

        final File unpackedDir = ConstructService.unpack(sourceArchive);
        KongregateService.pack(unpackedDir, targetArchive);
        log.info("Cleaning up temporary files...");
        FileUtils.deleteDirectory(unpackedDir);
        log.info("All done!");
    }
}
