package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.MyTreeNode;
import com.teach.javafx.request.OptionItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;

public class ResumeController extends ToolController {
    private Stage stage=new Stage();
    @FXML
    private ImageView photoImageView;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private ObservableList<Map> honorOb=FXCollections.observableArrayList();
    @FXML
    private HTMLEditor introduceHtml; //个人简历HTML编辑器
    @FXML
    private Button photoButton;  //照片显示和上传按钮
    @FXML
    private Label age;  //学号标签
    @FXML
    private Label name;//姓名标签
    @FXML
    private Label dept; //学院标签
    @FXML
    private Label major; //专业标签
    @FXML
    private Label province;
    @FXML
    private Label politics; //班级标签
    @FXML
    private Label card;  //证件号码标签
    @FXML
    private Label gender; //性别标签
    @FXML
    private Label birthday; //出生日期标签
    @FXML
    private Label email; //邮箱标签
    @FXML
    private Label phone; //电话标签
    @FXML
    private Label address; //地址标签
    @FXML
    private Label job;
    @FXML
    private Label avgMark;
    @FXML
    private Label avgGPA;
    @FXML
    private Label totalRank;
    @FXML
    private WebEngine webEngine;
    @FXML
    private TableView<Map> scoreTable;  //成绩表TableView
    @FXML
    private TableView<Map> honorTable;
    @FXML
    private TableColumn<Map,String> courseNumColumn;  //课程号列
    @FXML
    private TableColumn<Map,String> courseNameColumn; //课程名列
    @FXML
    private TableColumn<Map,String> GPAColumn; //学分列
    @FXML
    private TableColumn<Map,String> markColumn; //成绩列
    @FXML
    private TableColumn<Map,String> rankingColumn; //排名列
    @FXML
    private TableColumn<Map,String> honorTitle;
    @FXML
    private TableColumn<Map,String> honorTime;
    @FXML
    private TableColumn<Map,String> honorHost;
    @FXML
    private TableColumn<Map,String> honorLevel;

    @FXML
    private BarChart<String,Number> barChart;  //消费直方图控件
    @FXML
    private StackedBarChart<String,Number> honorBarChart;
    @FXML
    private PieChart pieChart;   //成绩分布饼图控件
    private Integer studentId = null;  //学生主键
    private Integer personId = null;  //学生关联人员主键
    @FXML
    private StackedBarChart<String, Number> stackedBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private WebView webView;


    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        photoButton.setGraphic(photoImageView);
        //photoButton.setGraphic(photoImageView);
        courseNumColumn.setCellValueFactory(new MapValueFactory("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));

        GPAColumn.setCellValueFactory(new MapValueFactory<>("GPA"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        rankingColumn.setCellValueFactory(new MapValueFactory<>("ranking"));
        honorTitle.setCellValueFactory(new MapValueFactory<>("title"));
        honorTime.setCellValueFactory(new MapValueFactory<>("time"));
        honorHost.setCellValueFactory(new MapValueFactory<>("host"));
        honorLevel.setCellValueFactory(new MapValueFactory<>("level"));
        URL url = getClass().getResource("FirstMeeting,html");
        if (url != null) {
            webView.getEngine().load(url.toExternalForm());
        } else {
            // 如果找不到文件，可以在这里处理错误
            System.err.println("Cannot find the HTML file.");
        }

        getIntroduceData();

    }
    public void initInformation(){
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        photoButton.setGraphic(photoImageView);
    }

    public void initMark(){

    }
    public void initHonorTable(){

    }

    /**
     * getIntroduceData 从后天获取当前学生的所有信息，不传送的面板各个组件中
     */
    public void getIntroduceData(){
        DataRequest req = new DataRequest();
        DataResponse res;

        res = HttpRequestUtil.request("/api/resume/getStudentIntroduceData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        studentId = CommonMethod.getInteger(info,"studentId");
        personId = CommonMethod.getInteger(info,"personId");
        age.setText(CommonMethod.getString(info,"age"));
        name.setText(CommonMethod.getString(info,"name"));
        //dept.setText(CommonMethod.getString(info,"dept"));
        major.setText(CommonMethod.getString(info,"major"));
        politics.setText(CommonMethod.getString(info,"politics"));
        card.setText(CommonMethod.getString(info,"card"));
        gender.setText(CommonMethod.getString(info,"genderName"));
        birthday.setText(CommonMethod.getString(info,"birthday"));
        email.setText(CommonMethod.getString(info,"email"));
        phone.setText(CommonMethod.getString(info,"phone"));
        address.setText(CommonMethod.getString(info,"address"));
        job.setText(CommonMethod.getString(info,"job"));
        avgMark.setText((String) data.get("mark"));
        avgGPA.setText((String) data.get("GPA"));
        province.setText(CommonMethod.getString(info,"province"));
        totalRank.setText((String) data.get("rank"));
        introduceHtml.setHtmlText(CommonMethod.getString(info,"introduce"));
        List<Map> markList = (List)data.get("markList");
        List<Map>  honorList = (List)data.get("honorList");
        List<Map> levelList=(List)data.get("level");
        for (Map m: markList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        scoreTable.setItems(observableList);  // 成绩表数据显示
        for(Map m:honorList){
            honorOb.addAll(FXCollections.observableArrayList(m));
        }
        honorTable.setItems(honorOb);
        /*XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("各等级获奖柱状图");
        String[] levelList={"院级","校级","省级","国家级"};
        xAxis.setLabel("等级");
        yAxis.setLabel("数量");
        for(String s:levelList){
            series1.getData().add(new XYChart.Data<>(s,(Number) data.get(s)));
        }
        stackedBarChart.getData().add(series1);*/
        createBarChart(data);
        createPieChart(levelList);
        displayPhoto();

    }
    void createBarChart(Map data){
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("各等级获奖柱状图");
        String[] levelList={"院级","校级","省级","国家级"};
        xAxis.setLabel("等级");
        yAxis.setLabel("数量");
        for(String s:levelList){
            series1.getData().add(new XYChart.Data<>(s,(Number) data.get(s)));
        }
        stackedBarChart.getData().add(series1);
    }
    void createPieChart(List<Map> levels){

        pieChart.setData(createPieChartData(levels));
        pieChart.setClockwise(false);
        pieChart.setLabelLineLength(15);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);
        pieChart.setTitle("各科成绩分等级统计图");
    }
    private ObservableList<PieChart.Data> createPieChartData(List<Map> levels) {
        List<PieChart.Data> mapList=new ArrayList<>();
        Map m=new HashMap<>();
        String level;
        Double count;
        for(int i=0;i<levels.size();i++){
            level= (String) levels.get(i).get("name");
            count= (Double) levels.get(i).get("value");

            if(count>0){
                PieChart.Data data=new PieChart.Data(level, count);
                mapList.add(data);
            }

        }

        ObservableList<PieChart.Data> list = createPieChartDataFromExistingList(mapList);
        pieChart.setData(list);
        return list;
    }
    private ObservableList<PieChart.Data> createPieChartDataFromExistingList(List<PieChart.Data> existingDataList) {
        return FXCollections.observableArrayList(existingDataList);
    }

    public void displayPhoto(){
        DataRequest req = new DataRequest();
        req.add("fileName", "photo/" + personId + ".jpg");  //个人照片显示
        byte[] bytes = HttpRequestUtil.requestByteData("/api/base/getFileByteData", req);
        if (bytes != null) {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            Image img = new Image(in);
            photoImageView.setImage(img);
        }

    }

    @FXML
    public void gotoWeb(){
        //MessageDialog.showDialog("叙事");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Update the stage title when a new web page title is available
        webEngine.titleProperty().addListener((ObservableValue<? extends String> p,
                                               String oldTitle, String newTitle) -> {
            //Stage stage=new Stage();
            stage.setTitle(newTitle);
        });
        webEngine.load("http://www.google.com");
        VBox root = new VBox(webView);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
            /*String url="https://yiyan.baidu.com/";
            WebEngine webEngine=new WebEngine();
            webEngine.load(url);*/


    }
    /**
     * 点击保存按钮 执行onSubmitButtonClick 调用doSave 实现个人简历保存
     */
    @FXML
    public void onSubmitButtonClick(){
        doSave();
    }

    @FXML
    public void refresh(){
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        photoButton.setGraphic(photoImageView);
        //photoButton.setGraphic(photoImageView);
        /*courseNumColumn.setCellValueFactory(new MapValueFactory("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));

        GPAColumn.setCellValueFactory(new MapValueFactory<>("GPA"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        rankingColumn.setCellValueFactory(new MapValueFactory<>("ranking"));
        honorTitle.setCellValueFactory(new MapValueFactory<>("title"));
        honorTime.setCellValueFactory(new MapValueFactory<>("time"));
        honorHost.setCellValueFactory(new MapValueFactory<>("host"));
        honorLevel.setCellValueFactory(new MapValueFactory<>("level"));*/
        URL url = getClass().getResource("FirstMeeting,html");
        if (url != null) {
            webView.getEngine().load(url.toExternalForm());
        } else {
            // 如果找不到文件，可以在这里处理错误
            System.err.println("Cannot find the HTML file.");
        }
        DataRequest req = new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/resume/getStudentIntroduceData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        studentId = CommonMethod.getInteger(info,"studentId");
        personId = CommonMethod.getInteger(info,"personId");
        age.setText(CommonMethod.getString(info,"age"));
        name.setText(CommonMethod.getString(info,"name"));
        //dept.setText(CommonMethod.getString(info,"dept"));
        major.setText(CommonMethod.getString(info,"major"));
        politics.setText(CommonMethod.getString(info,"politics"));
        card.setText(CommonMethod.getString(info,"card"));
        gender.setText(CommonMethod.getString(info,"genderName"));
        birthday.setText(CommonMethod.getString(info,"birthday"));
        email.setText(CommonMethod.getString(info,"email"));
        phone.setText(CommonMethod.getString(info,"phone"));
        address.setText(CommonMethod.getString(info,"address"));
        job.setText(CommonMethod.getString(info,"job"));
        avgMark.setText((String) data.get("mark"));
        avgGPA.setText((String) data.get("GPA"));
        province.setText(CommonMethod.getString(info,"province"));
        totalRank.setText((String) data.get("rank"));
        introduceHtml.setHtmlText(CommonMethod.getString(info,"introduce"));
        List<Map> markList = (List)data.get("markList");
        List<Map>  honorList = (List)data.get("honorList");
        for (Map m: markList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        scoreTable.setItems(observableList);  // 成绩表数据显示
        for(Map m:honorList){
            honorOb.addAll(FXCollections.observableArrayList(m));
        }
        honorTable.setItems(honorOb);
    }

    /**
     * 显示生成的个人简历的PDF， 可以直接将PDF数据存入本地文件参见StudentController 中的doExpert 方法中的本地文件保存
     * 后台修改完善扩展PDF内容的生成方法，可以按照HTML语法生成PDF要展示的数据内容
     */
    @FXML
    public void onIntroduceDownloadClick() throws IOException {
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentIntroducePdf", req);
        DataResponse studentNum = HttpRequestUtil.request("/api/student/getStudentInfo",req);

        String fileName = CommonMethod.getString((Map) studentNum.getData(),"num") + ".pdf";
        File testFile = new File("C:" + File.separator + "teach" + File.separator + "run" + File.separator + fileName);
        File fileParent = testFile.getParentFile();//返回的是File类型,可以调用exsit()等方法
        String fileParentPath = testFile.getParent();//返回的是String类型
        if (!fileParent.exists()) {
            fileParent.mkdirs();// 能创建多级目录
        }
        if (!testFile.exists())
            testFile.createNewFile();//有路径才能创建文件

        try (FileOutputStream fileOutputStream = new FileOutputStream("/teach/run/"+fileName)) {
            fileOutputStream.write(bytes);
        }catch (Exception e){
            throw new FileNotFoundException();
        }


        if (bytes != null) {
            try {
                MessageDialog.pdfViewerDialog(bytes);
                MessageDialog.showDialog("文件已保存为\"teach/run/"+fileName+"\"");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        /*DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentIntroducePdf", req);
        if (bytes != null) {
            try {
                MessageDialog.pdfViewerDialog(bytes);
            }catch(Exception e){
                e.printStackTrace();
            }
        }*/
    }

    /**
     *  点击图片位置，可以重现上传图片，可在本地目录选择要上传的张片进行上传
     */
    @FXML
    public void onPhotoButtonClick(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("图片上传");
//        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG 文件", "*.jpg"));
        File file = fileDialog.showOpenDialog(null);
        if(file == null)
            return;
        DataResponse res =HttpRequestUtil.uploadFile("/api/base/uploadPhoto",file.getPath(),"photo/" + personId + ".jpg");
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
            displayPhoto();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 保存个人简介数据到数据库里
     */

    public void doSave(){
        String introduce = introduceHtml.getHtmlText();
        DataRequest req = new DataRequest();
        req.add("studentId",studentId);
        req.add("introduce",introduce);
        DataResponse res = HttpRequestUtil.request("/api/student/saveStudentIntroduce", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("提交成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * 数据导入示例，点击编辑菜单中的导入菜单执行该方法， doImport 重写了 ToolController 中的doImport
     * 该方法从本地选择Excl文件，数据上传到后台，后台从Excl格式的数据流中解析出日期和金额添加更新学生的消费记录
     */
    public void doImport(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择消费数据表");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "studentId="+studentId;
        DataResponse res =HttpRequestUtil.importData("/api/student/importFeeData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
}
