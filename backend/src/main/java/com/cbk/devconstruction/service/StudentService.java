package com.cbk.devconstruction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.repository.StudentRepository;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class StudentService {
	public static final long serialVersionUID=1L;
	
	@Autowired
	StudentRepository studentRepository;
	
	public Student checkValidTStudent(Long studentId) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(studentId, studentRepository);
	}

}
