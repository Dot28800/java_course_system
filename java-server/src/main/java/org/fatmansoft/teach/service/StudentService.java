package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.PreInformation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.PreRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private PreRepository preRepository;
    public Map getMapFromStudent(Student s) {
        Map m = new HashMap();
        Person p;
        if(s == null)
            return m;
        m.put("studentId", s.getStudentId());
        m.put("major",s.getMajor());
        m.put("className",s.getClassName());
        p = s.getPerson();
        if(p == null)
            return m;
        Optional<PreInformation> pre=preRepository.findByStudentStudentId(s.getStudentId());
        if(pre.isPresent()){
            m.put("province",pre.get().getSourcePlace());
        }else{
            m.put("province","未知");
        }
        m.put("personId", p.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("dept",p.getDept());
        m.put("card",p.getCard());
        m.put("genderName",p.getGender());
        /*String gender = p.getGender();
        m.put("gender",gender);*/
        //m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", p.getBirthday());  //时间格式转换字符串
        m.put("email",p.getEmail());
        m.put("phone",p.getPhone());
        m.put("address",p.getAddress());
        m.put("introduce",p.getIntroduce());
        m.put("age",p.getAge());
        m.put("politics","共青团员");
        m.put("job","体育委员");
        return m;
    }

}
