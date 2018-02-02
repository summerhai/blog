package canghailongyin.blog.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mingl on 2018-1-5.
 */
public class Test {
    public static void main(String[] args) {
        getFirstJavaArticle();
    }

    public static Optional<Article> getFirstJavaArticle() {
        List<Article> articles = new ArrayList<>();
        initArticles(articles);
        return articles.stream()
                .filter(article -> article.getTags().contains("Java"))
                .findFirst();
    }

    private static void initArticles(List<Article> articles) {
        List<String> tag1 = new ArrayList<>();
        tag1.add("abc");
        tag1.add("ddd");
        Article article1 = new Article("title1","author1",tag1);
        articles.add(article1);
        List<String> tag2 = new ArrayList<>();
        tag2.add("abc1");
        tag2.add("ddd1");
        Article article2 = new Article("title2","author2",tag2);
        articles.add(article2);
        List<String> tag3 = new ArrayList<>();
        tag3.add("Java");
        tag3.add("ddd2");
        Article article3 = new Article("title3","author3",tag3);
        articles.add(article3);
    }
}
