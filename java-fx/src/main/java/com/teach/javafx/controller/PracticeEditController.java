package com.teach.javafx.controller;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.PracticeTableController;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticeEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;

    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField placeField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField teamNameField;
    @FXML
    private TextField evaluationField;
    @FXML
    private Button okButtonClick;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    private List<OptionItem> studentList;
    private PracticeTableController practiceTableController;
    private Integer practiceId=null;

    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }
    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }
    public TextField getPlaceField() {
        return placeField;
    }
    public void setPlaceField(TextField titleField) {
        this.placeField = placeField;
    }
    public DatePicker getTimePicker() {
        return timePicker;
    }
    public void setTimePicker(DatePicker timePicker) {
        this.timePicker = timePicker;
    }
    public TextField getTeamNameField() {
        return teamNameField;
    }
    public void setTeamNameField(TextField teamNameField) {
        this.teamNameField = teamNameField;
    }
    public TextField getEvaluationField() {
        return evaluationField;
    }
    public void setEvaluationField(TextField evaluationField) {
        this.evaluationField = evaluationField;
    }
    public TextField getTypeField() {
        return typeField;
    }
    public void setTypeField(TextField typeField) {
        this.typeField = typeField;
    }

    public List<OptionItem> getStudentList() {
        return studentList;
    }
    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }


    public PracticeTableController getPracticeTableController() {
        return practiceTableController;
    }

    public void setPracticeTableController(PracticeTableController practiceTableController) {
        this.practiceTableController = practiceTableController;
    }

    public Integer getPracticeId() {
        return practiceId;
    }
    public void setPracticeId(Integer practiceId) {
        this.practiceId = practiceId;
    }

    @FXML
    public void initialize(){
    }
    public void  init() {
        studentList=practiceTableController.getStudentList();
        studentComboBox.getItems().addAll(studentList);


    }
    public void showDialog(Map data) {
        //studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        //studentComboBox.setDisable(true);
        practiceId= CommonMethod.getInteger(data,"practiceId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        studentComboBox.setDisable(true);
        teamNameField.setText(CommonMethod.getString(data,"teamName"));
        placeField.setText(CommonMethod.getString(data, "place"));
        evaluationField.setText(CommonMethod.getString(data, "evaluation"));
        typeField.setText(CommonMethod.getString(data,"type"));
        timePicker.getEditor().setText(CommonMethod.getString(data, "time"));

    }

    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();

        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        String place=placeField.getText();
        String time=timePicker.getEditor().getText();
        String teamName=teamNameField.getText();
        String evaluation=evaluationField.getText();
        String type=typeField.getText();
        boolean flag=place.isEmpty() || time.isEmpty() || teamName.isEmpty() || type.isEmpty() || evaluation.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("practiceId",practiceId);//缺少
        data.put("place",place);
        data.put("time",time);
        data.put("teamName",teamName);
        data.put("type",type);
        data.put("evaluation",evaluation);
        practiceTableController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        practiceTableController.doEditClose("cancel",null);

    }
}



