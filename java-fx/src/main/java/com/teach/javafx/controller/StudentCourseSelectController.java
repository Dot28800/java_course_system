package com.teach.javafx.controller;

import com.teach.javafx.controller.base.LoginController;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentCourseSelectController {

    public TableColumn<Map,String> courseNumColumn;
    public TableColumn<Map,String> typeColumn;
    public TableColumn<Map,String> examColumn;
    public TableColumn<Map,String> positionColumn;
    public TableColumn<Map,String > preCourseColumn;
    public TableColumn<Map,String> resourseColumn;
    @FXML
    public TableColumn<Map,String> timeColumn;
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> courseForStudentIdColumn;
    @FXML
    private TableColumn<Map,String> courseNameColumn;
    @FXML
    private TableColumn<Map,String> creditColumn;
    @FXML
    private TableColumn<Map,String> teacherColumn;
    @FXML
    private TableColumn<Map,Button> selectColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;

    @FXML
    private ComboBox<OptionItem> inputTermComboBox;
    @FXML
    private ComboBox<OptionItem> inputYearTermComboBox;
    @FXML
    private ComboBox<OptionItem> inputCourseComboBox;
    private List<Map> allCourseList;

    String[] courseForStudent = {"courseForStudentId","course","year_term","term","capacity","selectNum","restNum","weeks","classDay"};
    String[] course = {"courseId","courseName","credit","teacher"};
    String[] termStr = {"第一学期","暑假","第二学期","寒假"};
    String[] yearTermStr = {"第一学年","第二学年","第三学年","第四学年"};
    private String num;
    private String userName;
    private String userType;
    @FXML
    private Label studentNameLabel;



    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private List<Map> allStudentCourseList = new ArrayList<>();
    private List<Map> allSelectCourseList = new ArrayList<>();




    private void selectItem(String id){
        int index = Integer.parseInt(id.substring(6));
        Map map = allStudentCourseList.get(index);
        DataRequest req = new DataRequest();
        req.add("courseNum", CommonMethod.getString(map,"num"));
        req.add("courseName",CommonMethod.getString(map,"name"));
        req.add("studentNum", num);
        req.add("studentName",userName);
        DataResponse res = HttpRequestUtil.request("/api/courseChoose/courseSelectForStudent",req);
        if(res != null && res.getCode()== 0) {
            MessageDialog.showDialog("增选课程成功！");
            /*Map m=new HashMap()
            allStudentCourseList.add(allStudentCourseList.size(),1);*/
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误！");
        }
        onQueryButtonClick();
    }
    private void deleteItem(String id){
        int index = Integer.parseInt(id.substring(6));
        Map map = allStudentCourseList.get(index);
        DataRequest req = new DataRequest();
        req.add("courseNum",CommonMethod.getString(map,"num"));
        req.add("courseName",CommonMethod.getString(map,"name"));
        req.add("studentNum", num);
        req.add("studentName",userName);
        DataResponse res = HttpRequestUtil.request("/api/courseChoose/courseDeleteForStudent",req);
        if(res != null && res.getCode()== 0) {
            MessageDialog.showDialog("退选课程成功！");
            /*Map m=new HashMap()
            allStudentCourseList.add(allStudentCourseList.size(),1);*/
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误！");
        }
        onQueryButtonClick();
    }
    private void setTableViewData(){
        observableList.clear();
        Map map;
        Button selectButton;
        Button deleteButton;
        for (int j = 0; j < allStudentCourseList.size(); j++) {
            map = allStudentCourseList.get(j);
            selectButton = new Button("选课");
            selectButton.setId("select" + j);
            deleteButton = new Button("退选");
            deleteButton.setId("delete" + j);
            selectButton.setOnAction(e->{
                selectItem(((Button)e.getSource()).getId());
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            map.put("select",selectButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }
    private void setTermComboBox(){
        OptionItem item = new OptionItem(0,"0","请选择");
        inputTermComboBox.getItems().addAll(item);
        for (int i = 0; i < termStr.length; i++) {
            OptionItem item1 = new OptionItem(i+1,String.valueOf(i+1),termStr[i]);
            inputTermComboBox.getItems().addAll(item1);
        }
    }
    private void setYearTermComboBox(){
        OptionItem item = new OptionItem(0,"0","请选择");
        inputYearTermComboBox.getItems().addAll(item);
        for (int i = 0; i < yearTermStr.length; i++) {
            OptionItem item1 = new OptionItem(i+1,String.valueOf(i+1),yearTermStr[i]);
            inputYearTermComboBox.getItems().addAll(item1);
        }
    }


    @FXML
    public void initialize(){
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        userType = (String) data.get("typeId");
        num= (String) data.get("personNum");
        userName=(String) data.get("userName");

        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }

        courseNumColumn.setCellValueFactory(new MapValueFactory<>("num"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        positionColumn.setCellValueFactory(new MapValueFactory<>("position"));
        resourseColumn.setCellValueFactory(new MapValueFactory<>("resource"));
        typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        examColumn.setCellValueFactory(new MapValueFactory<>("exam"));
        preCourseColumn.setCellValueFactory(new MapValueFactory<>("preCourse"));
        selectColumn.setCellValueFactory(new MapValueFactory<>("select"));
        deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));


        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    @FXML
    public void onQueryButtonClick() {
        DataRequest req=new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/course/getCourseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            allStudentCourseList = (ArrayList<Map>)res.getData();
            /*Map m=new HashMap()
            allStudentCourseList.add(allStudentCourseList.size(),1);*/
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误！");
        }
        setTableViewData();
    }
    public List<OptionItem> initTypeList(){
        List<OptionItem> ranks  = new ArrayList<>();
        ranks.add(new OptionItem(0,"","必修课"));
        ranks.add(new OptionItem(1,"","限选课"));
        ranks.add(new OptionItem(2,"","通选课"));
        return ranks;
    }

    @FXML//查询当前学生已选的课程
    public void onQuerySCButtonClick(){
        DataRequest req = new DataRequest();
        req.add("studentNum", num);
        req.add("name",userName);
        DataResponse res = HttpRequestUtil.request("/api/courseChoose/getCourseSelectedList",req);
        if(res == null || res.getData() == null || res.getCode() == 1){
            MessageDialog.showDialog("未查询到当前信息");
            return;
        }
        allStudentCourseList = (ArrayList<Map>)res.getData();
        observableList.clear();
        Map map;
        //Button selectButton;
        Button deleteButton;
        for (int j = 0; j < allStudentCourseList.size(); j++) {
            map = allStudentCourseList.get(j);
            /*selectButton = new Button("选课");
            selectButton.setId("select" + j);*/
            deleteButton = new Button("退选");
            deleteButton.setId("delete" + j);
            /*selectButton.setOnAction(e->{
                selectItem(((Button)e.getSource()).getId());
            });*/
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            //map.put("select",selectButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
        /*List<Map> list = (List<Map>) res.getData();
        dataTableView.getItems().clear();
        dataTableView.getItems().addAll(list);
        observableList.clear();
        Map map;
        Button deleteButton;
        for (int j = 0; j < list.size(); j++) {
            map = list.get(j);
            deleteButton = new Button("退课");
            deleteButton.setId("delete" + j);
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);*/
    }
    @FXML
    public void onQueryNoSCButtonClick(){
        DataRequest req = new DataRequest();
        req.add("studentNum", num);
        req.add("name",userName);
        DataResponse res = HttpRequestUtil.request("/api/courseChoose/getNoSCourseForStudentList",req);
        if(res == null || res.getData() == null || res.getCode() == 1){
            MessageDialog.showDialog("未查询到当前信息");
            return;
        }
        allStudentCourseList = (ArrayList<Map>)res.getData();
        observableList.clear();
        Map map;
        //Button selectButton;
        Button selectButton;
        for (int j = 0; j < allStudentCourseList.size(); j++) {
            map = allStudentCourseList.get(j);
            /*selectButton = new Button("选课");
            selectButton.setId("select" + j);*/
            selectButton = new Button("增选");
            selectButton.setId("select" + j);
            /*selectButton.setOnAction(e->{
                selectItem(((Button)e.getSource()).getId());
            });*/
            selectButton.setOnAction(e->{
                selectItem(((Button)e.getSource()).getId());
            });
            //map.put("select",selectButton);
            map.put("select",selectButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
        /*String term = "0";
        String yearTerm = "0";

        OptionItem op;
        op = inputTermComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            term = op.getValue();
        op = inputYearTermComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            yearTerm = op.getValue();
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("term", term == null ? "0" : term);
        req.add("yearTerm", yearTerm == null ? "0" : yearTerm);

        req.add("studentNum", LoginController.nowLogin.getUsername());
        res = HttpRequestUtil.request("/api/studentCourse/getNoSCourseForStudentList",req);
        if (res == null || res.getData() == null || res.getCode() == 1){
            MessageDialog.showDialog("未查询到当前信息");
            return;
        }
        List<Map> list = (List<Map>) res.getData();
        for (Map map : list) {
            map.put("select",new Button("选课"));
            map.put("delete",new Button("退课"));
        }
        allStudentCourseList = list;
        dataTableView.getItems().clear();
        dataTableView.getItems().addAll(list);
        setTableViewData();*/
    }

}
