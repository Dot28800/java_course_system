package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.PreInformation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.util.ComDataUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class PreInformationService {
    public Map getMapFromPreInformation(PreInformation s) {
        Map m = new HashMap();
        Student student;
        Person p;
        if(s == null)
            return m;
        m.put("preInformationId",s.getPreInformationId());
        m.put("province",s.getSourcePlace());
        m.put("preSchool",s.getPreSchool());
        m.put("preScore",s.getPreScore());
        m.put("preRank",s.getPreRank());
        m.put("preHonor",s.getPreHonor());
        p=s.getStudent().getPerson();
        if(p == null)
            return m;
        m.put("studentId", s.getStudent().getStudentId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("gender",p.getGender());
        m.put("age", p.getAge());

        return m;
    }
}
