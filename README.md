# Mail
软件开发实训邮件系统
## 分工
### 前端
- 静态页面设计
- 页面和用户交互行为设计：按键响应、成功/失败提示、向后端发送请求、页面刷新
- 前后端交互接口形式，URL、表单协商
### 后端
- 用户管理：支持用户的注册和登录功能
- 邮件接收模块：解析收到的邮件，判断格式是否正确，对邮件内容进行持久化
- 邮件发送模块：根据用户的请求，读取数据库中的邮件内容以及磁盘上的附件内容，发送给用户
- 文件管理模块：服务器应该对邮件的附件进行隔离存储，便于用户访问单独邮件的附件


## 项目需求

- 邮箱注册：邮件系统应该提供注册功能，用户可以注册邮箱，服务器保存注册信息到数据库中，用于用户登录邮箱时验证。
- 接收邮件：一旦用户登录了电子邮件账户，应用程序可以自动连接到邮件服务器并下载新邮件。这可以使用IMAP或POP3(等)协议实现。一旦新邮件被下载，应用程序可以在用户的收件箱中显示它们。
- 发送邮件：用户可以使用应用程序来发送电子邮件。这可以通过SMTP(等)协议与邮件服务器进行通信以发送邮件，用户需要提供收件人地址、抄送、主题、邮件内容、附件上传。
- 其他功能：应用程序还可以包括其他功能，如标记已读/未读邮件、删除邮件、搜索邮件、同步邮件等。

### 得分项
- 实现邮件系统基本功能(收发功能)
- 对响应的错误弹窗(地址不合法、login失败等)
- 服务端使用数据库对邮件进行存储
- 发送成功需要有对应的弹窗提示
- 使用MIME发送附件，收件人能下载附件
- 文档
- 架构
- 答辩
### 加分项
- 报文约定合理
- 服务端数据库设计合理
- 同步客户端与服务器的邮件
- 实现邮件搜索、邮件过滤等功能
- 实现新邮件到达通知功能
- 用户友好的UI界面
- 客户端的本地缓存


## 功能接口（示例版）
### 用户注册登录模块
- 注册
    - URL: /register
    - Method: POST
    - Request:
        - username: string
        - password: string
    - Response:
        - status: string
        - message: string
- 登录
    - URL: /login
    - Method: POST
    - Request:
        - username: string
        - password: string
    - Response:
        - status: string
        - message: string
- 登出
    - URL: /logout
    - Method: POST
    - Request:
        - username: string
    - Response:
        - status: string
        - message: string
- 修改密码
    - URL: /changePassword
    - Method: POST
    - Request:
        - username: string
        - oldPassword: string
        - newPassword: string
    - Response:
        - status: string
        - message: string
- 验证码
    - URL: /captcha
    - Method: GET
    - Request:
        - username: string
    - Response:
        - status: string
        - message: string
        - captcha: string
### 邮件收发服务
收发邮箱不用基于现有的SMTP和POP3协议，可以基于HTTP实现，在上层自定义消息封装协议

- 发送邮件
    - URL: /sendMail
    - Method: POST
    - Request:
        - from: string
        - to: string
        - cc: string
        - subject: string
        - content: string
        - attachment: string
    - Response:
        - status: string
        - message: string
- 接收邮件
    - URL: /receiveMail
    - Method: POST
    - Request:
        - username: string
    - Response:
        - status: string
        - message: string
        - mailList: array
            - from: string
            - to: string
            - cc: string
            - subject: string
            - content: string
            - attachment: string
            - date: string
- 删除邮件
    - URL: /deleteMail
    - Method: POST
    - Request:
        - username: string
        - mailId: string
    - Response:
        - status: string
        - message: string
- 拉取邮件
    - URL: /pullMail
    - Method: POST
    - Request:
        - username: string
    - Response:
        - status: string
        - message: string
        - mailList: array
            - from: string
            - to: string
            - cc: string
            - subject: string
            - content: string
            - attachment: string
            - date: string
- 搜索邮件
    - URL: /searchMail
    - Method: POST
    - Request:
        - username: string
        - keyword: string
    - Response:
        - status: string
        - message: string
        - mailList: array
            - from: string
            - to: string
            - cc: string
            - subject: string
            - content: string
            - attachment: string
            - date: string
- 同步邮件
    - URL: /syncMail
    - Method: POST
    - Request:
        - username: string
    - Response:
        - status: string
        - message: string
        - mailList: array
            - from: string
            - to: string
            - cc: string
            - subject: string
            - content: string
            - attachment: string
            - date: string
- 标记已读/未读
    - URL: /markMail
    - Method: POST
    - Request:
        - username: string
        - mailId: string
        - read: boolean
    - Response:
        - status: string
        - message: string

### 附件管理模块
- 上传文件
    - URL: /uploadFile
    - Method: POST
    - Request:
        - file: file
    - Response:
        - status: string
        - message: string
- 下载文件
    - URL: /downloadFile
    - Method: GET
    - Request:
        - filename: string
    - Response:
        - status: string
        - message: string
        - file: file
- 删除文件
    - URL: /deleteFile
    - Method: POST
    - Request:
        - filename: string
    - Response:
        - status: string
        - message: string

## 数据库设计
数据库设计需要考虑条目，根据E-R图将数据放在多个表中

服务器应该将邮件正文和附件进行分离存储(将正文放在数据库中，附件存储在磁盘文件上)

- 用户表
    - username: string
    - password: string
- 邮件表
    - mailId: string
    - from: string
    - to: string
    - cc: string
    - subject: string
    - content: string
    - attachment: string
    - date: string
- 文件表
    - filename: string
    - ptah: filepath