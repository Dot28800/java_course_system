package com.teach.javafx.controller;

import com.teach.javafx.controller.base.LocalDateStringConverter;
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

public class InnovativeEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField hostField;
    @FXML
    private ComboBox<OptionItem> levelBox;
    @FXML
    private TextField typeField;
    private List<OptionItem> studentList;
    private List<OptionItem> levelList;
    //private HonorTableController honorTableController;
    private InnovativeController innovativeController;
    private Integer InnovativeId = null;

    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }

    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public DatePicker getTimePicker() {
        return timePicker;
    }

    public void setTimePicker(DatePicker timePicker) {
        this.timePicker = timePicker;
    }

    public TextField getHostField() {
        return hostField;
    }

    public void setHostField(TextField hostField) {
        this.hostField = hostField;
    }

    public ComboBox<OptionItem> getLevelBox() {
        return levelBox;
    }

    public void setLevelBox(ComboBox<OptionItem> levelBox) {
        this.levelBox = levelBox;
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

    public List<OptionItem> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<OptionItem> levelList) {
        this.levelList = levelList;
    }

    public Integer getInnovativeId() {
        return InnovativeId;
    }

    public void setInnovativeId(Integer innovativeId) {
        InnovativeId = innovativeId;
    }

    public void setInnovativeController(InnovativeController innovativeController) {
        this.innovativeController = innovativeController;
    }

    public InnovativeController getInnovativeController() {
        return innovativeController;
    }


    @FXML
    public void initialize(){
        timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }
    public void showDialog(Map data) {
        InnovativeId = CommonMethod.getInteger(data,"innovativeId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        levelBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(levelList, CommonMethod.getString(data, "level")));
        studentComboBox.setDisable(true);
        levelBox.setDisable(false);
        titleField.setText(CommonMethod.getString(data,"title"));
        hostField.setText(CommonMethod.getString(data, "host"));
        typeField.setText(CommonMethod.getString(data,"type"));
        timePicker.getEditor().setText(CommonMethod.getString(data, "time"));
        //preHonorField.setText(CommonMethod.getString(data,"preHonor"));
    }

    public void setCourseAttendanceController(HonorTableController honorTableController) {
    }

    public void init() {
        studentList = innovativeController.getStudentList();
        //courseList=courseChooseTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        levelList=initLevelList();
        levelBox.getItems().addAll(levelList);

    }
    public List<OptionItem> initLevelList(){
        List<OptionItem> levels=new ArrayList<>();
        levels.add(new OptionItem(0,"","院级"));
        levels.add(new OptionItem(1,"","校级"));
        levels.add(new OptionItem(2,"","省级"));
        levels.add(new OptionItem(3,"","国家级"));
        return levels;
    }
    @FXML
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
        op = levelBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("level", op.getTitle());
        }else{
            MessageDialog.showDialog("请选中获奖级别之后再点击我");
        }
        String title=titleField.getText();
        String time=timePicker.getEditor().getText();
        String host=hostField.getText();
        String type=typeField.getText();
        boolean flag=title.isEmpty() || time.isEmpty() || host.isEmpty() || type.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("innovativeId", InnovativeId);
        data.put("title",title);

        data.put("time",time);
        data.put("host",host);
        data.put("type",type);
        innovativeController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        innovativeController.doEditClose("cancel",null);

    }
}