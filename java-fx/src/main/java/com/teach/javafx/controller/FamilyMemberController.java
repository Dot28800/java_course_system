package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.XMLFormatter;

public class FamilyMemberController extends ToolController {
    @FXML
    private TableColumn<Map,String> jobColumn;

    private Stage stage=null;
    @FXML
    private ComboBox<OptionItem> MemberComboBox;
    public TextField nameField;
    public TableColumn<Map,Button> editColumn;
    private String memberId=null;
    private ArrayList<Map> memberList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    @FXML
    private Label userName;
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> memberIdColumn;
    @FXML
    private TableColumn<Map,String> studentIdColumn;

    @FXML
    private TableColumn<Map,String> memberNameColumn;
    @FXML
    private TableColumn<Map,String> genderColumn;
    @FXML
    private TableColumn<Map,String> relationColumn;
    @FXML
    private TableColumn<Map,Integer> ageColumn;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;

    @FXML
    private TextField memberIdField;
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField memberNameField;
    @FXML
    private TextField genderField;
    private String personNum;
    @FXML
    private ComboBox<OptionItem> genderComboBox;
    String user;
    String userType;
    @FXML
    private TextField ageField;
    @FXML
    private TextField relationField;
    private MemberDialogController memberDialogController;

    public List<OptionItem> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<OptionItem> genderList) {
        this.genderList = genderList;
    }

    @FXML
    public void initialize(){
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        userType = (String) data.get("typeId");
        personNum= (String) data.get("personNum");
        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }
        if (userType.equals("管理员")) {
            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("numIdName","");
            res= HttpRequestUtil.request("/api/family_member/getFamilyMemberList",req);
            if(res != null && res.getCode()== 0) {//只有200才会返回非空
                memberList = (ArrayList<Map>)res.getData();
            }
            genderList=initGender();
            memberIdColumn.setCellValueFactory(new MapValueFactory<>("familyMemberId"));
            studentIdColumn.setCellValueFactory(new MapValueFactory<>("studentId"));
            numColumn.setCellValueFactory(new MapValueFactory<>("num"));
            nameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            memberNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            relationColumn.setCellValueFactory(new MapValueFactory<>("relationship"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
            ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            jobColumn.setCellValueFactory((new MapValueFactory<>("job")));
            DataRequest req1 =new DataRequest();

            TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
            ObservableList<Integer> list = tsm.getSelectedIndices();
            list.addListener(this::onTableRowSelect);
            setTableViewData();
        }else{
            DataResponse res;
            DataRequest req =new DataRequest();

            req.add("numIdName",personNum);
            res= HttpRequestUtil.request("/api/family_member/getFamilyMemberList",req);
            if(res != null && res.getCode()== 0) {//只有200才会返回非空
                memberList = (ArrayList<Map>)res.getData();
            }
            genderList=initGender();
            memberIdColumn.setCellValueFactory(new MapValueFactory<>("familyMemberId"));
            studentIdColumn.setCellValueFactory(new MapValueFactory<>("studentId"));
            numColumn.setCellValueFactory(new MapValueFactory<>("num"));
            nameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            memberNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            relationColumn.setCellValueFactory(new MapValueFactory<>("relationship"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
            ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            jobColumn.setCellValueFactory((new MapValueFactory<>("job")));
            DataRequest req1 =new DataRequest();

            TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
            ObservableList<Integer> list = tsm.getSelectedIndices();
            list.addListener(this::onTableRowSelect);
            Map ownData= (Map) userResponse.getData();//inline选择器
            user= (String) ownData.get("userName");
            userName.setText(user);
            setTableViewData();
        }

        //onQueryButtonClick();
        /*genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");

        genderComboBox.getItems().addAll(genderList);*/
        //birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }
    List<OptionItem> initGender(){
        List<OptionItem> levels=new ArrayList<>();
        levels.add(new OptionItem(0,"","男"));
        levels.add(new OptionItem(1,"","女"));

        return levels;
    }
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < memberList.size(); j++) {

            observableList.addAll(FXCollections.observableArrayList(memberList.get(j)));
        }
        dataTableView.setItems(observableList);
    }


    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeMemberInfo();
    }

    protected void changeMemberInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        memberId = CommonMethod.getInteger(form,"familyMemberId").toString();
        DataRequest req = new DataRequest();
        req.add("familyMemberId",memberId);
        DataResponse res = HttpRequestUtil.request("/api/family_member/getMemberInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();
        memberIdField.setText(CommonMethod.getString(form, "familyMemberId"));
        studentIdField.setText(CommonMethod.getString(form, "studentId"));
        memberNameField.setText(CommonMethod.getString(form, "name"));
        ageField.setText(CommonMethod.getString(form, "age"));
        //genderField.setText(CommonMethod.getString(form, "className"));
        relationField.setText(CommonMethod.getString(form, "relationship"));
        genderField.setText(CommonMethod.getString(form,"gender"));
//        initialize();
        //genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, CommonMethod.getString(form, "gender")));
        /*birthdayPick.getEditor().setText(CommonMethod.getString(form, "birthday"));*/

//        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
//        ObservableList<Integer> list = tsm.getSelectedIndices();
//        list.addListener(this::onTableRowSelect);
//        setTableViewData();

    }
    public void clearPanel(){
        memberId = null;
        memberIdField=new TextField();
        memberIdField.setText("");
        studentIdField=new TextField();
        studentIdField.setText("");
        memberNameField=new TextField();
        memberNameField.setText("");
        ageField=new TextField();
        ageField.setText("");
        relationField=new TextField();
        relationField.setText("");
        genderField=new TextField();
        genderField.setText("");
        /*genderComboBox=new ComboBox<>();
        genderComboBox.getSelectionModel().select(-1);*/

    }
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }
    @FXML
    protected void onSaveButtonClick() {
        if( memberIdField.getText().equals("")) {
            MessageDialog.showDialog("家长序号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("familyMemberId",memberIdField.getText());
        form.put("studentId",studentIdField.getText());
        form.put("name",memberNameField.getText());
        form.put("age",ageField.getText());
        form.put("relation",relationField.getText());
        form.put("gender",genderField.getText());
        /*if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender",genderComboBox.getSelectionModel().getSelectedItem().getValue());*/
        DataRequest req = new DataRequest();
        req.add("familyMemberId", memberId);
        req.add("form", form);
       DataResponse res = HttpRequestUtil.request("/api/family_member/memberEditSave",req);
        if(res.getCode() == 0) {
            //memberId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void onQueryButtonClick() {
        //clearPanel();
        String memberName;

        //setTableViewData();
        if(userType.equals("学生")){
            memberName=user;
        }else {
            memberName=nameField.getText();
        }
        DataRequest req = new DataRequest();
        req.add("numIdName",memberName);
        //req.add("memberName","哦");
        DataResponse res = HttpRequestUtil.request("/api/family_member/getFamilyMemberList",req);
        if(res != null && res.getCode()== 0) {
            memberList= (ArrayList<Map>)res.getData();


            setTableViewData();
        }

    }
    @FXML
    public void onNewButtonClick(){
        Map form=new HashMap<>();
        form.put("studentName",user);
        form.put("num",personNum);
        form.put("userType",userType);
        //form.put("")
        initDialog("new");
        memberDialogController.showDialog(form);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    @FXML
    public void onEditButtonClick(){
        Map form = dataTableView.getSelectionModel().getSelectedItem();

        if(form == null) {
            MessageDialog.showDialog("没有选择");
            return;
        }
        form.put("userType",userType);
        initDialog("edit");
        memberDialogController.showDialog(form);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    void initDialog(String s){
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("member-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("作业信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
             memberDialogController= (MemberDialogController) fxmlLoader.getController();
            memberDialogController.setFamilyMemberController(this);
            memberDialogController.init(s);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDeleteButtonClick() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }

        DataRequest req = new DataRequest();
        req.add("memberId",form.get("familyMemberId"));
        req.add("studentName",CommonMethod.getString(form,"studentName"));
        req.add("name",CommonMethod.getString(form,"name"));
        DataResponse res = HttpRequestUtil.request("/api/family_member/memberDelete",req);
        if(res!=null && res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("连接错误！");
        }

    }
    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }

    public void onImportButtonClick() {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择家长数据表");
        fileDialog.setInitialDirectory(new File("D:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "";
        DataResponse res =HttpRequestUtil.importData("/api/term/importStudentData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
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
        String numName=CommonMethod.getString(data,"numName");
        String name=CommonMethod.getString(data,"name");
        String job=CommonMethod.getString(data,"job");
        String relation=CommonMethod.getString(data,"relation");
        String gender=CommonMethod.getString(data,"gender");
        String age=CommonMethod.getString(data,"age");
        DataRequest req=new DataRequest();
        req.add("numName",numName);
        req.add("name",name);
        req.add("job",job);
        req.add("relation",relation);
        req.add("gender",gender);
        req.add("age",age);
        req.add("type",CommonMethod.getString(data,"type"));
        res=HttpRequestUtil.request("/api/family_member/memberEditSave",req);
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        } else if (res!=null ) {
            MessageDialog.showDialog(res.getMsg());

        }else{
            MessageDialog.showDialog("通讯错误，未获得反馈");
        }
    }
}

