package cn.edu.bupt.controller;

import cn.edu.bupt.bean.po.User;
import cn.edu.bupt.bean.vo.EntityListVo;
import cn.edu.bupt.bean.vo.RelationListVo;
import cn.edu.bupt.bean.vo.UserInfoVo;
import cn.edu.bupt.constant.OauthConsts;
import cn.edu.bupt.constant.ParamConsts;
import cn.edu.bupt.service.UserService;
import cn.edu.bupt.util.ResponseResult;
import cn.edu.bupt.bean.vo.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("entities")
    public ResponseResult<EntityListVo> reviewedEntities(@RequestBody Map<String, Object> params,
                                                         HttpSession httpSession) {
        Identity identity = (Identity) httpSession.getAttribute(OauthConsts.KEY_IDENTITY);
//        boolean passed = (boolean) params.get("passed");
        int pageNo = (int) params.get(ParamConsts.pageNo);
        int pageSize = (int) params.get(ParamConsts.pageSize);
//        int interPassed = passed ? 1 : 0;
        EntityListVo marks = userService.listEntities(identity.getId() /*interPassed*/, pageNo, pageSize);
        return ResponseResult.of("success", marks);
    }

    @PostMapping("relations")
    public ResponseResult<RelationListVo> reviewedRelations(@RequestBody Map<String, Object> params,
                                                            HttpSession session) {
        Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
//        boolean passed = (boolean) params.get("passed");
        int pageNo = (int) params.get(ParamConsts.pageNo);
        int pageSize = (int) params.get(ParamConsts.pageSize);
//        int interPassed = passed ? 1 : 0;
        RelationListVo marks = userService.listRelations(identity.getId(), /*interPassed,*/ pageNo, pageSize);
        return ResponseResult.of("success", marks);
    }

    @GetMapping("info")
    public ResponseEntity<ResponseResult<UserInfoVo>> getUserInfo(HttpSession session) {
        Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
        User user = userService.getUser(identity.getClientId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(ResponseResult.of("success", new UserInfoVo(user)));
    }

}
