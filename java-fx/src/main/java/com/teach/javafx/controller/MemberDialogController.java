package com.teach.javafx.controller;

import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDialogController {
    @FXML
    private TextField studentNumName;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;

    @FXML
    private TextField jobField;
    @FXML
    private TextField relationField;
    private Integer memberId;
    @FXML
    private ComboBox<OptionItem> genderBox;
    private FamilyMemberController familyMemberController;

    private List<OptionItem> genderList;

    private String type="";

    public FamilyMemberController getFamilyMemberController() {
        return familyMemberController;
    }

    public void setFamilyMemberController(FamilyMemberController familyMemberController) {
        this.familyMemberController = familyMemberController;
    }
    List<OptionItem> initGender(){
        List<OptionItem> levels=new ArrayList<>();
        levels.add(new OptionItem(0,"","男"));
        levels.add(new OptionItem(1,"","女"));

        return levels;
    }

    @FXML
    public void initialize(){


    }

    public void init(String s){
        genderList=familyMemberController.getGenderList();
        genderBox.getItems().addAll(genderList);
        this.type=s;

    }
    @FXML
    public void showDialog(Map data) {
        if(data == null) {
            memberId = null;
            genderBox.getSelectionModel().select(-1);
            genderBox.setDisable(false);
            ageField.setText("");
            nameField.setText("");
            jobField.setText("");
            studentNumName.setText("");
            relationField.setText("");
            nameField.setText("");
        }else {
            memberId = CommonMethod.getInteger(data,"memberId");
            genderBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(genderList,"gender"));
            genderBox.setDisable(false);
            relationField.setText(CommonMethod.getString(data,"relationship"));
            ageField.setText(CommonMethod.getString(data,"age"));
            nameField.setText(CommonMethod.getString(data,"name"));
            jobField.setText(CommonMethod.getString(data,"job"));
            studentNumName.setText(CommonMethod.getString(data,"studentName"));
            String userType=CommonMethod.getString(data,"userType");
            if(userType.equals("学生")){
                studentNumName.setEditable(false);
            }


        }
    }
    @FXML
    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = genderBox.getSelectionModel().getSelectedItem();
        /*if(timeField.getText()==null || flagField.getText()==null ){
            MessageDialog.showDialog("信息不完整，请重新输入");
            return;
        }*/
        if(op != null) {
            data.put("gender",op.getTitle());
        }else {
            MessageDialog.showDialog("请先选择性别");
            return;
        }
        String numName=studentNumName.getText();
        String name=nameField.getText();
        String age=ageField.getText();
        String job=jobField.getText();
        String relation=relationField.getText();
        boolean flag=numName.isEmpty()||age.isEmpty()||job.isEmpty()||name.isEmpty()||relation.isEmpty();
        if(flag){
            MessageDialog.showDialog("请先补充完整信息再提交");
            return;
        }
        try{
            Integer integer=Integer.parseInt(age);
        }catch (Exception e){
            MessageDialog.showDialog("年龄输入格式有误");
            return;
        }
        data.put("numName",numName);
        data.put("name",name);
        data.put("age",age);
        data.put("relation",relation);
        data.put("job",job);
        data.put("type",type);
        /*data.put("time",timeField.getText());
        data.put("flag",flagField.getText());*/
        familyMemberController.doClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        familyMemberController.doClose("cancel",null);

    }
}
