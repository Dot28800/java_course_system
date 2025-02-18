package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import com.teach.javafx.controller.base.MessageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class StudentController extends ToolController {
    private Stage stage=null;
    @FXML
    private TableView<Map> dataTableView;  //学生信息表
    @FXML
    private TableColumn<Map,String> dressColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<Map,String> deptColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<Map,String> majorColumn; //学生信息表 专业列
    @FXML
    private TableColumn<Map,String> classNameColumn; //学生信息表 班级列
    @FXML
    private TableColumn<Map,String> cardColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<Map,String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<Map,String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<Map,String> emailColumn; //学生信息表 邮箱列
    @FXML
    private TableColumn<Map,String> phoneColumn; //学生信息表 电话列
    @FXML
    private TableColumn<Map,String> addressColumn;//学生信息表 地址列

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField majorField; //学生信息  专业输入域
    @FXML
    private TextField classNameField; //学生信息  班级输入域
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域
    @FXML
    private TableColumn<Map,String> ageColumn;

    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer studentId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> studentList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    private StudentNewController studentNewController;

    private StudentEditController studentEditController;
    private boolean flag=false;
    @FXML
    private Label ownName;
    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < studentList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(studentList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        String userType = (String) data.get("typeId");
        String num= (String) data.get("personNum");

        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }
        if (userType.equals("管理员")) {
            DataResponse res;
            DataRequest req = new DataRequest();
            req.add("numName", "");//为空，查询所有学生
            //req.add("studentId","");//为空，查询所有学生
            res = HttpRequestUtil.request("/api/student/getStudentList", req); //从后台获取所有学生信息列表集合
            if (res != null && res.getCode() == 0) {//只有200才会返回非空
                studentList = (ArrayList<Map>) res.getData();
            }
            numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
            nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            deptColumn.setCellValueFactory(new MapValueFactory<>("dept"));
            majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
            classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
            cardColumn.setCellValueFactory(new MapValueFactory<>("card"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
            birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
            //emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
            ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            phoneColumn.setCellValueFactory(new MapValueFactory<>("phone"));
            addressColumn.setCellValueFactory(new MapValueFactory<>("address"));
            emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
            TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
            genderColumn.setCellValueFactory(new MapValueFactory<>("genderName"));
            ObservableList<Integer> list = tsm.getSelectedIndices();
            //list.addListener(this::onTableRowSelect);
            setTableViewData();
            genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");

            //genderComboBox.getItems().addAll(genderList);
            //birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        }
        else{
            DataResponse res;
            DataRequest req = new DataRequest();

            req.add("numName", num);//为空，查询所有学生
            //req.add("studentId","");//为空，查询所有学生
            res = HttpRequestUtil.request("/api/student/getStudentList", req); //从后台获取所有学生信息列表集合
            if (res != null && res.getCode() == 0) {//只有200才会返回非空
                studentList = (ArrayList<Map>) res.getData();
            }
            DataResponse response = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
            if(userResponse==null){
                MessageDialog.showDialog("连接失败，请重试");
            }
            Map ownData= (Map) userResponse.getData();//inline选择器
            String userName= (String) ownData.get("userName");
            ownName.setText(userName);

            numColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
            nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            deptColumn.setCellValueFactory(new MapValueFactory<>("dept"));
            majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
            classNameColumn.setCellValueFactory(new MapValueFactory<>("className"));
            cardColumn.setCellValueFactory(new MapValueFactory<>("card"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("genderName"));
            birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
            //emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
            phoneColumn.setCellValueFactory(new MapValueFactory<>("phone"));
            addressColumn.setCellValueFactory(new MapValueFactory<>("address"));
            emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
            //ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
            ObservableList<Integer> list = tsm.getSelectedIndices();
            //list.addListener(this::onTableRowSelect);
            setTableViewData();
            genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");

            //genderComboBox.getItems().addAll(genderList);
            //birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        }
    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        studentId = null;
        numField=new TextField();
        numField.setText("");
        nameField=new TextField();
        nameField.setText("");
        deptField=new TextField();
        deptField.setText("");
        majorField=new TextField();
        majorField.setText("");
        classNameField=new TextField();
        classNameField.setText("");
        cardField=new TextField();
        cardField.setText("");
        genderComboBox=new ComboBox<>();
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick=new DatePicker();
        birthdayPick.getEditor().setText("");
        emailField=new TextField();
        emailField.setText("");
        phoneField=new TextField();
        phoneField.setText("");
        addressField=new TextField();
        addressField.setText("");
    }

    @FXML
    public void onEditButtonClick(){
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            //clearPanel();
            MessageDialog.showDialog("请选中学生后再点击我");
            return;
        }
        studentId = CommonMethod.getInteger(form,"studentId");
        initDialog();
        studentEditController.showDialog(form);
        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    private void initDialog() {
        if(stage!= null && flag==false)
            return;
        FXMLLoader fxmlLoader ;
        flag=true;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("student-edit-dialog.fxml"));
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
            studentEditController = (StudentEditController) fxmlLoader.getController();
            studentEditController.setStudentController(this);
            studentEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    protected void changeStudentInfo() {

        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        studentId = CommonMethod.getInteger(form,"studentId");
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/getStudentInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        deptField.setText(CommonMethod.getString(form, "dept"));
        majorField.setText(CommonMethod.getString(form, "major"));
        classNameField.setText(CommonMethod.getString(form, "className"));
        cardField.setText(CommonMethod.getString(form, "card"));
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, CommonMethod.getString(form, "gender")));
        birthdayPick.getEditor().setText(CommonMethod.getString(form, "birthday"));
        emailField.setText(CommonMethod.getString(form, "email"));
        phoneField.setText(CommonMethod.getString(form, "phone"));
        addressField.setText(CommonMethod.getString(form, "address"));

    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    /*public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }*/

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        String userType = (String) data.get("typeId");
        String num= (String) data.get("personNum");

        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }
        if(userType.equals("管理员")){
            String numName = numNameTextField.getText();
            DataRequest req = new DataRequest();
            req.add("numName",numName);
            //req.add("numName","弥娅");
            DataResponse res = HttpRequestUtil.request("/api/student/getStudentList",req);
            if(res != null && res.getCode()== 0) {
                studentList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }
        }else{

            DataRequest req = new DataRequest();
            req.add("numName",num);
            //req.add("numName","弥娅");
            DataResponse res = HttpRequestUtil.request("/api/student/getStudentList",req);
            if(res != null && res.getCode()== 0) {
                studentList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }
        }

    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        //clearPanel();

    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
      */
    @FXML
    protected void onDeleteButtonClick() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        studentId = CommonMethod.getInteger(form,"studentId");
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/studentDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    /*@FXML
    protected void onSaveButtonClick() {
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
           form.put("gender",genderComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("birthday",birthdayPick.getEditor().getText());
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());
        form.put("age",a)
        DataRequest req = new DataRequest();
        req.add("studentId", studentId);
        req.add("form", form);
        DataResponse res = HttpRequestUtil.request("/api/student/studentEditSave",req);
        if(res.getCode() == 0) {
            studentId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
        //clearPanel();
    }*/

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
     */
    public void doNew(){
        clearPanel();
    }
    /*public void doSave(){
        onSaveButtonClick();
    }*/
    public void doDelete(){
        onDeleteButtonClick();
    }

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */
    public void doExport(){
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    @FXML
    protected void onImportButtonClick() {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择学生数据表");
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
    public void onNewButtonClick(){
        initNewDialog();
        studentNewController.showDialog();
        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    private void initNewDialog() {
        if(stage!= null && flag==false)
            return;
        FXMLLoader fxmlLoader ;
        flag=true;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("student-new-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("学生基本信息新建对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            studentNewController = (StudentNewController) fxmlLoader.getController();
            studentNewController.setStudentController(this);
            studentNewController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doNewClose(String cmd, Map data) {

        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        String[] attributes= new String[]{"num", "name", "className", "major", "dept", "card", "birthday", "email","phone","address","gender","age"};
        String[] val=new String[attributes.length];
        for(int i=0;i< val.length;i++){
            if(CommonMethod.getString(data,attributes[i]).isEmpty()){
                MessageDialog.showDialog("信息不完整，请补全之后再提交");
                return;
            }
            val[i]=CommonMethod.getString(data,attributes[i]);
        }

        boolean flag=true;
        for(int i=0;i< val.length;i++){
            String value=val[i];
            if(i==0){
                flag=judgeNum(val[i],10);
            }else if(i==5 || i==8){
                flag=judgeNum(val[i],11);
            }else if(i==11){
                flag=judgeNum(val[i],2);
            }
            if(!flag){
                MessageDialog.showDialog("信息格式不符合要求，请修改后再提交");
                return;
            }
        }
       for(int i=0;i< val.length;i++){
           req.add(attributes[i],val[i]);
       }

        /*req.add("studentId",studentId);
        req.add("province",province);
        req.add("preInformationId",CommonMethod.getInteger(data,"preInformationId"));
        req.add("preSchool",CommonMethod.getString(data,"preSchool"));
        req.add("preScore",CommonMethod.getString(data,"preScore"));
        req.add("preRank",CommonMethod.getString(data,"preRank"));
        req.add("preHonor",CommonMethod.getString(data,"preHonor"));*/
        res = HttpRequestUtil.request("/api/student/studentNewSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
    boolean judgeNum(String card,int l){
        if(card.length()!=l){
            return false;
        }
        for(int i=0;i<card.length();i++){
            char ch=card.charAt(i);
            return ch>='0' && ch<='9';
        }
        return true;

    }

    public void doEditClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        String[] attributes= new String[]{"num", "name", "className", "major", "dept", "card", "birthday", "email","phone","address","gender","age"};
        String[] val=new String[attributes.length];
        for(int i=0;i< val.length;i++){
            if(CommonMethod.getString(data,attributes[i]).isEmpty()){
                MessageDialog.showDialog("信息不完整，请补全之后再提交");
                return;
            }
            val[i]=CommonMethod.getString(data,attributes[i]);
        }

        boolean flag=true;
        for(int i=0;i< val.length;i++){
            String value=val[i];
            if(i==0){
                flag=judgeNum(val[i],10);
            }else if(i==5 || i==8){
                flag=judgeNum(val[i],11);
            }else if(i==11){
                flag=judgeNum(val[i],2);
            }
            if(!flag){
                MessageDialog.showDialog("信息格式不符合要求，请修改后再提交");
                return;
            }
        }
        for(int i=0;i< val.length;i++){
            req.add(attributes[i],val[i]);
        }
        req.add("studentId",studentId);

        /*req.add("studentId",studentId);
        req.add("province",province);
        req.add("preInformationId",CommonMethod.getInteger(data,"preInformationId"));
        req.add("preSchool",CommonMethod.getString(data,"preSchool"));
        req.add("preScore",CommonMethod.getString(data,"preScore"));
        req.add("preRank",CommonMethod.getString(data,"preRank"));
        req.add("preHonor",CommonMethod.getString(data,"preHonor"));*/
        res = HttpRequestUtil.request("/api/student/studentEditSave",req); //从后台获取所有学生信息列表集合
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
