package com.ahrankina.task1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.nodes.Element;

@AllArgsConstructor
@Getter
class Category {
    private String name;
    private Element article;
}