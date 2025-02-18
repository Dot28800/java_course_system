package com.teach.javafx;


import com.teach.javafx.controller.base.AdminFrameController;
import com.teach.javafx.controller.base.MainFrameController;
import com.teach.javafx.controller.base.StudentFrameController;
import com.teach.javafx.request.JwtResponse;

/**
 * 前端应用全程数据类
 * JwtResponse jwt 客户登录信息
 */
public class AppStore {//共享方法区域
    private static JwtResponse jwt;
    private static MainFrameController mainFrameController;
    public static AdminFrameController adminFrameController;

    public static StudentFrameController studentFrameController;


    private AppStore(){
    }

    public static JwtResponse getJwt() {
        return jwt;
    }

    public static void setJwt(JwtResponse jwt) {
        AppStore.jwt = jwt;
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static AdminFrameController getAdminFrameController() {
        return adminFrameController;
    }

    public static StudentFrameController getStudentFrameController() {
        return studentFrameController;
    }

    public static void setMainFrameController(MainFrameController mainFrameController) {
        AppStore.mainFrameController = mainFrameController;
    }
    public static void setAdminHomePage(AdminFrameController adminFrameController){
        AppStore.adminFrameController=adminFrameController;
    }
    public static void setStudentFrameController(StudentFrameController studentFrameController){
        AppStore.studentFrameController=studentFrameController;
    }


}
