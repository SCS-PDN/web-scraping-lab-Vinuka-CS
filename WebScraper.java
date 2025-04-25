import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraper {

    public static void main(String[] args) throws IOException {
        String url = "https://www.bbc.com";
        boolean scrapeTitle = true;
        boolean scrapeLinks = true;
        boolean scrapeImages = true;

        scrapeWebsite(url, scrapeTitle, scrapeLinks, scrapeImages);
    }

    public static void scrapeWebsite(String url, boolean scrapeTitle, boolean scrapeLinks, boolean scrapeImages) throws IOException {
        Document doc = Jsoup.connect(url).get();
        StringBuilder result = new StringBuilder();

        if (scrapeTitle) {
            result.append("Page Title: ").append(doc.title()).append("\n\n");
        }

        if (scrapeLinks) {
            Elements links = doc.select("a[href]");
            result.append("\nLinks:\n");
            for (Element link : links) {
                result.append(link.attr("abs:href")).append(" - ").append(link.text()).append("\n");
            }
        }

        if (scrapeImages) {
            Elements images = doc.select("img[src]");
            result.append("\nImages:\n");
            for (Element img : images) {
                result.append(img.attr("abs:src")).append(" - ").append(img.attr("alt")).append("\n");
            }
        }

        System.out.println(result.toString());
    }
}

