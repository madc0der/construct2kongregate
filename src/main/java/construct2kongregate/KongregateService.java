package construct2kongregate;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class KongregateService {

    private static final Logger log = Logger.getLogger(KongregateService.class);

    public static void pack(final File unpackedDir, final File targetZip) throws Exception {
        log.info("Zip directories...");
        packDirs(unpackedDir);
        log.info("Fixing index.html...");
        insertKongregateScript(unpackedDir);
        log.info("Zip to target Kongregate file...");
        ZipCompressionService.zip(unpackedDir, targetZip);
    }

    private static void packDirs(final File unpackedDir) throws Exception {
        for (final File dir : unpackedDir.listFiles(File::isDirectory)) {
            log.info("Zip dir: " + dir.getName());
            ZipCompressionService.zip(dir, new File(unpackedDir, dir.getName() + ".zip"));
        }
    }

    private static void insertKongregateScript(final File unpackedDir) throws IOException {
        final File indexFile = new File(unpackedDir, "index.html");
        final Document doc = Jsoup.parse(indexFile, "UTF-8");
        doc.select("head").first()
                .appendElement("script")
                .attr("src", "https://cdn1.kongregate.com/javascripts/kongregate_api.js");
        FileUtils.writeStringToFile(indexFile, doc.outerHtml(), "UTF-8");
    }
}
