package eu.rbecker.jsepa.maven;

/*
 * The MIT License
 *
 * Copyright 2017 Robert Becker <robert at rbecker.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import eu.rbecker.jsepa.information.GermanBankInformationProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class BankDataFileUpdater implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DE_BANK_DATA_HOST = "https://www.bundesbank.de";

    private static final String DE_BANK_DATA_INDEX_URL = DE_BANK_DATA_HOST + "/Redaktion/DE/Standardartikel/Aufgaben/Unbarer_Zahlungsverkehr/bankleitzahlen_download.html";

    /**
     * Should match paths like
     * /Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/2017_03_05/blz_2016_12_05_txt.txt?__blob=publicationFile
     * or
     * /Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/2017_03_05/blz_2016_12_05_txt.txt;jsessionid=0000BPKSIVwMwqnw42YIVTLXJlY:-1?__blob=publicationFile
     * but not
     * /Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/2017_03_05/blz_2016_12_05_xls.xlsx?__blob=publicationFile
     * and not
     * /Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/2017_03_05/blz_loeschungen_2016_12_05.pdf?__blob=publicationFile.
     *
     * Only first occurance is relevant, i. e. The following may be present, too, together with the first string.
     * /Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/2016_12_04/blz_2016_09_05_txt.txt?__blob=publicationFile
     */
    private static final Pattern DE_BANK_FILE_REGEXP = Pattern.compile("href=\"([^\"]*/blz_\\d{4}_\\d{2}_\\d{2}_txt\\.txt(;jsessionid=[^\\?]*)?\\?__blob=publicationFile)\"");

    private static final long FILE_MAX_AGE_MS = 1000 * 60 * 60 * 24 * 7; // 1 week

    private final String targetDirectory;

    private static final Logger LOG = Logger.getLogger(BankDataFileUpdater.class.getName());

    public static void main(String... args) throws Exception {
        new BankDataFileUpdater(args[0]).checkAndUpdateFiles();
    }

    public BankDataFileUpdater(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void checkAndUpdateFiles() throws Exception {
        LOG.info("Checking bank data files...");
        checkGermany();
    }

    private void checkGermany() throws MalformedURLException, IOException {
        String filePath = targetDirectory + File.separator + GermanBankInformationProvider.BANK_DATA_FILE_NAME;
        File f = new File(filePath);
        if (!fileNeedsUpdate(f)) {
            LOG.log(Level.INFO, "{0} is up to date.", filePath);
            return;
        }
        LOG.log(Level.INFO, "{0} is outdated or missing - fetching new...", filePath);
        String url = resolveGermanBankDataFileUrl();
        String content = fetchUrl(url);
        if (f.exists()) {
            f.delete();
        }
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        System.out.println("Done.");
    }

    private String resolveGermanBankDataFileUrl() throws IOException, RuntimeException {
        String content = fetchUrl(DE_BANK_DATA_INDEX_URL);
        Matcher m = DE_BANK_FILE_REGEXP.matcher(content);
        if (!m.find()) {
            LOG.info(content);
            throw new RuntimeException("Could not extract bank data file url from " + DE_BANK_DATA_INDEX_URL);
        }
        return DE_BANK_DATA_HOST + m.group(1);
    }

    private String fetchUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        String result;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.ISO_8859_1))) {
            result = reader
                .lines()
                .parallel()
                .collect(Collectors.joining("\n"));
        }
        return result;
    }

    private static boolean fileNeedsUpdate(File f) {
        return !f.exists() || System.currentTimeMillis() - f.lastModified() > FILE_MAX_AGE_MS;
    }

}
