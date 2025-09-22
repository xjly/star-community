# Star-Community ✨

欢迎来到 Star-Community 项目！本指南将引导您完成项目客户端和服务端的完整设置流程。

## 🚀 快速开始

请遵循以下步骤来启动和运行本项目。

### **环境准备**

在开始之前，请确保您已安装好以下环境和工具：
* [HBuilder X](https.www.dcloud.io/hbuilderx.html)
* [Node.js](https://nodejs.org/) (及其包管理工具 npm)
* [Redis](https://redis.io/)
* [MySQL](https://www.mysql.com/)

### **客户端配置 (前端)**

1.  **导入项目**
    * 打开 `HBuilder X` 并导入 `client` 文件夹。

2.  **配置后端地址**
    * 在 `HBuilder X` 中，找到并打开 `main.js` 和 `http.js` 这两个文件。
    * 在两个文件中，找到 `base_url` 变量，并将其值修改为您的后端服务器 URL 地址。

3.  **安装依赖**
    * 在 `client` 文件夹的根目录下打开终端，并执行以下命令来安装所需依赖：
        ```bash
        npm install uview-ui@2.0.38
        npm install commander
        ```

4.  **运行客户端**
    * 在 `HBuilder X` 中，点击 "运行到浏览器" 或 "运行到小程序模拟器" 来启动前端应用。

### **服务端配置 (后端)**

1.  **找到配置文件**
    * 进入 `server` 文件夹。
    * 找到并打开 `application.yaml` 配置文件。

2.  **修改配置参数**
    * 根据您的实际环境，依次修改文件中的以下配置项：
        * **腾讯云COS**: 您的对象存储桶名称、地域、密钥等信息。
        * **微信小程序**: 您的小程序 `AppID` 和 `AppSecret`。
        * **JWT**: 用于身份验证的 `JWT` 密钥和过期时间配置。
        * **Redis**: 连接地址、端口号和密码。
        * **MySQL**: 数据库连接 URL、用户名和密码。
        * **MyBatis-Plus**: 相关配置。
        * **服务端口**: 您希望后端服务运行的端口号。

3.  **启动服务**
    * 完成所有配置后，启动您的后端 Spring Boot 服务。

---

现在，您可以开始使用 Star-Community 了！如果在部署过程中遇到任何问题，欢迎随时提出。
