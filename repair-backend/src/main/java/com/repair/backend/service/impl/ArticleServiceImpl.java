package com.repair.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.backend.entity.Article;
import com.repair.backend.mapper.ArticleMapper;
import com.repair.backend.service.IArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
}
