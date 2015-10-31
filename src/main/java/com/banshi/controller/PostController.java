package com.banshi.controller;

import com.banshi.model.dto.PostDTO;
import com.banshi.service.PostService;
import com.banshi.utils.Constants;
import com.banshi.utils.MapUtil;
import com.banshi.controller.vo.PostVO;
import com.banshi.controller.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/post")
public class PostController {

    @Resource
    private PostService postService;

    /**
     * 跳转到新增日志页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/new.do", method = RequestMethod.GET)
    public ModelAndView newPost() throws Exception {
        return new ModelAndView("post/edit");
    }

    /**
     * 跳转到新增日志页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit.do", method = RequestMethod.GET)
    public ModelAndView editPost(@RequestParam(value = "id") Long id) throws Exception {
        PostVO postVO = postService.getPost(id);
        return new ModelAndView("post/edit", "post", postVO);
    }

    /**
     * 保存日志
     * @param postDto
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    public ModelAndView savePost(@ModelAttribute("post") PostDTO postDto, HttpServletRequest request) throws Exception {

        UserVO sessionUserVo = (UserVO)request.getSession().getAttribute(Constants.SESSION_USER);
        if(sessionUserVo == null || sessionUserVo.getId() == null){
            PostVO postVO = new PostVO();
            postVO.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            postVO.setRetErrorMap(MapUtil.buildMap("post.user", "用户未登录或登录已失效"));
            return new ModelAndView("post/edit", "post", postVO);
        }
        PostVO postVO = postService.savePost(postDto, sessionUserVo, request);

        if(!PostVO.RET_CODE_SUCCESS.equals(postVO.getRetCode())){
            //保存失败
            return new ModelAndView("post/edit", "post", postVO);
        } else {
            //保存成功
            List<PostVO> postVOList = postService.getPostList(sessionUserVo.getName());
            return new ModelAndView("post/list", "postList", postVOList);
        }
    }

    /**
     * 查看日志列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/viewList.do", method = RequestMethod.POST)
    public ModelAndView viewPostList(HttpServletRequest request) throws Exception {

        UserVO sessionUserVo = (UserVO)request.getSession().getAttribute(Constants.SESSION_USER);
        List<PostVO> postVOList = postService.getPostList(sessionUserVo.getName());
        return new ModelAndView("post/list", "postList", postVOList);
    }

}

