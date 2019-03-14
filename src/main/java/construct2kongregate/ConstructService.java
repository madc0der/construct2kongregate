package construct2kongregate;

import org.apache.log4j.Logger;

import java.io.File;

public class ConstructService {

    private static final Logger log = Logger.getLogger(ConstructService.class);

    public static File unpack(final File constructPackage) throws Exception {
        final File destDir = new File(new File(System.getProperty("java.io.tmpdir")), "construct2kongregate-" + System.currentTimeMillis());
        log.info("Unzipping Construct package to " + destDir.getAbsolutePath() + "...");
        ZipCompressionService.unzip(constructPackage, destDir);
        return destDir;
    }
}
