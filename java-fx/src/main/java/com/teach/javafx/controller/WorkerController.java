package com.teach.javafx.controller;

import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.Worker;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import com.teach.javafx.controller.base.MessageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class WorkerController extends ToolController {
    @FXML
    private TableView<Worker> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Worker,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Worker,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Worker,String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<Worker,String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域
    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域

    private Integer workerId = null;  //当前编辑修改的学生的主键

    private List workerList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Worker> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < workerList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList((Worker)workerList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("numName","");
        workerList= HttpRequestUtil.requestDataList("/api/worker/getWorkerList",req);
        int a=5;
        /*numColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Worker, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> cellData) {
                return new SimpleStringProperty(cellData.getValue().getPerson().getNum());
            }
        });*/
        nameColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPerson().getName()));

        TableView.TableViewSelectionModel<Worker> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));



    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        workerId = null;
        numField.setText("");
        nameField.setText("");

        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");

    }

    protected void changeStudentInfo() {
        Worker worker = dataTableView.getSelectionModel().getSelectedItem();
        if(worker == null) {
            clearPanel();
            return;
        }
        workerId = worker.getWorkerId();
        DataRequest req = new DataRequest();
        req.add("workerId",workerId);
        worker = (Worker)HttpRequestUtil.requestDataObject("/api/worker/getWorkerInfo",req);
        numField.setText(worker.getPerson().getNum());
        nameField.setText(worker.getPerson().getName());

        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, worker.getPerson().getGender()));
        birthdayPick.getEditor().setText(worker.getPerson().getBirthday());


    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        workerList =  (List<Teacher>)HttpRequestUtil.requestDataList("/api/teacher/getTeacherList",req);
        setTableViewData();
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
     */
    /*@FXML
    protected void onDeleteButtonClick() {
        Worker worker = dataTableView.getSelectionModel().getSelectedItem();
        if(worker == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        workerId = worker.getWorkerId();
        DataRequest req = new DataRequest();
        req.add("workerId", workerId);
        DataResponse res = HttpRequestUtil.request("/api/worker/teacherDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }*/
    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Teacher t = new Teacher();
        Person p = new Person();
        t.setPerson(p);
        p.setNum(numField.getText());
        p.setName(nameField.getText());

        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            p.setGender(genderComboBox.getSelectionModel().getSelectedItem().getValue());
        p.setBirthday(birthdayPick.getEditor().getText());

        DataRequest req = new DataRequest();
        req.add("workerId", workerId);
        req.add("form", t);
        DataResponse res = HttpRequestUtil.request("/api/worker/workerEditSave",req);
        if(res.getCode() == 0) {
            workerId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /*public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }*/
    /*public void doDelete(){
        onDeleteButtonClick();
    }*/


}

