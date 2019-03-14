package construct2kongregate;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressionService {

    private static final Logger log = Logger.getLogger(ZipCompressionService.class);

    public static void unzip(final File sourceZip, final File targetDir) throws Exception {

        if (!targetDir.mkdir()) {
            throw new RuntimeException("Cannot create tmp dir to extract package");
        }

        try (final ArchiveInputStream inputStream = new ZipArchiveInputStream(
                new FileInputStream(sourceZip),
                null,
                false,
                true)) {

            ArchiveEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (!inputStream.canReadEntryData(entry)) {
                    log.info("Skipping " + entry.getName());
                    continue;
                }
                log.info("Extracting " + entry.getName());

                final File entryFile = new File(targetDir, entry.getName());
                if (entry.isDirectory()) {
                    if (!entryFile.isDirectory() && !entryFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + entryFile);
                    }
                } else {
                    final File parent = entryFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    try (OutputStream outputStream = Files.newOutputStream(entryFile.toPath())) {
                        IOUtils.copy(inputStream, outputStream);
                    }
                }
            }
        }
    }

    public static void zip(final File sourceDir, final File targetZip) throws IOException {
        final Path p = Files.createFile(Paths.get(targetZip.getAbsolutePath()));
        try (final ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            final Path pp = Paths.get(sourceDir.getAbsolutePath());
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        final ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            log.info("Adding " + path);
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (final IOException exc) {
                            log.error("Failed to zip " + zipEntry.getName(), exc);
                        }
                    });
        }
    }
}
