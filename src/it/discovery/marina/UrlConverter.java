package it.discovery.marina;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class UrlConverter implements Serializable {

    private static final String URLS_FILE_NAME = "urlsdb.out";
    private static final String PROTOCOL_AND_DOMAIN = "https://gen.ly/";
    private static final String ERROR_URL_NOT_FOUND = "URL not found";
    private static final String ERROR_DOMAIN_NOT_SUPPORTED = "This domain isn't supported";
    private static final String ERROR_WRONG_URL = "Wrong URL";

    private HashMap<String, String> mapOfUrls = new HashMap<>();
    private int keyOffset = 0;

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
        System.out.println(urlConverter.convertUrl(null));
        System.out.println(urlConverter.convertUrl(""));

        urlConverter.printMap();

        System.out.println(newUrl+" "+ urlConverter.getFullUrl(newUrl));
        System.out.println(newUrl2+" "+ urlConverter.getFullUrl(newUrl2));

        String fakeUrl = "https://gena/fsdf";
        System.out.println(fakeUrl+" "+ urlConverter.getFullUrl(fakeUrl));

        String fakeUrl2 = "https://gen.ly/fsdf";
        System.out.println(fakeUrl2+" "+ urlConverter.getFullUrl(fakeUrl2));

        System.out.println("null: "+ urlConverter.getFullUrl(null));

        /*for(int i=0; i< 1000; i++)
        {
            System.out.println(urlConverter.convertUrl("http://validurl.com/"+Integer.toString(i)));
        }*/

    }
   
    public String convertUrl(String longUrl)
    {
        if(!validateUrl(longUrl))
        {
            return ERROR_WRONG_URL;
        }

        loadStorage();

        String keyFromStorage = checkUrlAlreadyConverted(longUrl);
        if(keyFromStorage!= null && keyFromStorage.length() > 0){
            return toShortUrl(keyFromStorage);
        }
       
        return toShortUrl(addToStorage(longUrl));
    }

    public String getFullUrl(String shortUrl)
    {
        if(!isDomainSupported(shortUrl))
        {
            return ERROR_DOMAIN_NOT_SUPPORTED;
        }

        loadStorage();

        return mapOfUrls.getOrDefault(extractKey(shortUrl), ERROR_URL_NOT_FOUND);
    }

    private boolean isDomainSupported(String shortUrl)
    {
        return getOrDefault(shortUrl).startsWith(PROTOCOL_AND_DOMAIN);
    }

    private String extractKey(String shortUrl)
    {
        String url = getOrDefault(shortUrl);
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void printMap()
    {
        System.out.println(mapOfUrls);
    }

    private static String generateNewKey(int startPoint)
    {
        int firstValidKey = Integer.valueOf("a00000", Character.MAX_RADIX );
        return Integer.toString(firstValidKey + startPoint, Character.MAX_RADIX);
    }

   
    private boolean validateUrl(String longUrl)
    {
        try
        {
            URL url = new URL(longUrl);
            url.toURI();
        } catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    private String toShortUrl(String newKey)
    {
        return PROTOCOL_AND_DOMAIN + newKey;
    }

    private String checkUrlAlreadyConverted(String longUrl)
    {
        if(!mapOfUrls.containsValue(longUrl)) {
            return "";
        }

        Optional<Entry<String, String>> entry = mapOfUrls.entrySet().stream()
                                                .filter((n)->n.getValue().equals(longUrl)).findAny();
        if(entry.isPresent())
        {
            return entry.get().getKey();
        }

        return "";

    }

    private String addToStorage(String longUrl)
    {
        String newKey = generateNewKey(keyOffset++);
        boolean exists = mapOfUrls.containsKey(newKey);
        if(exists)
        {
            System.out.println("logging: key already exists, recursion");
            return addToStorage(longUrl);
        }
        else
        {
            mapOfUrls.put(newKey, longUrl);
            saveStorage();
            return newKey;
        }
    }

    private void saveStorage()
    {
        try {
            ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(URLS_FILE_NAME));
            out.writeInt(keyOffset);
            out.writeObject(mapOfUrls);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadStorage()
    {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(URLS_FILE_NAME));
            keyOffset = in.readInt();
            mapOfUrls = (HashMap<String, String>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getOrDefault(String string)
    {
        return Optional.ofNullable(string).orElse("");
    }
}
