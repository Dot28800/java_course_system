package com.teach.javafx.controller;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField activityPlaceField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField contentField;
    @FXML
    private  ComboBox<OptionItem> typeComboBox;

    @FXML
    private TextField activityEvaluationField;
    @FXML
    private Button okButtonClick;
    private java.util.List<OptionItem> studentList;
    private ActivityTableController activityTableController;
    private Integer activityId=null;

    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }
    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }
    public TextField getActivityPlaceField() {
        return activityPlaceField;
    }
    public void setActivityPlaceField(TextField titleField) {
        this.activityPlaceField = activityPlaceField;
    }
    public DatePicker getTimePicker() {
        return timePicker;
    }
    public void setTimePicker(DatePicker timePicker) {
        this.timePicker = timePicker;
    }
    public TextField getContentField() {
        return contentField;
    }
    public void setContentField(TextField contentField) {
        this.contentField = contentField;
    }
    public TextField getActivityEvaluationField() {
        return activityEvaluationField;
    }
    public void setActivityEvaluationField(TextField activityEvaluationField) {this.activityEvaluationField = activityEvaluationField;
    }
    public TextField getTypeField() {
        return typeField;
    }
    public void setTypeField(TextField typeField) {
        this.typeField = typeField;
    }

    public java.util.List<OptionItem> getStudentList() {
        return studentList;
    }
    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }
    private List<OptionItem> typeList;

    public ActivityTableController getActivityTableController() {
        return activityTableController;
    }

    public void setActivityTableController(ActivityTableController activityTableController) {
        this.activityTableController = activityTableController;
    }

    public Integer getActivityId() {
        return activityId;
    }
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    public List<OptionItem> initTypeList(){
        List<OptionItem> types  = new ArrayList<>();
        types.add(new OptionItem(0,"","运动"));
        types.add(new OptionItem(1,"","旅游"));
        types.add(new OptionItem(2,"","演出"));
        types.add(new OptionItem(3,"","聚会"));
        types.add(new OptionItem(4,"","其它"));
        return types;
    }
    @FXML
    public void initialize(){
    }
    public void  init() {

        typeList=initTypeList();
        typeComboBox.getItems().addAll(typeList);

    }
    public void showDialog(Map data) {
        //studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        //studentComboBox.setDisable(true);
        activityId= CommonMethod.getInteger(data,"activityId");
        contentField.setText(CommonMethod.getString(data,"content"));
        activityPlaceField.setText(CommonMethod.getString(data, "activityPlace"));
        activityEvaluationField.setText(CommonMethod.getString(data, "activityEvaluation"));
        //typeField.setText(CommonMethod.getString(data,"type"));
        timePicker.getEditor().setText(CommonMethod.getString(data, "activityTime"));

    }

    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = typeComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("type", op.getTitle());
        } else {
            MessageDialog.showDialog("请选中活动类型之后再点击我");
        }

        String activityPlace=activityPlaceField.getText();
        String activityTime=timePicker.getEditor().getText();
        String content=contentField.getText();
        String activityEvaluation=activityEvaluationField.getText();
        boolean flag=activityPlace.isEmpty() || activityTime.isEmpty() || content.isEmpty() || activityEvaluation.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("activityId",activityId);
        data.put("activityPlace",activityPlace);
        data.put("activityTime",activityTime);
        data.put("content",content);
        data.put("activityEvaluation",activityEvaluation);
        activityTableController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        activityTableController.doEditClose("cancel",null);

    }
}

