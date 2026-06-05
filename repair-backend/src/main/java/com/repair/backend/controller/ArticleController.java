package com.repair.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.repair.backend.entity.Article;
import com.repair.backend.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    // 获取文章列表 (支持分页和分类筛选)
    @GetMapping
    public Page<Article> list(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size,
                              @RequestParam(required = false) String category) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Article::getCategory, category);
        }
        wrapper.orderByDesc(Article::getCreateTime);
        return articleService.page(pageParam, wrapper);
    }

    // 获取文章详情
    @GetMapping("/{id}")
    public Article getById(@PathVariable Long id) {
        return articleService.getById(id);
    }

    // 新增文章 (管理端)
    @PostMapping
    public boolean save(@RequestBody Article article) {
        return articleService.save(article);
    }

    // 更新文章 (管理端)
    @PutMapping
    public boolean update(@RequestBody Article article) {
        return articleService.updateById(article);
    }

    // 删除文章 (管理端)
    @DeleteMapping("/{id}")
    public boolean remove(@PathVariable Long id) {
        return articleService.removeById(id);
    }
}
