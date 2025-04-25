import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        boolean scrapeTitle = Boolean.parseBoolean(request.getParameter("title"));
        boolean scrapeLinks = Boolean.parseBoolean(request.getParameter("links"));
        boolean scrapeImages = Boolean.parseBoolean(request.getParameter("images"));

        StringBuilder result = new StringBuilder();
        if (url != null && !url.isEmpty()) {
            result.append(scrapeWebsite(url, scrapeTitle, scrapeLinks, scrapeImages));
        }

        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) visitCount = 0;
        session.setAttribute("visitCount", visitCount + 1);

        result.append("<br>You have visited this page ").append(visitCount + 1).append(" times.");

        Gson gson = new Gson();
        String json = gson.toJson(result.toString());

        response.setContentType("application/json");
        response.getWriter().write(json);

        request.setAttribute("scrapedData", result.toString());
        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
    }

    private String scrapeWebsite(String url, boolean scrapeTitle, boolean scrapeLinks, boolean scrapeImages) throws IOException {
        Document doc = Jsoup.connect(url).get();
        StringBuilder result = new StringBuilder();

        if (scrapeTitle) {
            result.append("<br><b>Title:</b> ").append(doc.title()).append("<br>");
        }

        if (scrapeLinks) {
            Elements links = doc.select("a[href]");
            result.append("<br><b>Links:</b><br>");
            for (Element link : links) {
                result.append(link.attr("abs:href")).append(" - ").append(link.text()).append("<br>");
            }
        }

        if (scrapeImages) {
            Elements images = doc.select("img[src]");
            result.append("<br><b>Images:</b><br>");
            for (Element img : images) {
                result.append(img.attr("abs:src")).append(" - ").append(img.attr("alt")).append("<br>");
            }
        }

        return result.toString();
    }
}
