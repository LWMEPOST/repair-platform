package com.repair.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repair.backend.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
