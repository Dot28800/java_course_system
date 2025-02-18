package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.CourseAttendanceEditController;
import com.teach.javafx.controller.base.LocalDateStringConverter;
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

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HonorTableController {

    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> titleColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> timeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> hostColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> typeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> levelColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputTitle;
    @FXML
    private TextField inputType;
    @FXML
    private ComboBox<OptionItem> levelBox;
    @FXML
    private TextField inputHost;
    @FXML
    private DatePicker timePicker;

    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    private List<OptionItem> levelList;

    private ArrayList<Map> honorList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private List<OptionItem> studentList;
    private HonorEditController honorEditController = null;


    private Stage stage = null;
    private Integer honorId=null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }
    private String num;
    private String userName;
    private String userType;
    @FXML
    private Label studentNameLabel;

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
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            hostColumn.setCellValueFactory(new MapValueFactory<>("host"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            levelColumn.setCellValueFactory(new MapValueFactory<>("level"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            titleColumn.setCellValueFactory(new MapValueFactory<>("title"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/honor/getStudentItemOptionList", req1);
            OptionItem item = new OptionItem(null, "0", "请选择");
            levelList = initLevelList();
            levelBox.getItems().addAll(item);
            levelBox.getItems().addAll(levelList);
            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);
            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else{
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            hostColumn.setCellValueFactory(new MapValueFactory<>("host"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            levelColumn.setCellValueFactory(new MapValueFactory<>("level"));
            titleColumn.setCellValueFactory(new MapValueFactory<>("title"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            studentNameLabel.setText(userName);
            DataRequest req1 = new DataRequest();
            //studentList = HttpRequestUtil.requestOptionItemList("/api/honor/getStudentItemOptionList", req1);
            OptionItem item = new OptionItem(null, "0", "请选择");
            levelList = initLevelList();
            levelBox.getItems().addAll(item);
            levelBox.getItems().addAll(levelList);
            /*studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);
            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);*/
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }
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

            res=HttpRequestUtil.request("/api/honor/getHonorList",req);
            if(res != null && res.getCode()== 0) {
                honorList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
            setTableViewData();
        }else{
            String level="";
            DataRequest req=new DataRequest();
            req.add("num",num);
            req.add("name",userName);
            OptionItem op=levelBox.getSelectionModel().getSelectedItem();
            if(op!=null){
                level=op.getTitle();
            }
            if(level.equals("请选择")){
                level="";
            }
            req.add("level",level);
            DataResponse res=HttpRequestUtil.request("/api/honor/getHonorListByNumName",req);
            if(res != null && res.getCode()== 0) {
                honorList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
            setTableViewData();
        }



    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < honorList.size(); j++) {
            map = honorList.get(j);
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

    private void deleteItem(String name) {
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=honorList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        honorId= CommonMethod.getInteger(data,"honorId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("honorId",honorId);
        DataResponse res=HttpRequestUtil.request("/api/honor/deleteHonor",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = honorList.get(j);
        initDialog();
        honorEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Honor/honor-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("荣誉信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            honorEditController = (HonorEditController) fxmlLoader.getController();
            honorEditController.setHonorTableController(this);
            honorEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onNewButtonClick(){
        Integer studentId = 0;
        String newLevel=null;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        //newLevel=levelBox.getSelectionModel().getSelectedItem().getTitle();//补充的地方
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击我");
            return;
        }
        String newTime=timePicker.getEditor().getText();
        String newTitle=inputTitle.getText();
        String newType=inputType.getText();
        String newHost=inputHost.getText();
        op=levelBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newLevel=op.getTitle();
        }
        if(newLevel==null){
            MessageDialog.showDialog("请选择荣誉奖项级别之后再点击我");
            return;
        }

        boolean flag=  newTime.isEmpty() || newType.isEmpty() || newTitle.isEmpty() || newHost.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击我");
            return;
        }
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("newTime",newTime);
        req.add("newTitle",newTitle);
        req.add("newType",newType);
        req.add("newHost",newHost);
        req.add("newLevel",newLevel);
        DataResponse res=HttpRequestUtil.request("/api/honor/newHonor",req);
        if(res!=null && res.getCode() == 0) {
            //honorId = CommonMethod.getIntegerFromObject(res.getData());
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
        req.add("honorId",CommonMethod.getInteger(data,"honorId"));
        req.add("level",CommonMethod.getString(data,"level"));
        req.add("title",CommonMethod.getString(data,"title"));
        req.add("host",CommonMethod.getString(data,"host"));
        req.add("time",CommonMethod.getString(data,"time"));
        req.add("type",CommonMethod.getString(data,"type"));
        res = HttpRequestUtil.request("/api/honor/honorEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }


}
