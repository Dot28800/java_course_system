# 使用说明
将项目下载到本地：

`git clone https://github.com/Dot28800/java_course_system.git`

注意将三个文件夹放到一个目录下

用IDE打开java-server文件夹，找到如图所示的pom文件，选中maven，点击“reload”：
![20250218145608](https://github.com/user-attachments/assets/cd88c687-a71a-49ff-837c-51ed0886fd9f)

之后再找到server下的 java-server\src\main\java\org\fatmansoft\teach\SpringBootSecurityJwtApplication.java文件，这是主方法所在地，点击运行，得到下面的终端输出表示正常运行：
![20250218150048](https://github.com/user-attachments/assets/7959e90a-7b3f-4e32-8f47-054599cadc61)

之后在另一个窗口打开java-fx文件夹，重复刚才类似的操作，找到java-fx\src\main\java\com\teach\javafx\MainApplication.java的文件，点击运行即可，出现下面的登录界面：
![20250218150302](https://github.com/user-attachments/assets/c2d649d4-0636-422a-9582-dc531e7f8890)

此时无需输入用户名，密码和验证码，点击管理员登录或学生用户登录即可进入系统，得到下面的界面（以管理员端为例）：
![20250218150444](https://github.com/user-attachments/assets/8ad27540-ca89-4ad3-b5ba-b4e3b12337cc)

点击上方的二级菜单即可查看系统功能，并且可以进行信息的增删查改：
![20250218150531](https://github.com/user-attachments/assets/7334b804-8811-4249-8ae4-e9e147177788)

开发仓促，还有很多漏洞，仅供参考，敬请指正！



