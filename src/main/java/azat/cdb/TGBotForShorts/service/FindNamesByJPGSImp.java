package azat.cdb.TGBotForShorts.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
@Slf4j
public class FindNamesByJPGSImp implements FindNamesByJPG{


    public String getJPG(String url) throws IOException {

        Document document = Jsoup.connect(url).get();
        Elements script = document.select("script");
        String doc = script.toString();

        Pattern pattern = Pattern.compile("https?://\\S+(?:jpg)");
        Matcher matcher = pattern.matcher(doc);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
            result = result.substring(0,result.indexOf('?'));
        }
        return result;
    }

    public String getNamesByImg(String urlImg) throws IOException {
        if (urlImg == null) return urlImg;
        String URL = "https://yandex.ru/images/search?source=collections&rpt=imageview&url=" + urlImg;
        Document doc = Jsoup.connect(URL).get();

        Elements elements = doc.select("div.Tags-Wrapper.Tags-Wrapper_clip.Tags-Wrapper_flow_wrap");
        Elements inner = elements.select("span.Button2-Text");

        StringBuilder result = new StringBuilder();

        for (Element e : inner
        ) {
            result.append(e.text()).append("\n");
        }

        return result.toString();
    }
}
