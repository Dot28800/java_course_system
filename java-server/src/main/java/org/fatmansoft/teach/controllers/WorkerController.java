package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Worker;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.service.StudentService;
import org.fatmansoft.teach.service.SystemService;
import org.fatmansoft.teach.service.WorkerService;
import org.fatmansoft.teach.util.JsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/worker")

public class WorkerController {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private WorkerRepository workerRepository;  //学生数据操作自动注入
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
    private WorkerService workerService;
    @Autowired
    private SystemService systemService;
    @PostMapping("/getWorkerList")
    public DataResponse getWorkerList(@Valid @RequestBody DataRequest dataRequest){
        String numName = dataRequest.getString("numName");
        List dataList = getStudentMapList(numName);

        return new DataResponse(0,dataList,null);
        /*String numName= dataRequest.getString("numName");
        if(numName==null)
            numName="";
        List<Worker> sList=workerRepository.findWorkerListByNumName(numName);
        return JsonConvertUtil.getDataListJson(sList);*/
    }
    public List getStudentMapList(String numName){
        List dataList=new ArrayList();
        List<Worker> sList=workerRepository.findWorkerListByNumName(numName);
        if(sList==null || sList.size()==0)
            return dataList;
        for (int i = 0; i < sList.size(); i++) {

            dataList.add((workerService.getMapFromWorker(sList.get(i))));
        }
        return dataList;

    }

}
