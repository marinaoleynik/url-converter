package it.discovery.marina;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.Map.Entry;

public class UrlConverter implements Serializable {

    private static final String URLS_FILE_NAME = "urlsdb.out";
    private static final String PROTOCOL_AND_DOMAIN = "https://gen.ly/";
    private static final String ERROR_URL_NOT_FOUND = "URL not found";
    private static final String ERROR_DOMAIN_NOT_SUPPORTED = "This domain isn't supported";
    private static final String ERROR_WRONG_URL = "Wrong URL";

    //have to be changed for regexp
    private static final int MIN_URL_LENGTH = 6;
    //have to be changed for regexp
    private static final String MIN_URL_FORMAT = "://";


    private HashMap<String, String> mapOfUrls = new HashMap<>();

    public static void main(String[] args)
    {

        String currentUrl = "https://google.com/456454545454545456";
        String currentUrl2 = "https://google.com/456454000008884545456";
        String currentUrl3 = "htt54000008884545456";
        String currentUrl4 = "https://google.4000008884545456";
        UrlConverter urlConverter = new UrlConverter();

        urlConverter.printMap();
        String newUrl = urlConverter.convertUrl(currentUrl);
        System.out.println(newUrl);

        String newUrl2 = urlConverter.convertUrl(currentUrl2);
        System.out.println(newUrl2);

        String newUrl3 = urlConverter.convertUrl(currentUrl3);
        System.out.println(newUrl3);
        String newUrl4 = urlConverter.convertUrl(currentUrl4);
        System.out.println(newUrl4);
        urlConverter.convertUrl(null);
        urlConverter.convertUrl("");

        urlConverter.printMap();



        System.out.println(newUrl+" "+ urlConverter.getFullUrl(newUrl));
        System.out.println(newUrl2+" "+ urlConverter.getFullUrl(newUrl2));

        String fakeUrl = "https://gena/fsdf";
        System.out.println(fakeUrl+" "+ urlConverter.getFullUrl(fakeUrl));

        String fakeUrl2 = "https://gen.ly/fsdf";
        System.out.println(fakeUrl2+" "+ urlConverter.getFullUrl(fakeUrl2));

    }

    public String getFullUrl(String shortUrl)
    {
        boolean isOurDomain = checkDomain(shortUrl);
        if(isOurDomain == false)
        {
            return ERROR_DOMAIN_NOT_SUPPORTED;
        }

        loadUrlsFromFile();
        String key = getKeyFromUrl(shortUrl);
        return mapOfUrls.getOrDefault(key, ERROR_URL_NOT_FOUND);
    }

    private boolean checkDomain(String shortUrl)
    {
        return shortUrl.startsWith(PROTOCOL_AND_DOMAIN);
    }

    private String getKeyFromUrl(String shortUrl)
    {
        int index = shortUrl.lastIndexOf('/');
        if(index == -1) return shortUrl;

        return shortUrl.substring(index+1);

    }

    private void printMap()
    {
        System.out.println(mapOfUrls);
    }
    private static String generateNewKey()
    {
        Random rand = new Random();
        return Long.toString(Instant.now().getEpochSecond(), Character.MAX_RADIX)
                + Integer.toString(rand.nextInt(Character.MAX_RADIX), Character.MAX_RADIX);
    }
    public String convertUrl(String longUrl)
    {
        boolean isUrl = validateUrl(longUrl);
        if(isUrl == false)
        {
            return ERROR_WRONG_URL;
        }

        loadUrlsFromFile();

        String oldKey = checkUrlAlreadyAtMap(longUrl);
        if(oldKey.length()> 0){
            return makeNewUrlFromKey(oldKey);
        }


        String newKey = addUrlToMap(longUrl);
        return makeNewUrlFromKey(newKey);
    }

    private boolean validateUrl(String longUrl)
    {
        if(longUrl == null) {
            return false;
        }

        if(longUrl.length() < MIN_URL_LENGTH) //have to be changed for regexp using
        {
            return false;
        }
        if(longUrl.contains(MIN_URL_FORMAT) == false) //have to be changed for regexp using
        {
            return false;
        }

        return true;
    }

    private String makeNewUrlFromKey(String newKey)
    {
        return PROTOCOL_AND_DOMAIN + newKey;
    }

    private String checkUrlAlreadyAtMap(String longUrl)
    {
        boolean exists = mapOfUrls.containsValue(longUrl);
        if(exists)
        {
            Set<Entry<String, String>> entries = mapOfUrls.entrySet();
            for(Entry<String, String> entry : entries)
            {
                if(entry.getValue().equals(longUrl))
                {
                    return entry.getKey();
                }
            }
        }

        return "";

    }

    private String addUrlToMap(String longUrl)
    {
        String newKey = generateNewKey();
        boolean exists = mapOfUrls.containsKey(newKey);
        if(exists)
        {
            System.out.println("logging: recursion detected");
            return addUrlToMap(longUrl);
        }
        else
        {
            mapOfUrls.put(newKey, longUrl);
            saveUrlsToFile();
            return newKey;
        }
    }

    private void saveUrlsToFile()
    {
        try {
            ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(URLS_FILE_NAME));
            out.writeObject(mapOfUrls);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadUrlsFromFile()
    {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(URLS_FILE_NAME));
            mapOfUrls = (HashMap<String, String>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
