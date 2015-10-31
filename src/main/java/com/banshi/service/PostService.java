package com.banshi.service;

import com.banshi.model.dao.PostDao;
import com.banshi.model.dto.PostDTO;
import com.banshi.model.enums.PostEnum;
import com.banshi.utils.*;
import com.banshi.controller.vo.PostVO;
import com.banshi.controller.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Resource
    private PostDao postDao;

    /**
     * 保存日志
     *
     * @param postDTO
     * @return
     */
    public PostVO savePost(PostDTO postDTO, UserVO sessionUserVo, HttpServletRequest request) {

        //输入check
        //日志标题不能为空
        if (MyString.isBlank(postDTO.getTitle())) {
            PostVO postVO = new PostVO();
            postVO.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            postVO.setRetErrorMap(MapUtil.buildMap("post.title", "日志标题不能为空"));
            return postVO;
        }

        //日志内容不能为空
        if (MyString.isBlank(postDTO.getContent())) {
            PostVO postVO = new PostVO();
            postVO.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            postVO.setRetErrorMap(MapUtil.buildMap("post.content", "日志内容不能为空"));
            return postVO;
        }

        if(sessionUserVo == null || sessionUserVo.getId() == null){
            PostVO postVO = new PostVO();
            postVO.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            postVO.setRetErrorMap(MapUtil.buildMap("post.user", "用户未登录或登录已失效"));
            return postVO;
        } else {
            postDTO.setUserId(sessionUserVo.getId());
            postDTO.setUserName(sessionUserVo.getName());
        }
        String userIp = WebUtil.getRemoteIp(request);
        postDTO.setUserIp(userIp);
        postDTO.setStatus(PostEnum.PostStatus.NEW.name());
        String retMsg = "新增日志成功";
        try{
            Date nowDate = new Date();
            if(postDTO.getId() != null){
                retMsg = "更新日志成功";
                postDTO.setUpdatedAt(nowDate);
                postDao.update(postDTO);
            } else {
                postDTO.setCreatedAt(nowDate);
                postDTO.setUpdatedAt(nowDate);
                postDao.insert(postDTO);
            }
        } catch (Exception e){
            Logger.error(this, String.format("savePost failed, error=%s", e.getMessage()), e);
            PostVO postVO = new PostVO();
            postVO.setRetCode(UserVO.RET_CODE_SYS_ERROR);
            postVO.setRetErrorMap(MapUtil.buildMap("system", "系统异常"));
            return postVO;
        }

        PostVO postVO = new PostVO(postDTO);
        postVO.setRetCode(UserVO.RET_CODE_SUCCESS);
        postVO.setRetMsg(retMsg);

        return postVO;
    }

    /**
     * 查询指定日志
     *
     * @param id
     * @return
     */
    public PostVO getPost(Long id) {

        PostDTO postDTO = postDao.selectById(id);
        if(postDTO == null){
            return new PostVO();
        } else {
            return new PostVO(postDTO);
        }
    }

    /**
     * 查询日志
     *
     * @param userName
     * @return
     */
    public List<PostVO> getPostList(String userName) {

        Map<String, Object> cndMap = MapUtil.buildMap("userName", userName);
        List<PostDTO> postDTOList = postDao.selectByCnd(cndMap);
        List<PostVO> postVOList = new ArrayList<PostVO>();
        for(PostDTO postDTO:postDTOList){
            PostVO postVO = new PostVO(postDTO);
            postVOList.add(postVO);
        }
        return postVOList;
    }

}
