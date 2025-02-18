package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController 登录交互控制类 对应 course-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class CourseController {//多个共享同一方法可以将方法写到service里面并注入service对象
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,Integer> creditColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;
    @FXML
    private TableColumn<Map,String> positionColumn;
    @FXML
    private TableColumn<Map,Integer> resourceColumn;
    @FXML
    private TableColumn<Map,String> preCourseColumn;
    @FXML
    private TableColumn<Map,Integer> typeColumn;
    @FXML
    private TableColumn<Map,String> examColumn;

    @FXML
    private TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField numField;
    @FXML
    private TextField creditField;
    @FXML
    private TextField inputField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField resourceField;
    @FXML
    private TextField examField;
    @FXML
    private TextField preCourseField;
    @FXML
    private ComboBox<OptionItem> typeComboBox;

    private List<OptionItem> typeList;

    private Stage stage = null;

    private Integer courseId;
    private CourseEditController courseEditController=null;

    private List<Map> courseList = new ArrayList();  // 学生信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    public List<OptionItem> initTypeList(){
        List<OptionItem> ranks  = new ArrayList<>();
        ranks.add(new OptionItem(0,"","必修课"));
        ranks.add(new OptionItem(1,"","限选课"));
        ranks.add(new OptionItem(2,"","通选课"));
        return ranks;
    }
    @FXML
    private void onQueryButtonClick(){
        String numName = inputField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        DataResponse res;
        res = HttpRequestUtil.request("/api/course/getCourseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map=new HashMap<>();
        Button editButton;
        Button deleteButton;

        for (int j = 0; j < courseList.size(); j++) {
            map = courseList.get(j);
            editButton = new Button("修改");
            editButton.setId("edit"+j);
            deleteButton=new Button("删除");
            deleteButton.setId("delete"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(courseList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    private void deleteItem(String name) {
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=courseList.get(j);
        int jet= MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        Integer courseId=CommonMethod.getInteger(data,"courseId");
        DataRequest req=new DataRequest();
        req.add("courseId",courseId);
        DataResponse res=HttpRequestUtil.request("/api/course/courseDelete",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除课程成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = courseList.get(j);
        initDialog();
        courseEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void showDialog(Map data) {
    }

    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course/course-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 700, 1000);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("考勤信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            courseEditController = (CourseEditController) fxmlLoader.getController();
            courseEditController.setCourseController(this);
            courseEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory("name"));
        creditColumn.setCellValueFactory(new MapValueFactory("credit"));
        editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
        deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
        timeColumn.setCellValueFactory(new MapValueFactory("time"));  //设置列值工程属性
        typeColumn.setCellValueFactory(new MapValueFactory("type"));
        positionColumn.setCellValueFactory(new MapValueFactory("position"));
        resourceColumn.setCellValueFactory(new MapValueFactory<>("resource"));
        preCourseColumn.setCellValueFactory(new MapValueFactory<>("preCourse"));
        examColumn.setCellValueFactory(new MapValueFactory<>("exam"));
        //preCourseColumn.setCellValueFactory(new MapValueFactory("preCourse"));
        typeList=initTypeList();

        OptionItem item = new OptionItem(null,"0","请选择");
        typeComboBox.getItems().addAll(item);
        typeComboBox.getItems().addAll(typeList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    @FXML
    public void onNewButtonClick(){
        String newType=null;
        OptionItem op;
        String newNum=numField.getText();
        String newName=nameField.getText();
        String newCredit=creditField.getText();
        String newTime=timeField.getText();
        String newExam=examField.getText();
        String newPosition=positionField.getText();
        String newPreCourse=preCourseField.getText();
        String newResource=resourceField.getText();
        //String newPreCourse=preCourseField.getText();
        op=typeComboBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newType=op.getTitle();
        }

        if(newType==null){
            MessageDialog.showDialog("请选择课程类别之后再点击此框");
            return;
        }
        DataRequest req=new DataRequest();
        req.add("num",newNum);
        req.add("name",newName);
        req.add("credit",newCredit);
        req.add("time",newTime);
        req.add("exam",newExam);
        req.add("resource",newResource);
        req.add("position",newPosition);
        req.add("preCourse",newPreCourse);
        req.add("type",newType);
        // req.add("credit",newCredit);
        //req.add("preCourse",newPreCourse);
        DataResponse res=HttpRequestUtil.request("/api/course/newCourse",req);
        if(res.getCode() == 0) {
            courseId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("添加课程成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        String num= CommonMethod.getString(data,"num");
        if(num == null || num.isEmpty()) {
            MessageDialog.showDialog("没有课序号不能添加保存！");
            return;
        }
        String name = CommonMethod.getString(data,"name");
        if(name == null || name.isEmpty()) {
            MessageDialog.showDialog("没有课程名称不能添加保存！");
            return;
        }
        String credit = CommonMethod.getString(data,"credit");
        if(credit == null || credit.isEmpty()) {
            MessageDialog.showDialog("没有课程学分不能添加保存！");
            return;
        }
        String time = CommonMethod.getString(data,"time");
        if(time  == null || time.isEmpty()) {
            MessageDialog.showDialog("没有学时不能添加保存！");
            return;
        }
        String  exam = CommonMethod.getString(data,"exam");
        if(exam == null || exam.isEmpty()) {
            MessageDialog.showDialog("没有考核方式不能添加保存！");
            return;
        }
        String resource= CommonMethod.getString(data,"resource");
        if(resource == null || resource.isEmpty()) {
            MessageDialog.showDialog("没有参考资料不能添加保存！");
            return;
        }
        String position = CommonMethod.getString(data,"position");
        if(position == null || position.isEmpty()) {
            MessageDialog.showDialog("没有上课地点不能添加保存！");
            return;
        }
        String preCourse= CommonMethod.getString(data,"preCourse");
        if(preCourse == null || preCourse.isEmpty()) {
            MessageDialog.showDialog("没有前修课程不能添加保存！");
            return;
        }
        /*if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/
        DataRequest req =new DataRequest();

        req.add("courseId",CommonMethod.getInteger(data,"courseId"));
        req.add("credit",CommonMethod.getString(data,"credit"));
        req.add("num",CommonMethod.getString(data,"num"));
        req.add("type",CommonMethod.getString(data,"type"));
        req.add("name",CommonMethod.getString(data,"name"));
        req.add("position",CommonMethod.getString(data,"position"));
        req.add("preCourse",CommonMethod.getString(data,"preCourse"));
        req.add("resource",CommonMethod.getString(data,"resource"));
        req.add("exam",CommonMethod.getString(data,"exam"));
        req.add("time",CommonMethod.getString(data,"time"));
        res = HttpRequestUtil.request("/api/course/courseSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }




}

/*package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController 登录交互控制类 对应 course-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
/*public class CourseController {
    public TableColumn timeColumn;//多个共享同一方法可以将方法写到service里面并注入service对象
    public TableColumn typeColumn;
    public TableColumn examColumn;
    public TableColumn positionColumn;
    public TableColumn resourceColumn;
    public TextField timeField;
    public TextField positionField;
    public ComboBox typeComboBox;
    public TextField resourceField;
    public TextField examField;
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,Integer> creditColumn;
    @FXML
    private TableColumn<Map,String> preCourseColumn;
    @FXML
    private TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField numField;
    @FXML
    private TextField creditField;
    @FXML
    private TextField inputField;
    private Stage stage = null;
    @FXML
    private TextField preCourseField;
    private Integer courseId;
    private CourseEditController courseEditController=null;

    private List<Map> courseList = new ArrayList();  // 学生信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    @FXML
    private void onQueryButtonClick(){
        String numName = inputField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        DataResponse res;
        res = HttpRequestUtil.request("/api/course/getCourseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map=new HashMap<>();
        Button editButton;
        Button deleteButton;

        for (int j = 0; j < courseList.size(); j++) {
            map = courseList.get(j);
            editButton = new Button("修改");
            editButton.setId("edit"+j);
            deleteButton=new Button("删除");
            deleteButton.setId("delete"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(courseList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    private void deleteItem(String name) {
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=courseList.get(j);
        int jet= MessageDialog.choiceDialog("该操作会将与其关联的其他数据也一并删除，确定要删除吗？");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        Integer courseId=CommonMethod.getInteger(data,"courseId");
        DataRequest req=new DataRequest();
        req.add("courseId",courseId);
        DataResponse res=HttpRequestUtil.request("/api/course/courseDelete",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除课程成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = courseList.get(j);
        initDialog();
        courseEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void showDialog(Map data) {
    }

    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course/course-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("考勤信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            courseEditController = (CourseEditController) fxmlLoader.getController();
            courseEditController.setCourseController(this);
            courseEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new MapValueFactory("name"));
        creditColumn.setCellValueFactory(new MapValueFactory("credit"));
        editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
        deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
        //preCourseColumn.setCellValueFactory(new MapValueFactory("preCourse"));
        onQueryButtonClick();
    }
    @FXML
    public void onNewButtonClick(){
        String newNum=numField.getText();
        String newName=nameField.getText();
        String newCredit=creditField.getText();
        //String newPreCourse=preCourseField.getText();
        DataRequest req=new DataRequest();
        req.add("num",newNum);
        req.add("name",newName);
        req.add("credit",newCredit);
        //req.add("preCourse",newPreCourse);
        DataResponse res=HttpRequestUtil.request("/api/course/newCourse",req);
        if(res.getCode() == 0) {
            courseId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("添加课程成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        String num= CommonMethod.getString(data,"num");
        if(num == null || num.isEmpty()) {
            MessageDialog.showDialog("没有课序号不能添加保存！");
            return;
        }
        String name = CommonMethod.getString(data,"name");
        if(name == null || name.isEmpty()) {
            MessageDialog.showDialog("没有课程名称不能添加保存！");
            return;
        }
        String credit = CommonMethod.getString(data,"credit");
        if(credit == null || credit.isEmpty()) {
            MessageDialog.showDialog("没有课程学分不能添加保存！");
            return;
        }
        if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }
        DataRequest req =new DataRequest();

        req.add("courseId",CommonMethod.getInteger(data,"courseId"));
        req.add("credit",CommonMethod.getString(data,"credit"));
        req.add("num",CommonMethod.getString(data,"num"));
        req.add("name",CommonMethod.getString(data,"name"));
        res = HttpRequestUtil.request("/api/course/courseSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else{

            MessageDialog.showDialog(res.getMsg());
        }

    }




}*/
