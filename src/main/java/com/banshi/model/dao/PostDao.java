package com.banshi.model.dao;


import com.banshi.model.dto.PostDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PostDao extends BaseDao {

    /**
     * 根据dto插入一条记录，返回自动生成主键
     *
     * @param postDTO
     * @return
     */
    public int insert(PostDTO postDTO) {
        return insert("Post.insert", postDTO);
    }

    /**
     * 根据dto更新一条记录，更新条件为id=dto.id
     *
     * @param postDTO
     * @return
     */
    public int update(PostDTO postDTO) {
        return update("Post.update", postDTO);
    }

    /**
     * 根据id(主键)查询，返回唯一一条记录
     *
     * @param id
     * @return
     */
    public PostDTO selectById(Long id) {
        return (PostDTO) queryOne("Post.selectById", id);
    }

    /**
     * 根据指定条件查询，返回记录集
     *
     * @param cndMap
     * @return
     */
    public Integer countByCnd(Map<String, Object> cndMap) {
        return (Integer) queryOne("Post.countByCnd", cndMap);
    }

    /**
     * 根据指定条件查询，返回记录集
     *
     * @param cndMap
     * @return
     */
    public List<PostDTO> selectByCnd(Map<String, Object> cndMap) {
        return queryList("Post.selectByCnd", cndMap);
    }

}
