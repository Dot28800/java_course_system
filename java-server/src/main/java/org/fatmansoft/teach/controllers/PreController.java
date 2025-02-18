package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.PreInformation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.service.PreInformationService;
import org.fatmansoft.teach.service.StudentService;
import org.fatmansoft.teach.service.SystemService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pre")
public class PreController {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private ScoreRepository scoreRepository;  //成绩数据操作自动注入
    @Autowired
    private FeeRepository feeRepository;  //消费数据操作自动注入
    @Autowired
    private BaseService baseService;   //基本数据处理数据操作自动注入
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private PreRepository preRepository;
    @Autowired
    private PreInformationService preInformationService;
    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem( s.getStudentId(),s.getStudentId()+"", s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }
    @PostMapping("/getPreListByNumName")
    public DataResponse getPreListByNumName(@Valid@RequestBody DataRequest dataRequest){
        String numName = dataRequest.getString("numName");
        List dataList = new ArrayList<>();
        List<PreInformation> sList = preRepository.findPreListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(preInformationService.getMapFromPreInformation(sList.get(i)));
        }

        //return new DataResponse(0,dataList,null);
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/getNumByProvince")
    public DataResponse getNumByProvince(@Valid@RequestBody DataRequest dataRequest){
        Map<String,Integer> m= dataRequest.getMap("provinceMap");
        for(String key: m.keySet()){
            List<PreInformation> numList=preRepository.findByProvince(key);
            m.put(key, numList.size());
        }


        return CommonMethod.getReturnData(m);

    }
    @PostMapping("/getPreListByProvince")
    public DataResponse getPreListByProvince(@Valid@RequestBody DataRequest dataRequest){
        String province= dataRequest.getString("province");
        List dataList = new ArrayList<>();
        List<PreInformation> sList=preRepository.findBySourcePlace(province);
        if (sList == null || sList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(preInformationService.getMapFromPreInformation(sList.get(i)));
        }

        //return new DataResponse(0,dataList,null);
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/getComprehensiveList")
    public DataResponse ComprehensiveSearch(@Valid@RequestBody DataRequest dataRequest){
        String numName = dataRequest.getString("numName");
        String province= dataRequest.getString("province");
        List dataList = new ArrayList<>();
        List<PreInformation> sList=preRepository.comprehensiveSearch(numName,province);
        if (sList == null || sList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(preInformationService.getMapFromPreInformation(sList.get(i)));
        }

        //return new DataResponse(0,dataList,null);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/preEditSave")
    public DataResponse preEditSave(@Valid@RequestBody DataRequest dataRequest){
            Integer studentId= dataRequest.getInteger("studentId");
            Integer preInformationId=dataRequest.getInteger("preInformationId");
            String province= dataRequest.getString("province");
            String preSchool= dataRequest.getString("preSchool");
            String preScore= dataRequest.getString("preScore");
            String preRank= dataRequest.getString("preRank");
            String preHonor= dataRequest.getString("preHonor");
        Optional<PreInformation> op;
        PreInformation s=null;
        if(preInformationId!=null){
            op=preRepository.findById(preInformationId);
            if(op.isPresent()){
                s=op.get();
            }else {
                return CommonMethod.getReturnMessageError("找不到相应的记录");

            }
        }else {
            return CommonMethod.getReturnMessageError("传来的入学前信息序号为空，请检查前端是否成功获取！");
        }
        s.setStudent(studentRepository.findById(studentId).get());
        s.setSourcePlace(province);
        s.setPreSchool(preSchool);
        s.setPreScore(preScore);
        s.setPreRank(preRank);
        s.setPreHonor(preHonor);
        preRepository.save(s);
        return CommonMethod.getReturnMessageOK();

    }
    @PostMapping("/delete")
    public DataResponse delete(@Valid@RequestBody DataRequest dataRequest){
        Integer preInformationId=dataRequest.getInteger("preInformationId");
        Optional<PreInformation> op;
        PreInformation c=null;
        if(preInformationId!=null){
            op=preRepository.findById(preInformationId);
            if(op.isPresent()){
                c=op.get();
                preRepository.delete(c);
                return CommonMethod.getReturnMessageOK();
            }else{
                return CommonMethod.getReturnMessageError("找不到要删除的记录！");
            }
        }else {
            return CommonMethod.getReturnMessageError("删除失败，请重试");
        }

    }
    @PostMapping("/preNewSave")
    public DataResponse preNewSave(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        //Integer preInformationId=dataRequest.getInteger("preInformationId");
        String province= dataRequest.getString("province");
        String preSchool= dataRequest.getString("preSchool");
        String preScore= dataRequest.getString("preScore");
        String preRank= dataRequest.getString("preRank");
        String preHonor= dataRequest.getString("preHonor");
        boolean flag= !preSchool.isEmpty() && !preScore.isEmpty() && !preRank.isEmpty() && !preHonor.isEmpty() && !province.isEmpty() && !(studentId ==null);
        if (!flag){
            return CommonMethod.getReturnMessageError("信息不完整，无法创建，请补充完整后再提交");
        }
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到此学生，无法创建");
        }

        Optional<PreInformation> infoOp=preRepository.findByStudentStudentId(studentId);
        if(infoOp.isPresent()){
            return CommonMethod.getReturnMessageError("该学生入学前信息已经存在，请勿重复添加");
        }
        PreInformation s=new PreInformation();
        s.setStudent(op.get());
        s.setSourcePlace(province);
        s.setPreSchool(preSchool);
        s.setPreScore(preScore);
        s.setPreRank(preRank);
        s.setPreHonor(preHonor);
        preRepository.save(s);
        return CommonMethod.getReturnMessageOK();

    }
    public void localStudentDelete(Integer studentId){
        Optional<PreInformation> op=preRepository.findByStudentStudentId(studentId);
        op.ifPresent(preInformation -> preRepository.delete(preInformation));
    }

    @PostMapping("/getRankData")
    public DataResponse getRankData(@Valid@RequestBody DataRequest dataRequest){
        List<PreInformation> preInformationList=preRepository.findAll();
        Map<String,Integer> data=new HashMap<>();
        data.put("<3000",0);
        data.put("3000~4500",0);
        data.put("4500~5500",0);
        data.put("5500~6500",0);
        data.put("6500~7500",0);
        data.put("7500~9000",0);
        data.put(">9000",0);
        for(PreInformation p:preInformationList){
            int preRank= Integer.parseInt(p.getPreRank());
            if(preRank<=3000){
                data.put("<3000",1+data.get("<3000"));
            }else if(preRank<=4500){
                data.put("3000~4500",1+data.get("3000~4500"));
            }else if(preRank<=5500){
                data.put("4500~5500",1+data.get("4500~5500"));
            }
            else if(preRank<=6500){
                data.put("5500~6500",1+data.get("5500~6500"));
            }else if(preRank<=7500){
                data.put("6500~7500",1+data.get("6500~7500"));
            }else if(preRank<=9000){
                data.put("7500~9000",1+data.get("7500~9000"));
            }else{
                data.put(">9000",1+ data.get(">9000"));
            }
        }
        return CommonMethod.getReturnData(data);
    }

}

