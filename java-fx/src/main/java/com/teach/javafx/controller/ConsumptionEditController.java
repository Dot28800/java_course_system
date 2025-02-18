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

public class ConsumptionEditController {

    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private ComboBox<OptionItem> reasonComboBox;
    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField consumePlaceField;
    @FXML
    private TextField moneyField;

    private List<OptionItem> studentList;
    private List<OptionItem> reasonList;

    private ConsumptionTableController consumptionTableController;
    private Integer consumptionId=null;


    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }

    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }

    public TextField getMoneyField() {
        return moneyField;
    }
    public void setMoneyField(TextField moneyField) {
        this.moneyField = moneyField;
    }

    public void setConsumePlaceField(TextField consumePlaceField) {
        this.consumePlaceField = consumePlaceField;
    }

    public DatePicker getTimePicker() {
        return timePicker;
    }

    public void setTimePicker(DatePicker timePicker) {
        this.timePicker = timePicker;
    }

    public ComboBox<OptionItem> getReasonComboBox() {
        return reasonComboBox;
    }

    public void setReasonComboBox(ComboBox<OptionItem> reasonComboBox) {
        this.reasonComboBox = reasonComboBox;
    }


    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }

    public List<OptionItem> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<OptionItem> reasonList) {
        this.reasonList = reasonList;
    }

    public ConsumptionTableController getConsumptionTableController() {
        return consumptionTableController;
    }

    public void setConsumptionTableController(ConsumptionTableController consumptionTableController) {
        this.consumptionTableController = consumptionTableController;
    }

    public Integer getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
    }

    @FXML
    public void initialize(){
        timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    public void init() {
        studentList=consumptionTableController.getStudentList();
        //courseList=courseChooseTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        reasonList=initReasonList();
        reasonComboBox.getItems().addAll(reasonList);
    }

    public List<OptionItem> initReasonList(){
        List<OptionItem> reasons  = new ArrayList<>();
        reasons.add(new OptionItem(0,"","饮食"));
        reasons.add(new OptionItem(1,"","出行"));
        reasons.add(new OptionItem(2,"","衣物"));
        reasons.add(new OptionItem(3,"","兴趣爱好"));
        reasons.add(new OptionItem(4,"","健康"));
        reasons.add(new OptionItem(5,"","学习"));
        reasons.add(new OptionItem(6,"","其它"));
        return reasons;
    }

    public void showDialog(Map data) {
        consumptionId= CommonMethod.getInteger(data,"consumptionId");
        studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
        reasonComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(reasonList, CommonMethod.getString(data, "consumeReason")));
        studentComboBox.setDisable(true);
        reasonComboBox.setDisable(false);
        consumePlaceField.setText(CommonMethod.getString(data,"consumePlace"));
        moneyField.setText(CommonMethod.getString(data, "money"));
        timePicker.getEditor().setText(CommonMethod.getString(data, "consumeTime"));


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
        op = reasonComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("reason", op.getTitle());
        } else {
            MessageDialog.showDialog("请选中消费原因之后再点击此处");
        }
        String consumePlace =consumePlaceField.getText();
        String time=timePicker.getEditor().getText();
        String money=moneyField.getText();
        if(!isValidAmount(money)){
            MessageDialog.showDialog("金额格式不符合要求（除了数字字符外只允许有小数点，且最多只能有两位小数）");
            return;
        }
        boolean flag=consumePlace.isEmpty() || time.isEmpty() || money==null ;
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        data.put("consumptionId",consumptionId);
        data.put("consumePlace",consumePlace);

        data.put("time",time);
        data.put("money",money);
        consumptionTableController.doEditClose("ok",data);

    }
    private boolean isValidAmount(String amount) {
        // 正则表达式：
        // ^ 表示字符串开始
        // \d+ 表示一个或多个数字
        // (\.\d{1,2})? 表示小数部分（可选），点号后面跟着1到2位数字
        // $ 表示字符串结束
        // 注意：这个正则表达式不会处理逗号或空格作为千位分隔符的情况
        String regex = "^\\d+(\\.\\d{1,2})?$";
        return amount.matches(regex);
    }
    @FXML
    public void cancelButtonClick(){
        consumptionTableController.doEditClose("cancel",null);

    }
}

