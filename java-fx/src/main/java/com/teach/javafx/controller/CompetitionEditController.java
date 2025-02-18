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

public class CompetitionEditController {

    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private ComboBox<OptionItem> rankComboBox;
    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField positionField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField contestNameField;
    @FXML
    private TextField instructorField;
    @FXML
    private TextField prizeField;

    private List<OptionItem> studentList;
    private List<OptionItem> rankList;

    private CompetitionTableController competitionTableController;
    private Integer competitionId=null;


    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }

    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }

    public TextField getPositionField() {
        return positionField;
    }

    public void setPositionField(TextField titleField) {
        this.positionField = positionField;
    }

    public DatePicker getTimePicker() {
        return timePicker;
    }

    public void setTimePicker(DatePicker timePicker) {
        this.timePicker = timePicker;
    }

    public TextField getContestNameField() {
        return contestNameField;
    }

    public void setContestNameField(TextField contestNameField) {
        this.contestNameField = contestNameField;
    }

    public TextField getInstructorField() {
        return instructorField;
    }

    public void setInstructorField(TextField instructorField) {
        this.instructorField = instructorField;
    }
    public ComboBox<OptionItem> getRankComboBox() {
        return rankComboBox;
    }

    public void setRankComboBox(ComboBox<OptionItem> rankComboBox) {
        this.rankComboBox = rankComboBox;
    }

    public TextField getTypeField() {
        return typeField;
    }

    public void setTypeField(TextField typeField) {
        this.typeField = typeField;
    }
    public TextField getPrizeField() {
        return prizeField;
    }

    public void setPrizeField(TextField prizeField) {
        this.prizeField = prizeField;
    }

    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }

    public List<OptionItem> getRankList() {
        return rankList;
    }

    public void setRankList(List<OptionItem> rankList) {
        this.rankList = rankList;
    }

    public CompetitionTableController getCompetitionTableController() {
        return competitionTableController;
    }

    public void setCompetitionTableController(CompetitionTableController competitionTableController) {
        this.competitionTableController = competitionTableController;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    @FXML
    public void initialize(){

    }

    public void init() {
        studentList=competitionTableController.getStudentList();
        //courseList=courseChooseTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        rankList=initRankList();
        rankComboBox.getItems().addAll(rankList);

    }

    public List<OptionItem> initRankList(){
        List<OptionItem> ranks  = new ArrayList<>();
        ranks.add(new OptionItem(0,"","国际顶级"));
        ranks.add(new OptionItem(1,"","国家级"));
        ranks.add(new OptionItem(2,"","省级"));
        ranks.add(new OptionItem(3,"","校级"));
        ranks.add(new OptionItem(4,"","院级"));
        return ranks;
    }

    public void showDialog(Map data) {
        competitionId= CommonMethod.getInteger(data,"competitionId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        rankComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(rankList, CommonMethod.getString(data, "level")));
        studentComboBox.setDisable(true);
        rankComboBox.setDisable(false);
        contestNameField.setText(CommonMethod.getString(data,"contestName"));
        positionField.setText(CommonMethod.getString(data, "position"));
        instructorField.setText(CommonMethod.getString(data, "instructor"));
        prizeField.setText(CommonMethod.getString(data, "prize"));
        typeField.setText(CommonMethod.getString(data,"type"));
        timePicker.getEditor().setText(CommonMethod.getString(data, "contestTime"));

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
        op = rankComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("rank", op.getTitle());
        } else {
            MessageDialog.showDialog("请选中获奖级别之后再点击我");
        }
        String position =positionField.getText();
        String time=timePicker.getEditor().getText();
        String contestName=contestNameField.getText();
        String instructor=instructorField.getText();
        String type=typeField.getText();
        String prize=prizeField.getText();
        boolean flag=position.isEmpty() || time.isEmpty() || contestName.isEmpty() || type.isEmpty() || instructor.isEmpty() ||prize.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("competitionId",competitionId);
        data.put("position",position);

        data.put("time",time);
        data.put("contestName",contestName);
        data.put("type",type);
        data.put("prize",prize);
        data.put("instructor",instructor);
        competitionTableController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        competitionTableController.doEditClose("cancel",null);

    }
}
