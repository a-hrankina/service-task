package com.ahrankina.task1;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SecondParser extends BaseParser {

    public static void main(String[] args) throws Exception {
        long startDate = System.currentTimeMillis();
        SecondParser parser = new SecondParser();
        ExecutorService executor = Executors.newFixedThreadPool(15);
        try {
            List<Future<List<Category>>> list = new ArrayList<>();
            for (Element category : parser.getTags()) {
                list.add(executor.submit(new CategoryJob(category)));
            }

            for (Future<List<Category>> future : list) {
                for (Category category : future.get()) {
                    executor.submit(new ArticleJob(category));
                }
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        }
        System.out.println(System.currentTimeMillis() - startDate);

    }

    private static final class CategoryJob implements Callable<List<Category>> {
        private Element tag;

        CategoryJob(Element tag) {
            this.tag = tag;
        }

        @Override
        public List<Category> call() {
            String category = getCategoryNameByTag(tag);
            return getArticlesByTag(tag)
                    .parallelStream()
                    .limit(5)
                    .map(article -> new Category(category, article))
                    .collect(Collectors.toList());
        }
    }

    private static final class ArticleJob implements Runnable {
        private Category category;

        ArticleJob(Category category) {
            this.category = category;
        }

        @Override
        public void run() {
            insertArticleToDb(category.getArticle(), category.getName());
        }
    }

}
