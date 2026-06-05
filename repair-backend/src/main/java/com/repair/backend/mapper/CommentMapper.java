package com.repair.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repair.backend.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
