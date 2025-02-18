package com.teach.javafx.controller;
import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> activityPlaceColumn;
    @FXML
    private TableColumn<Map,String> typeColumn;
    @FXML
    private TableColumn<Map,String> activityTimeColumn;
    @FXML
    private TableColumn<Map,String> activityEvaluationColumn;
    @FXML
    private TableColumn<Map,String> contentColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private TextField inputActivityPlace;
    @FXML
    private TextField inputActivityEvaluation;
    @FXML
    private TextField inputContent;
    @FXML
    private TextField inputActivityTime;
    @FXML
    private ComboBox<OptionItem> typeComboBox;

    private List<OptionItem> typeList;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker timePicker;
    private List<OptionItem> studentList;
    private ArrayList<Map> activityList=new ArrayList<>();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private ActivityEditController activityEditController = null;
    private Stage stage = null;
    private String num;
    private String userName;
    private String userType;
    @FXML
    private Label studentNameLabel;
    private Integer activityId=null;
    public List<OptionItem> getStudentList() {
        return studentList;
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
        if(userType.equals("管理员")) {
            studentNumColumn.setCellValueFactory(new MapValueFactory<>("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            contentColumn.setCellValueFactory(new MapValueFactory<>("content"));
            activityTimeColumn.setCellValueFactory(new MapValueFactory<>("activityTime"));
            activityPlaceColumn.setCellValueFactory(new MapValueFactory<>("activityPlace"));
            activityEvaluationColumn.setCellValueFactory(new MapValueFactory<>("activityEvaluation"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/activity/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");

            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);

            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);

            typeList = initTypeList();
            typeComboBox.getItems().addAll(item);
            typeComboBox.getItems().addAll(typeList);


            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else {
            studentNumColumn.setCellValueFactory(new MapValueFactory<>("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            contentColumn.setCellValueFactory(new MapValueFactory<>("content"));
            activityTimeColumn.setCellValueFactory(new MapValueFactory<>("activityTime"));
            activityPlaceColumn.setCellValueFactory(new MapValueFactory<>("activityPlace"));
            activityEvaluationColumn.setCellValueFactory(new MapValueFactory<>("activityEvaluation"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            studentNameLabel.setText(userName);
            OptionItem item = new OptionItem(null, "0", "请选择");
            typeList = initTypeList();
            typeComboBox.getItems().addAll(item);
            typeComboBox.getItems().addAll(typeList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }
    }
    @FXML
    void onQueryButtonClick(){
        if(userType.equals("管理员")){
            Integer studentId = 0;
            OptionItem op;
            op = studentComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                studentId = Integer.parseInt(op.getValue());

            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("studentId",studentId);

            res=HttpRequestUtil.request("/api/activity/getActivityList",req);
            if(res != null && res.getCode()== 0) {
                activityList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else {
            String type="";
            OptionItem op;
            op = typeComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                type=op.getTitle();
            if(type.equals("请选择")){
                type="";
            }
            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("type",type);
            req.add("num",num);
            req.add("name",userName);

            res=HttpRequestUtil.request("/api/activity/getActivityListByNumName",req);
            if(res != null && res.getCode()== 0) {
                activityList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }

    }
    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < activityList.size(); j++) {
            map = activityList.get(j);
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
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }
    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = activityList.get(j);

        if(stage == null){
            FXMLLoader fxmlLoader ;
            Scene scene = null;
            try {
                fxmlLoader = new FXMLLoader(MainApplication.class.getResource("activity/activity-edit-dialog.fxml"));
                scene = new Scene(fxmlLoader.load(), 1200, 800);
                stage = new Stage();
                stage.initOwner(MainApplication.getMainStage());
                stage.initModality(Modality.NONE);
                stage.setAlwaysOnTop(true);
                stage.setScene(scene);
                stage.setTitle("日常活动修改对话框");
                stage.setOnCloseRequest(event ->{
                    MainApplication.setCanClose(true);
                });
                activityEditController = fxmlLoader.getController();
                activityEditController.setActivityTableController(this);
                activityEditController.init();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        activityEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    private void deleteItem(String name) {

        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=activityList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        activityId= CommonMethod.getInteger(data,"activityId");
        DataRequest req=new DataRequest();
        req.add("activityId",activityId);
        DataResponse res=HttpRequestUtil.request("/api/activity/deleteActivity",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    @FXML
    void onNewButtonClick(){
        Integer studentId = 0;
        String newType=null;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击此框");
            return;
        }
        String newActivityTime=timePicker.getEditor().getText();
        String newActivityPlace=inputActivityPlace.getText();
        String newContent=inputContent.getText();
        String newActivityEvaluation=inputActivityEvaluation.getText();
        op=typeComboBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newType=op.getTitle();
        }

        if(newType==null){
            MessageDialog.showDialog("请选择活动类型之后再点击此框");
            return;
        }

        boolean flag=  newActivityTime.isEmpty()   || newContent.isEmpty() || newActivityPlace.isEmpty() || newActivityEvaluation.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("activityId",activityId);
        req.add("studentId",studentId);
        req.add("newActivityTime",newActivityTime);
        req.add("newActivityPlace",newActivityPlace);
        req.add("newType",newType);
        req.add("newContent",newContent);
        req.add("newActivityEvaluation",newActivityEvaluation);
        DataResponse res=HttpRequestUtil.request("/api/activity/newActivity",req);
        if(res!=null && res.getCode() == 0){
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信异常！");
        }
    }

    public void doEditClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("activityId",CommonMethod.getInteger(data,"activityId"));;
        req.add("activityPlace",CommonMethod.getString(data,"activityPlace"));
        req.add("Content",CommonMethod.getString(data,"Content"));
        req.add("activityTime",CommonMethod.getString(data,"activityTime"));
        req.add("type",CommonMethod.getString(data,"type"));
        req.add("activityEvaluation",CommonMethod.getString(data,"activityEvaluation"));
        res = HttpRequestUtil.request("/api/activity/activityEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }


    public void setInputStudentComboBox(ComboBox<OptionItem> inputStudentComboBox) {
        this.inputStudentComboBox = inputStudentComboBox;
    }
}
