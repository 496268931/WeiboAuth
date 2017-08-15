

## 微博自动登录
##### 登录时可能会遇到验证码，本程序调用tesseract-ocr进行识别

    验证码识别模版：weibo2.traineddata，是使用安卓版图片训练出来的
    每次获得验证码地址后，会下载十张下来
    对下载到的验证码图片，进行去除干扰线、二值化等操作，提高识别度
    通过模版进行识别，可能会得到若干个结果
    将若干个结果进行组合，得出可能的验证码值，提交登录验证
    遇到识别不出的验证码，或者登录验证失败，则刷新验证码，重复上述步骤
    
## 登录后获取到GSID
##### 登录用到的参数有s、u、p

    u是用户名，
    s是用户名+密码进过MD5加密，然后算法计算出来的，
    p是密码通过RSA加密得到的
    
## 最后用GSID获得Access_token

    获得code和access_token的步骤，参考了微博官方weibo4j项目
    获得code的时候，将gsid设置到cookie中，以通过身份校验
    获得code后，将code+应用id等参数post到https://api.weibo.com/oauth2/authorize即可获得acess_token
    
    

需要安装tesseract，并把weibo2.traineddata拷到tesseract安装路径下的tessdata目录中（如/usr/local/share/tessdata）`
