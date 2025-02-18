package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class LeaveTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> leaveReasonColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> leaveTimeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> returnTimeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> destinationColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> flagColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> agreeColumn;
    @FXML
    private TableColumn<Map,Button> negateColumn;
    @FXML
    private TextField inputLeaveReason;
    @FXML
    private ComboBox<OptionItem> inputFlagComboBox;
    @FXML
    private TextField inputDestination;
    @FXML
    private TextField inputFlag;
    @FXML
    private TextField inputReturnTime;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker returnTimePicker;
    @FXML
    private DatePicker leaveTimePicker;

    private List<OptionItem> flagList;
    private List<OptionItem> studentList;
    private ArrayList<Map> leaveList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    //private CompetitionTableController competitionTableController = null;
    private LeaveEditController leaveEditController = null;

    private Stage stage = null;
    private Integer leaveId=null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }
    private String num;
    private String userName;
    private String userType;
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
            leaveReasonColumn.setCellValueFactory(new MapValueFactory<>("leaveReason"));
            leaveTimeColumn.setCellValueFactory(new MapValueFactory<>("leaveTime"));
            returnTimeColumn.setCellValueFactory(new MapValueFactory<>("returnTime"));
            destinationColumn.setCellValueFactory(new MapValueFactory<>("destination"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            flagColumn.setCellValueFactory(new MapValueFactory<>("flag"));
            agreeColumn.setCellValueFactory(new MapValueFactory<>("agree"));
            negateColumn.setCellValueFactory(new MapValueFactory<>("negate"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/leave/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合
            OptionItem item = new OptionItem(null, "0", "请选择");
            flagList = initFlagList();
            inputFlagComboBox.getItems().addAll(item);
            inputFlagComboBox.getItems().addAll(flagList);
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
            leaveReasonColumn.setCellValueFactory(new MapValueFactory<>("leaveReason"));
            leaveTimeColumn.setCellValueFactory(new MapValueFactory<>("leaveTime"));
            returnTimeColumn.setCellValueFactory(new MapValueFactory<>("returnTime"));
            destinationColumn.setCellValueFactory(new MapValueFactory<>("destination"));
            flagColumn.setCellValueFactory(new MapValueFactory<>("flag"));
            DataRequest req1 = new DataRequest();
            //studentList = HttpRequestUtil.requestOptionItemList("/api/leave/getByFlag", req1); //从后台获取所有学生信息列表集合
            OptionItem item = new OptionItem(null, "0", "请选择");
            /*flagList = initFlagList();
            inputFlagComboBox.getItems().addAll(item);
            inputFlagComboBox.getItems().addAll(flagList);
            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);
            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);*/
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();

        }
    }
    public List<OptionItem> initFlagList(){
        List<OptionItem> flags  = new ArrayList<>();
        flags.add(new OptionItem(0,"","是"));
        flags.add(new OptionItem(1,"","否"));
        return flags;
    }
    @FXML
    void getAll(){
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("num",num);
        req.add("name",userName);

        res=HttpRequestUtil.request("/api/leave/getByFlag",req);
        if(res != null && res.getCode()== 0) {
            leaveList = (ArrayList<Map>)res.getData();
            setTableViewData();
            MessageDialog.showDialog("返回全部记录成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误！");
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

            res=HttpRequestUtil.request("/api/leave/getLeaveList",req);
            if(res != null && res.getCode()== 0) {
                leaveList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else{
            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("num",num);
            req.add("name",userName);

            res=HttpRequestUtil.request("/api/leave/getByFlag",req);
            if(res != null && res.getCode()== 0) {
                leaveList = (ArrayList<Map>)res.getData();
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
        Button agreeButton;
        Button negateButton;
        for (int j = 0; j < leaveList.size(); j++) {
            map = leaveList.get(j);
            editButton = new Button("修改");
            editButton.setId("edit"+j);
            deleteButton=new Button("删除");
            deleteButton.setId("delete"+j);
            agreeButton=new Button("批准");
            agreeButton.setId("agree"+j);
            negateButton=new Button("否决");
            negateButton.setId("negate"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            agreeButton.setOnAction(e->{
                        agreeItem(((Button)e.getSource()).getId());
                    }
                    );
            negateButton.setOnAction(e->{
                negateItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            map.put("delete",deleteButton);
            map.put("agree",agreeButton);
            map.put("negate",negateButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }
    void negateItem(String name){
        if(name==null){
            return;
        }
        int j=Integer.parseInt(name.substring(6,name.length()));
        Map data=leaveList.get(j);
        int jet=MessageDialog.choiceDialog("确定要否决吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        leaveId= CommonMethod.getInteger(data,"leaveId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("leaveId",leaveId);
        DataResponse res=HttpRequestUtil.request("/api/leave/negate",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("否决成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }
    }
    @FXML
    private void onAllAgreeButton(){
        int jet=MessageDialog.choiceDialog("确定要全部批准吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        DataResponse res=HttpRequestUtil.request("/api/leave/allAgree",new DataRequest());
        if(res.getCode()==0){
            MessageDialog.showDialog("一键销假成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }
    }
    private void agreeItem(String name){
        if(name==null){
            return;
        }
        int j=Integer.parseInt(name.substring(5,name.length()));
        Map data=leaveList.get(j);
        int jet=MessageDialog.choiceDialog("确定要批准吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        leaveId= CommonMethod.getInteger(data,"leaveId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("leaveId",leaveId);
        DataResponse res=HttpRequestUtil.request("/api/leave/agree",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("销假成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }
    }

    private void deleteItem(String name) {

        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=leaveList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        leaveId= CommonMethod.getInteger(data,"leaveId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("leaveId",leaveId);
        DataResponse res=HttpRequestUtil.request("/api/leave/deleteLeave",req);
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
        Map data = leaveList.get(j);
        initDialog();
        leaveEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void initDialog(){
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Leave/leave-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 1400, 1000);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("请假记录修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            leaveEditController = (LeaveEditController) fxmlLoader.getController();
            leaveEditController.setLeaveTableController(this);
            leaveEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onNewButtonClick(){

        if(userType.equals("管理员")){
            Integer studentId = 0;
            String newFlag=null;
            OptionItem op;
            op = inputStudentComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                studentId=Integer.parseInt(op.getValue());
            else{
                MessageDialog.showDialog("请选择学生后再点击此框");
                return;
            }
            String newReturnTime=returnTimePicker.getEditor().getText();
            String newLeaveTime=leaveTimePicker.getEditor().getText();
            String newDestination=inputDestination.getText();
            String newLeaveReason=inputLeaveReason.getText();
            op=inputFlagComboBox.getSelectionModel().getSelectedItem();
            if(op!=null){
                newFlag=op.getTitle();
            }

            if(newFlag==null){
                MessageDialog.showDialog("请选择是否销假之后再点击此框");
                return;
            }

            boolean flag=  newReturnTime.isEmpty() || newLeaveTime.isEmpty() || newDestination.isEmpty() || newLeaveReason.isEmpty() ;
            if(flag){
                MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
                return;
            }

            DataRequest req =new DataRequest();
            req.add("studentId",studentId);
            req.add("flag",newFlag);
            req.add("leaveTime",newLeaveTime);
            req.add("returnTime",newReturnTime);
            req.add("leaveReason",newLeaveReason);
            req.add("destination",newDestination);

            DataResponse res=HttpRequestUtil.request("/api/leave/newLeave",req);
            if(res!=null && res.getCode() == 0){
                MessageDialog.showDialog("新建记录成功！");
                onQueryButtonClick();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信异常！");
            }
        }else{
            Integer studentId = 0;
            String newFlag=null;
            /*OptionItem op;
            op = inputStudentComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                studentId=Integer.parseInt(op.getValue());
            else{
                MessageDialog.showDialog("请选择学生后再点击此框");
                return;
            }*/
            String newReturnTime=returnTimePicker.getEditor().getText();
            String newLeaveTime=leaveTimePicker.getEditor().getText();
            String newDestination=inputDestination.getText();
            String newLeaveReason=inputLeaveReason.getText();
            /*op=inputFlagComboBox.getSelectionModel().getSelectedItem();
            if(op!=null){
                newFlag=op.getTitle();
            }*/

            /*if(newFlag==null){
                MessageDialog.showDialog("请选择是否销假之后再点击此框");
                return;
            }*/

            boolean flag=  newReturnTime.isEmpty() || newLeaveTime.isEmpty() || newDestination.isEmpty() || newLeaveReason.isEmpty() ;
            if(flag){
                MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
                return;
            }

            DataRequest req =new DataRequest();
            req.add("num",num);
            req.add("flag","待批准");
            req.add("leaveTime",newLeaveTime);
            req.add("returnTime",newReturnTime);
            req.add("leaveReason",newLeaveReason);
            req.add("destination",newDestination);

            DataResponse res=HttpRequestUtil.request("/api/leave/studentNew",req);
            if(res!=null && res.getCode() == 0){
                MessageDialog.showDialog("请假成功，请耐心等待管理老师审核哦！");
                onQueryButtonClick();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信异常！");
            }
        }

    }

    public void doEditClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("leaveId",CommonMethod.getInteger(data,"leaveId"));
        req.add("flag",CommonMethod.getString(data,"flag"));
        req.add("destination",CommonMethod.getString(data,"destination"));
        req.add("returnTime",CommonMethod.getString(data,"returnTime"));
        req.add("leaveTime",CommonMethod.getString(data,"leaveTime"));
        req.add("leaveReason",CommonMethod.getString(data,"leaveReason"));

        res = HttpRequestUtil.request("/api/leave/leaveEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
    @FXML
    public void filter() {
        DataRequest req=new DataRequest();
        req.add("flag","否");
        DataResponse res=HttpRequestUtil.request("/api/leave/getByFlag",req);
        if(res != null && res.getCode()== 0) {
            leaveList = (ArrayList<Map>)res.getData();
            setTableViewData();
            //onQueryButtonClick();
            MessageDialog.showDialog("过滤成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
}
