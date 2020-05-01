package com.example.auth.demo.controller.book;

import com.example.auth.demo.cache.Code;
import com.example.auth.demo.cache.PhoneCode;
import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.JSONApi.SignupApi;
import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.entity.user.UserLogin;
import com.example.auth.demo.domain.auth.ResponseUserToken;
import com.example.auth.demo.JSONApi.UserLoginApi;
import com.example.auth.demo.service.user.AuthService;

import com.example.auth.demo.utils.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


@RestController
@Api(description = "登陆,退出")
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {
    @Value("${jwt.tokenHead}")
    private String tokenHeader;

    @Value("${jwt.header}")
    private String header;

    @Autowired
    private PhoneCode phoneCode;

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登陆", notes = "登陆成功返回token;用户账号：123,123456")
    public ResultJson login(@Valid @RequestBody UserLoginApi userLogin, HttpServletResponse response){
        final ResponseUserToken res = authService.login(userLogin.getLoginid(), userLogin.getPassword());
        Cookie cookie = new Cookie(header,tokenHeader+res.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
        Map map = new HashMap();
        map.put("user_id",res.getUserDetail().getId());
        map.put("isVerify",res.getUserDetail().getIsVerify());
        return ResultJson.ok(map);
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "登出", notes = "退出登陆")
    //@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public ResultJson logout(HttpServletRequest request,HttpServletResponse response){
        Cookie cookie = new Cookie(header,"");
        response.addCookie(cookie);
        return ResultJson.ok();
    }

    @GetMapping(value = "/user")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    //@ApiImplicitParams({@ApiImplicitParam(name = "user",value = "userid",required = true, dataType = "string", paramType = "query")})
    public ResultJson getUser(HttpServletRequest request){
        long user_id = (long)request.getAttribute("user_id");
        UserDetail userDetail = authService.getUserDetailById(user_id);
        return ResultJson.ok(userDetail);
    }

    @PostMapping(value = "/sign")
    @ApiOperation(value = "用户注册")
    public ResultJson sign(@RequestBody SignupApi signupApi) {
        String phone = signupApi.getPhonenum();
        String code = signupApi.getSmscode();
        if (!org.apache.commons.lang3.StringUtils.isAnyBlank(signupApi.getPhonenum(), signupApi.getPassword(),signupApi.getSmscode())) {
            if (code.equals(phoneCode.getCodemap().get(phone).getCode())==false)
            {
                return ResultJson.failure(ResultCode.SMSCODE_ERROR);
            }
        }
        return ResultJson.ok(authService.register(UserLogin.builder().id(0).loginid(signupApi.getPhonenum()).password(signupApi.getPassword()).build()));
    }
//    @GetMapping(value = "refresh")
//    @ApiOperation(value = "刷新token")
//    public ResultJson refreshAndGetAuthenticationToken(
//            HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        ResponseUserToken response = authService.refresh(token);
//        if(response == null) {
//            return ResultJson.failure(ResultCode.BAD_REQUEST, "token无效");
//        } else {
//            return ResultJson.ok(response);
//        }
//    }





    @GetMapping("/getsmscode")
    @ApiOperation(value = "获取短信验证码", notes = "测试时从从服务器返回json，SMSCode")
    public ResultJson getsmscode(@RequestParam String phonenum) {
        Map map = new HashMap();
        String host = "https://fesms.market.alicloudapi.com";
        String path = "/sms/";
        String method = "GET";
        String appcode = "ecfbdb56c7d747dc8f9bdb4cd1aec41e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        Integer code = (int) (Math.random() * 8998) + 1000 + 1;
        querys.put("code", code.toString());
        querys.put("phone", phonenum);
        querys.put("skin", "2");
        querys.put("sign", "1");
        //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());//如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode()==200)
            {
                phoneCode.getCodemap().put(phonenum,new Code(code.toString(),System.currentTimeMillis()));
                this.removecode(phoneCode,phonenum);
                return ResultJson.ok();
            }
//            map.put("SMSCode",code);
//            phoneCode.getCodemap().put(phonenum,new Code(""+code,System.currentTimeMillis()));
//            return ResultJson.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failure(ResultCode.SERVICE_GETCODE_ERROR);
        }
        return ResultJson.failure(ResultCode.SERVICE_GETCODE_ERROR);
    }

    private void removecode(final PhoneCode phoneCode, final String phonenum) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                phoneCode.removecode(phonenum);
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }

}
