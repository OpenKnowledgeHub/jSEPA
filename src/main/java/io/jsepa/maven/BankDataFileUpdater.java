/*
 * The MIT License (MIT)
 *
 * Copyright © 2024 jsepa.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.jsepa.maven;

import io.jsepa.information.GermanBankInformationProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BankDataFileUpdater implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final String DE_BANK_DATA_HOST = "https://www.bundesbank.de";

    private static final String DE_BANK_DATA_INDEX_URL = DE_BANK_DATA_HOST + "/de/aufgaben/unbarer-zahlungsverkehr/serviceangebot/bankleitzahlen/download---bankleitzahlen-602592";

    private static final Pattern DE_BANK_FILE_REGEXP = Pattern.compile("href=\"(/resource/blob/[0-9]+/[a-z0-9]+/mL/blz-aktuell-txt-data.txt)\"");
    
    private static final long FILE_MAX_AGE_MS = 1000 * 60 * 60 * 24 * 7; // 1 week
    private static final Logger LOG = Logger.getLogger(BankDataFileUpdater.class.getName());
    private final String targetDirectory;

    public BankDataFileUpdater(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public static void main(String... args) throws Exception {
        new BankDataFileUpdater(args[0]).checkAndUpdateFiles();
    }

    private static boolean fileNeedsUpdate(File f) {
        return !f.exists() || System.currentTimeMillis() - f.lastModified() > FILE_MAX_AGE_MS;
    }

    public void checkAndUpdateFiles() throws Exception {
        LOG.info("Checking bank data files...");
        boolean updateRequired = !checkGermany();
        if (updateRequired) {
            System.out.println("############################################################");
            System.out.println("New files have been added to the classpath - Please restart your build process!");
            System.out.println("############################################################");
            System.exit(1);
        }
    }

    private boolean checkGermany() throws IOException {
        String filePath = targetDirectory + File.separator + GermanBankInformationProvider.BANK_DATA_FILE_NAME;
        File f = new File(filePath);
        if (!fileNeedsUpdate(f)) {
            LOG.log(Level.INFO, "{0} is up to date.", filePath);
            return true;
        }
        String url = resolveGermanBankDataFileUrl();
        LOG.log(Level.INFO, "{0} is outdated or missing - fetching new version from {1}", new String[]{filePath, url});
        fetchUrlToFile(url, filePath);
        System.out.println("Done.");
        return false;
    }

    private void fetchUrlToFile(String url, String filePath) throws IOException {
        String content = fetchUrl(url);
        Path file = Paths.get(filePath);
        Files.createDirectories(file.getParent());
        Files.write(file, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
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

    private String fetchUrl(String urlString) throws IOException {
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

}
