package com.teach.javafx.controller;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.ScienceTableController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.util.CommonMethod;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScienceEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;

    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField achievementField;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextField teacherNameField;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    private List<OptionItem> studentList;
    private ScienceTableController scienceTableController;
    private Integer scienceId=null;

    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }
    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }
    public TextField getAchievementField() {
        return achievementField;
    }
    public void setProjectNameField(TextField titleField) {
        this.projectNameField = projectNameField;
    }

    public TextField getTeacherNameField() {
        return teacherNameField;
    }
    public void setScienceTableController(ScienceTableController scienceTableController) {
        this.scienceTableController = scienceTableController;
    }

    public Integer getScienceId() {
        return scienceId;
    }
    public void setPracticeId(Integer practiceId) {
        this.scienceId = scienceId;
    }

    @FXML
    public void initialize(){

    }
    public void  init() {
        studentList = HttpRequestUtil.requestOptionItemList("/api/homework/getStudentItemOptionList",new DataRequest());
        OptionItem item = new OptionItem(null,"0","请选择");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);

    }
    public void showDialog(Map data) {

        //studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        //studentComboBox.setDisable(true);
        scienceId= CommonMethod.getInteger(data,"scienceId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "studentId")));
        studentComboBox.setDisable(true);
        achievementField.setText(CommonMethod.getString(data,"achievement"));
        projectNameField.setText(CommonMethod.getString(data, "projectName"));
        teacherNameField.setText(CommonMethod.getString(data, "teacherName"));


    }

    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();

        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        data.put("scienceId",scienceId);
        String achievement=achievementField.getText();
        String teacherName=teacherNameField.getText();
        String projectName=projectNameField.getText();
        boolean flag=achievement.isEmpty() || teacherName.isEmpty() || projectName.isEmpty() ;
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("achievement",achievement);
        data.put("teacherName",teacherName);
        data.put("projectName",projectName);
        scienceTableController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        scienceTableController.doEditClose("cancel",null);

    }
}



