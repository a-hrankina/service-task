package com.ahrankina.task1;

public class FirstParser extends BaseParser {

    private void doJob() throws Exception {
        getTags().parallelStream().forEach(t -> {
            getArticlesByTag(t).parallelStream().limit(5).forEachOrdered(a -> {
                insertArticleToDb(a, getCategoryNameByTag(t));
            });
        });
    }

    public static void main(String[] args) {
        long startDate = System.currentTimeMillis();
        FirstParser parser = new FirstParser();
        try {
            parser.doJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - startDate);
    }
}
