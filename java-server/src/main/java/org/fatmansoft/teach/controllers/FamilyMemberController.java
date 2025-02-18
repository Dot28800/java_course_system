package org.fatmansoft.teach.controllers;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.FamilyMemberRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/family_member")
public class FamilyMemberController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    FamilyMemberRepository familyMemberRepository;
    @PostMapping("/getFamilyMemberList")
    //@PreAuthorize(" hasRole('ADMIN') or  hasRole('STUDENT')")
    public DataResponse getFamilyMemberList(@Valid @RequestBody DataRequest dataRequest) {
        String numIdName = dataRequest.getString("numIdName");
        List<FamilyMember> fList = familyMemberRepository.findByNumIdName(numIdName);
        List dataList = new ArrayList();
        Map m;
        if (fList != null) {
            for (FamilyMember f : fList) {
                m = new HashMap();
                m.put("familyMemberId", f.getMemberId());
                m.put("studentId", f.getStudent().getStudentId());
                m.put("num",f.getStudent().getPerson().getNum());
                m.put("studentName",f.getStudent().getPerson().getName());
                m.put("relationship", f.getRelation());
                m.put("name", f.getName());
                m.put("gender", f.getGender());
                m.put("age", f.getAge());
                m.put("job",f.getJob());
                //m.put("unit", f.getUnit());
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/memberEditSave")
    //@PreAuthorize(" hasRole('ADMIN') or  hasRole('STUDENT')")
    public DataResponse familyMemberSave(@Valid @RequestBody DataRequest dataRequest) {
        Map data=dataRequest.getData();
        DataResponse res;
        String type=CommonMethod.getString(data,"type");
        if(type.equals("new")){
            res=newMember(data);
        }else{
            res=editMember(data);
        }
        return res;

    }

    private DataResponse editMember(Map data){
        String numName=CommonMethod.getString(data,"numName");
        String name=CommonMethod.getString(data,"name");
        Optional<FamilyMember> op=familyMemberRepository.findByStudentNumNameAndName(numName,name);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("该对象不存在，无法修改 ");
        }
        FamilyMember familyMember=op.get();
        Optional<Student> sop=studentRepository.findByStudentNumName(numName);
        if(!sop.isPresent()){
            return CommonMethod.getReturnMessageError("对应学生不存在");
        }
        Student s=sop.get();
        familyMember.setStudent(s);
        familyMember.setGender(CommonMethod.getString(data,"gender"));
        familyMember.setName(name);
        familyMember.setAge(CommonMethod.getString(data,"age"));
        familyMember.setRelation(CommonMethod.getString(data,"relation"));
        familyMember.setJob(CommonMethod.getString(data,"job"));
        familyMemberRepository.save(familyMember);
        return CommonMethod.getReturnMessageOK();

    }
    private DataResponse newMember(Map data){
        String numName=CommonMethod.getString(data,"numName");
        String name=CommonMethod.getString(data,"name");
        Optional<FamilyMember> op=familyMemberRepository.findByStudentNumNameAndName(numName,name);
        if(op.isPresent()){
            return CommonMethod.getReturnMessageError("已经有这个对象，请勿重新创建 ");
        }
        FamilyMember familyMember=new FamilyMember();
        Optional<Student> sop=studentRepository.findByStudentNumName(numName);
        if(!sop.isPresent()){
            return CommonMethod.getReturnMessageError("对应学生不存在");
        }
        Student s=sop.get();

        familyMember.setStudent(s);
        familyMember.setGender(CommonMethod.getString(data,"gender"));
        familyMember.setName(name);
        familyMember.setAge(CommonMethod.getString(data,"age"));
        familyMember.setRelation(CommonMethod.getString(data,"relation"));
        familyMember.setJob(CommonMethod.getString(data,"job"));
        familyMemberRepository.save(familyMember);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/memberDelete")
    //@PreAuthorize(" hasRole('ADMIN') or  hasRole('STUDENT')")
    public DataResponse familyMemberDelete(@Valid @RequestBody DataRequest dataRequest) {
        //Integer memberId = Integer.valueOf(dataRequest.getString("memberId"));
        Map data=dataRequest.getData();

        String studentName= (String) data.get("studentName");
        String name= dataRequest.getString("name");
        Optional<FamilyMember> op;
        op = familyMemberRepository.findByStudentNumAndMemberName(studentName,name);
        if(op.isPresent()) {
            familyMemberRepository.delete(op.get());
        }else{
            return CommonMethod.getReturnMessageError("没有这一对象");
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getMemberInfo")
    public DataResponse getMemberInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer memberId = dataRequest.getInteger("familyMemberId");
        FamilyMember f = null;
        Optional<FamilyMember> op;
        if (memberId!= null) {
            op = familyMemberRepository.findById(memberId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                f = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromMember(f)); //这里回传包含学生信息的Map对象
    }

    public Map getMapFromMember(FamilyMember f){
        Map m = new HashMap();
        m.put("familyMemberId",f.getMemberId());
        m.put("studentId",f.getStudent().getStudentId());
        m.put("name",f.getName());
        m.put("relationship",f.getRelation());
        m.put("gender",f.getGender());
        String gender=f.getGender();
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender));
        m.put("age",f.getAge());
        return m;
    }

    @PostMapping("/memberNew")
    public DataResponse memberNew(@Valid@RequestBody DataRequest dataRequest){
        Integer memberId=dataRequest.getInteger("memberId");
        Optional<FamilyMember> op=familyMemberRepository.findById(memberId);
        if(op.isPresent()){
            return CommonMethod.getReturnMessageError("该记录已经存在，请勿重复新增");
        }
        FamilyMember f=new FamilyMember();
        Integer studentId=dataRequest.getInteger("studentId");
        f.setStudent(studentRepository.findById(studentId).get());
        f.setName(dataRequest.getString("name"));
        f.setAge(dataRequest.getString("age"));
        f.setGender(dataRequest.getString("gender"));
        f.setRelation(dataRequest.getString("relation"));
        familyMemberRepository.save(f);
        return CommonMethod.getReturnMessageOK();
    }
    public void localStudentDelete(Integer studentId){
        List<FamilyMember> studentList=familyMemberRepository.findByStudent(studentId);
        familyMemberRepository.deleteAll(studentList);
    }
}
