package com.teach.javafx.controller.base;

import com.teach.javafx.request.HttpRequestUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.Objects;

public class EditPassword {


    @FXML
    private TextField numField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField repeatPassword;

    @FXML
    public void initialize(){

    }
    @FXML
    void onOk(){
        String num=numField.getText();
        String name=nameField.getText();
        String password=newPassword.getText();
        String repeat= repeatPassword.getText();
        if(Objects.equals(num, "") || name.isEmpty() || password.isEmpty() || repeat.isEmpty()){
            MessageDialog.showDialog("请输入完整信息！");
            return;
        }
        if(!password.equals(repeat)){
            MessageDialog.showDialog("密码与确认的不一致！");
            return;
        }
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("num",num);
        dataRequest.add("name",name);
        dataRequest.add("password",password);
        DataResponse res= HttpRequestUtil.request("/api/base/editPassword",dataRequest);
        if(res!=null && res.getCode()==0){
            MessageDialog.showDialog("修改成功！");
        } else if ((res!=null)) {
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误");
        }

    }
    @FXML
    void onCancel(){
        clearPanel();
        MessageDialog.showDialog("重置成功！");

    }

    void clearPanel(){
        numField.setText("");
        nameField.setText("");
        newPassword.setText("");
        repeatPassword.setText("");
    }
}
