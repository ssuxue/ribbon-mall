package com.chase.ribbon.common.util;

import com.chase.ribbon.domain.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Description 解析网页返回数据
 * @Author chase
 * @Date 2020/8/27 20:47
 */
@Component
public class HtmlParseUtil {

    public List<Content> parseJingDong(String keyword) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByClass("gl-item");

        List<Content> goodsList = new ArrayList<>();

        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            goodsList.add(Content.builder()
                    .title(title)
                    .img(img)
                    .price(price)
                    .build());
        }
        return goodsList;
    }
}
