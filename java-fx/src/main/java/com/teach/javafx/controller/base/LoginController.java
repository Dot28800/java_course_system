package com.teach.javafx.controller.base;

import com.teach.javafx.AppStore;
import com.teach.javafx.MainApplication;
import com.teach.javafx.request.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.XMLFormatter;


/**
 * LoginController 登录交互控制类 对应 base/login-view.fxml
 *  @FXML  属性 对应fxml文件中的 fx:id 属性 如TextField usernameField 对应 fx:id="usernameField"
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性  如onLoginButtonClick() 对应onAction="#onLoginButtonClick"
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private VBox vbox;
    @FXML
    private Button loginButton;
    @FXML
    private TextField yzmField;
    private String key;
    @FXML
    public ImageView yzm;
    public static LoginRequest nowLogin;
    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        shuaxin();
        usernameField.setText("admin");
        passwordField.setText("123456");
        usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            }
        });
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            }
        });
        yzmField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            }
        });
//        vbox.setId("min");  // id选择器 #
//        vbox.getStyleClass().add("min");  类选择器 .
        //vbox.setStyle("-fx-background-image: url('双生龙.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: cover;");  //inline选择器
//        loginButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
    }

    /**
     *  点击登录按钮 执行onLoginButtonClick 方法 从面板上获取用户名和密码，请求后台登录服务，登录成功加载主框架，切换舞台到主框架，登录不成功，提示错误信息
     */
    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        LoginRequest loginRequest = new LoginRequest(username,password);
        nowLogin = new LoginRequest(username,password);
        String msg = HttpRequestUtil.login(loginRequest);
        if(msg != null) {
            MessageDialog.showDialog( msg);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/admin-homePage.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            //AppStore.setMainFrameController((MainFrameController) fxmlLoader.getController());
            AppStore.setAdminHomePage((AdminFrameController) fxmlLoader.getController());
            MainApplication.resetStage("学生管理系统（管理版）", scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void loginByStudent(){
        String username = "2023003165";
        String password = "12345";
        LoginRequest loginRequest = new LoginRequest(username,password);
        String msg = HttpRequestUtil.login(loginRequest);
        if(msg != null) {
            MessageDialog.showDialog( msg);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/student-homePage.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            AppStore.setStudentFrameController((StudentFrameController) fxmlLoader.getController());
            MainApplication.resetStage("学生管理系统（学生版）", scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void shuaxin() {
        for (int i = 1; i < 2; i++) {
            ByteArrayOutputStream out = null;
            try {
                out=new ByteArrayOutputStream();
                String s= com.teach.javafxclient.controller.base.AuthCodeUtils.getRandom(4);
                key=s;
                com.teach.javafxclient.controller.base.AuthCodeUtils.draw(out, key);
                out.close();
                byte[] bytes=out.toByteArray();
                Image image = new Image(new ByteArrayInputStream(bytes));
                yzm.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    @FXML
    public void forgetPassword(){
        MessageDialog.showDialog("请联系老师处理(⊙﹏⊙)");
    }
}