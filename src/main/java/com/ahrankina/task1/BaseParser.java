package com.ahrankina.task1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

abstract class BaseParser {
    private static final String BASE_URL = "https://ain.ua/";
    private static final int BASE_URL_LENGTH = BASE_URL.length();
    private static final String USER_AGENT = "Chrome";

    private static final String TAGS_SELECT = "div.web-view > div.tag";
    private static final String CATEGORY_CLASS = "block-title-styles";
    private static final String ARTICLE_CLASS = "post-item ordinary-post";
    private static final String A_TAG = "a";
    private static final String HREF_ATTR = "href";
    private static final String ORDINAL_ARTICLE_CLASS = "post-content";
    private static final String ADVERTISING_ARTICLE_CLASS = "t-records";
    private static final String ADVERTISING_ARTICLE_CONDITION = "special";
    private static final String SUB_DOMAIN_ARTICLE_CLASS = "content";

    private static Document getDocument(String name) throws Exception {
        return Jsoup.connect(name).userAgent(USER_AGENT).get();
    }

    Elements getTags() throws Exception {
        return getDocument(BASE_URL).select(TAGS_SELECT);
    }

    static String getCategoryNameByTag(Element tag) {
        return tag.getElementsByClass(CATEGORY_CLASS).first().text();
    }

    static Elements getArticlesByTag(Element tag) {
        return tag.getElementsByClass(ARTICLE_CLASS);
    }

    static void insertArticleToDb(Element article, String categoryName) {
        Element link = article.getElementsByTag(A_TAG).first();
        String title = link.text();
        String url = link.attr(HREF_ATTR);
        String className = url.startsWith(BASE_URL) ?
                (url.startsWith(ADVERTISING_ARTICLE_CONDITION, BASE_URL_LENGTH)
                        ? ADVERTISING_ARTICLE_CLASS : ORDINAL_ARTICLE_CLASS)
                : SUB_DOMAIN_ARTICLE_CLASS;
        try {
            String text = getDocument(url).getElementsByClass(className).first().text();
            JDBCUtils.insert(title, categoryName, url, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
