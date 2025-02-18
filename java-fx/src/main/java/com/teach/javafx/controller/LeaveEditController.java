package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveEditController {

    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private ComboBox<OptionItem> flagComboBox;
    @FXML
    private DatePicker returnTimePicker;
    @FXML
    private DatePicker leaveTimePicker;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField leaveReasonField;


    private List<OptionItem> studentList;
    private List<OptionItem> flagList;

    private LeaveTableController leaveTableController;
    private Integer leaveId=null;


    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }

    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }

    public TextField getLeaveReasonField() {
        return leaveReasonField;
    }

    public void setLeaveReasonField(TextField leaveReasonField) {
        this.leaveReasonField = leaveReasonField;
    }

    public DatePicker getReturnTimePicker() {
        return returnTimePicker;
    }

    public void setReturnTimePicker(DatePicker returnTimePicker) {
        this.returnTimePicker = returnTimePicker;
    }

    public DatePicker getLeaveTimePicker(){
        return leaveTimePicker;
    }

    public void setLeaveTimePicker(DatePicker leaveTimePicker) {
        this.leaveTimePicker = leaveTimePicker;
    }

    public TextField getDestinationField() {
        return destinationField;
    }

    public void setDestinationField(TextField destinationField) {
        this.destinationField = destinationField;
    }
    public ComboBox<OptionItem> getFlagComboBox() {
        return flagComboBox;
    }

    public void setFlagComboBox(ComboBox<OptionItem> flagComboBox) {
        this.flagComboBox = flagComboBox;
    }



    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }

    public List<OptionItem> getFlagList() {
        return flagList;
    }

    public void setFlagList(List<OptionItem> rankList) {
        this.flagList = flagList;
    }

    public LeaveTableController getLeaveTableController() {
        return leaveTableController;
    }

    public void setLeaveTableController(LeaveTableController leaveTableController) {
        this.leaveTableController = leaveTableController;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    @FXML
    public void initialize(){

    }

    public void init() {
        studentList=leaveTableController.getStudentList();
        //courseList=courseChooseTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        flagList=initFlagList();
        flagComboBox.getItems().addAll(flagList);

    }

    public List<OptionItem> initFlagList(){
        List<OptionItem> flags  = new ArrayList<>();
        flags.add(new OptionItem(0,"","是"));
        flags.add(new OptionItem(1,"","否"));
        return flags;
    }

    public void showDialog(Map data) {
        leaveId= CommonMethod.getInteger(data,"leaveId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        flagComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(flagList, CommonMethod.getString(data, "level")));
        studentComboBox.setDisable(true);
        flagComboBox.setDisable(false);
        destinationField.setText(CommonMethod.getString(data,"destination"));
        leaveReasonField.setText(CommonMethod.getString(data, "leaveReason"));

        returnTimePicker.getEditor().setText(CommonMethod.getString(data, "returnTime"));
        leaveTimePicker.getEditor().setText(CommonMethod.getString(data, "leaveTime"));
    }

    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        /*if(timeField.getText()==null || flagField.getText()==null ){
            MessageDialog.showDialog("信息不完整，请重新输入");
            return;
        }*/
        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        op = flagComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("flag", op.getTitle());
        } else {
            MessageDialog.showDialog("请选择是否销假之后再点击此处");
        }
        String leaveReason =leaveReasonField.getText();
        String returnTime=returnTimePicker.getEditor().getText();
        String leaveTime=leaveTimePicker.getEditor().getText();
        String destination=destinationField.getText();

        boolean flag=leaveTime.isEmpty() || returnTime.isEmpty() || leaveReason.isEmpty() || destination.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("leaveId",leaveId);
        data.put("leaveReason",leaveReason);

        data.put("leaveTime",leaveTime);
        data.put("returnTime",returnTime);
        data.put("destination",destination);

        leaveTableController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        leaveTableController.doEditClose("cancel",null);

    }
}

