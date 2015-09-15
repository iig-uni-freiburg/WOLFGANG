package de.uni.freiburg.iig.telematik.wolfgang.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * <p>
 * Utilities class to read information of Wolfgang's latest release on GitHub.
 * </p>
 *
 * @version 1.0
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public class ReleaseUtils {

    public final static String API_URL = "https://api.github.com/repos/%s/%s/releases";

    /**
     * DateFormat of the ISO date
     */
    public final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private final String userName;
    private final String repositoryName;

    private Date latestDate = null;
    private JsonObject latestObj = null;

    /**
     * Creates a new utilities class object for the given repository.
     *
     * @param userName GitHub user name for the repository, e.g.
     * <i>iig-uni-freiburg</i>.
     * @param repositoryName GitHub repository name, e.g. <i>WOLFGANG</i>.
     */
    public ReleaseUtils(String userName, String repositoryName) {
        this.userName = userName;
        this.repositoryName = repositoryName;
    }

    /**
     * Returns the version tag of the last drafted release.
     *
     * @return Version tag.
     */
    public String getLatestVersion() {
        if (latestObj == null) {
            try {
                getLatestReleaseObject();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return latestObj.get("tag_name").getAsString();
    }

    /**
     * Returns the assets of the last drafted release.
     *
     * @return Assets.
     */
    public Collection<Map<String, String>> getLatestVersionAssets() {
        if (latestObj == null) {
            try {
                getLatestReleaseObject();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        Collection<Map<String, String>> assets = new HashSet<>();

        JsonArray assetArray = latestObj.get("assets").getAsJsonArray();
        for (JsonElement assetElement : assetArray) {
            JsonObject assetObj = assetElement.getAsJsonObject();
            Map<String, String> entry = new HashMap<>();

            entry.put("content_type", assetObj.get("content_type").getAsString());
            entry.put("downloads", assetObj.get("download_count").getAsString());
            entry.put("id", assetObj.get("id").getAsString());
            entry.put("name", assetObj.get("name").getAsString());
            entry.put("url", assetObj.get("browser_download_url").getAsString());

            assets.add(entry);
        }

        return assets;
    }

    /**
     * Returns the date of creation of the last drafted release.
     *
     * @return Date of creation.
     */
    public Date getLatestVersionDate() {
        if (latestDate == null) {
            try {
                getLatestReleaseObject();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return latestDate;
    }

    /**
     * Returns the name of the last drafted release.
     *
     * @return Version name.
     */
    public String getLatestVersionName() {
        if (latestObj == null) {
            try {
                getLatestReleaseObject();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return latestObj.get("name").getAsString();
    }

    /**
     * Returns the URL of the last drafted release.
     *
     * @return Version URL.
     */
    public URL getLatestVersionURL() {
        if (latestObj == null) {
            try {
                getLatestReleaseObject();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            return new URL(latestObj.get("html_url").getAsString());
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void getLatestReleaseObject() throws MalformedURLException, IOException, ParseException {
        URL url = new URL(String.format(API_URL, userName, repositoryName));
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonArray rootarray = root.getAsJsonArray();

        for (JsonElement e : rootarray) {
            if (e.isJsonObject()) {
                JsonObject obj = e.getAsJsonObject();
                String createdAt = obj.get("created_at").getAsString();
                boolean prerelease = Boolean.valueOf(obj.get("prerelease").getAsString());
                Date result = DATE_FORMAT.parse(createdAt);
                if (latestDate == null || (result.compareTo(latestDate) > 0 && !prerelease)) {
                    latestDate = result;
                    latestObj = obj;
                }
            }
        }
    }
}
