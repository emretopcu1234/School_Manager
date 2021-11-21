package com.emretopcu.schoolmanager.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.emretopcu.schoolmanager.commonObjectTypes.CourseFilterType;
import com.emretopcu.schoolmanager.commonObjectTypes.DepartmentType;
import com.emretopcu.schoolmanager.commonObjectTypes.PersonFilterType;
import com.emretopcu.schoolmanager.commonObjectTypes.PersonType;
import com.emretopcu.schoolmanager.viewmodel.interfaces.Interface_Dept_Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Model_Dept_Admin {

    private static Model_Dept_Admin INSTANCE;
    private Interface_Dept_Admin vmDeptAdmin;

    private FirebaseFirestore dbRef;
    private CollectionReference semestersRef;
    private CollectionReference semesterDepartmentsRef;
    private CollectionReference semesterDeptAdminsRef;
    private CollectionReference semesterLecturersRef;
    private CollectionReference semesterStudentsRef;

    private CollectionReference departmentsRef;
    private CollectionReference deptAdminsRef;
    private CollectionReference lecturersRef;
    private CollectionReference studentsRef;

    private final HashMap<String,String> departmentsInfo = new HashMap<>();
    private final HashMap<String,String[]> lecturersInfo = new HashMap<>();
    private final HashMap<String,String[]> studentsInfo = new HashMap<>();

    private final ArrayList<DepartmentType> departmentList = new ArrayList<>();
    private final ArrayList<PersonType> lecturerList = new ArrayList<>();
    private final ArrayList<PersonType> studentList = new ArrayList<>();

    private String deptAdminId;
    private String deptAdminName;
    private String deptAdminSurname;
    private String deptId;

    private Model_Dept_Admin(){
        try{
            dbRef = FirebaseFirestore.getInstance();
            semestersRef = dbRef.collection("semesters");
            semesterDepartmentsRef = dbRef.collection("semesterDepartments");
            semesterDeptAdminsRef = dbRef.collection("semesterDeptAdmins");
            semesterLecturersRef = dbRef.collection("semesterLecturers");
            semesterStudentsRef = dbRef.collection("semesterStudents");
            departmentsRef = dbRef.collection("departments");
            deptAdminsRef = dbRef.collection("deptAdmins");
            lecturersRef = dbRef.collection("lecturers");
            studentsRef = dbRef.collection("students");
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' constructor method.");
        }
    }

    public static Model_Dept_Admin getInstance(){
        try{
            if(INSTANCE == null){
                INSTANCE = new Model_Dept_Admin();
            }
            return INSTANCE;
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getInstance method.");
            return null;
        }
    }

    private void storeInitialData(){
        try{
            deptAdminsRef.document(deptAdminId).get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        Log.d("Exception","Data Load Error on storeInitialData!!!");
                        return;
                    }
                    else{
                        deptAdminName = task.getResult().getString("name");
                        deptAdminSurname = task.getResult().getString("surname");
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' deptAdminsRef.document(deptAdminId).get().addOnCompleteListener method.");
                }
            });
            departmentsRef.get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        Log.d("Exception","Data Load Error on storeInitialData!!!");
                        return;
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        departmentsInfo.put(document.getId().toUpperCase(),document.getString("name"));
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' departmentsRef.get().addOnCompleteListener method.");
                }
            });
            lecturersRef.get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        Log.d("Exception","Data Load Error on storeInitialData!!!");
                        return;
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lecturersInfo.put(document.getId(),new String[]{document.getString("name"),document.getString("surname")});
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' lecturersRef.get().addOnCompleteListener method.");
                }
            });
            studentsRef.get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        Log.d("Exception","Data Load Error on storeInitialData!!!");
                        return;
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        studentsInfo.put(document.getId(),new String[]{document.getString("name"),document.getString("surname")});
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' studentsRef.get().addOnCompleteListener method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' storeInitialData method.");
        }
    }

    public void getDepartmentIdInfo(){
        try{
            vmDeptAdmin.onDepartmentIdInfo(departmentsInfo);
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getDepartmentIdInfo method.");
        }
    }

    public void loadSemesters(){
        try{
            semestersRef.orderBy("startDate", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        vmDeptAdmin.dataLoadError();
                        return;
                    }
                    ArrayList<String> semesterList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        semesterList.add(document.getId());
                    }
                    for(int i=0;i<semesterList.size();i++){
                        semesterList.set(i, Common_Services.convertProcessedSemester(semesterList.get(i)));
                    }
                    vmDeptAdmin.onLoadSemestersResulted(semesterList);
                }
                catch (Exception e) {
                    Log.d("Exception", "Exception on Model_Dept_Admin class' semestersRef.get().addOnCompleteListener method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' loadSemesters method.");
        }
    }

    public void isSemesterActiveOrFuture(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            DocumentReference specificSemester = semestersRef.document(semester);
            specificSemester.get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        vmDeptAdmin.dataLoadError();
                        return;
                    }
                    if(Common_Services.isSemesterActive(task.getResult().getTimestamp("startDate"),task.getResult().getTimestamp("endDate"))){
                        vmDeptAdmin.onIsSemesterActiveOrFutureResulted(true);
                    }
                    else if (Common_Services.isSemesterFuture(task.getResult().getTimestamp("startDate"))){
                        vmDeptAdmin.onIsSemesterActiveOrFutureResulted(true);
                    }
                    else{
                        vmDeptAdmin.onIsSemesterActiveOrFutureResulted(false);
                    }
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' specificSemester.get().addOnCompleteListener method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' isSemesterActive method.");
        }
    }

    public void getDeptAdminInfo(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            if(deptAdminName == null || deptAdminSurname == null || deptId == null){
                if(deptAdminName == null || deptAdminSurname == null){
                    deptAdminsRef.document(deptAdminId).get().addOnCompleteListener(task -> {
                        try{
                            if(!task.isSuccessful()){
                                Log.d("Exception","Data Load Error on storeInitialData!!!");
                                return;
                            }
                            else{
                                deptAdminName = task.getResult().getString("name");
                                deptAdminSurname = task.getResult().getString("surname");
                                if(deptId == null){
                                    semesterDeptAdminsRef.whereEqualTo("semesterId",semester).whereEqualTo("deptAdminId",deptAdminId)
                                            .get().addOnCompleteListener(task1 -> {
                                                try{
                                                    if(!task1.isSuccessful()){
                                                        vmDeptAdmin.dataLoadError();
                                                        return;
                                                    }
                                                    for (QueryDocumentSnapshot document : task1.getResult()) {
                                                        deptId = document.getString("deptId");
                                                        break;
                                                    }
                                                    PersonType person = new PersonType();
                                                    person.setId(deptAdminId);
                                                    person.setName(deptAdminName);
                                                    person.setSurname(deptAdminSurname);
                                                    person.setDeptId(deptId);
                                                    vmDeptAdmin.onDeptAdminInfoResulted(person);
                                                }
                                                catch (Exception e){
                                                    Log.d("Exception", "Exception on Model_Dept_Admin class' semesterDeptAdminsRef.where.get.addOnCompleteListener method.");
                                                }
                                            });
                                }
                                else{
                                    PersonType person = new PersonType();
                                    person.setId(deptAdminId);
                                    person.setName(deptAdminName);
                                    person.setSurname(deptAdminSurname);
                                    person.setDeptId(deptId);
                                    vmDeptAdmin.onDeptAdminInfoResulted(person);
                                }
                            }
                        }
                        catch (Exception e){
                            Log.d("Exception", "Exception on Model_Dept_Admin class' deptAdminsRef.document(deptAdminId).get().addOnCompleteListener method.");
                        }
                    });
                }
                else{
                    semesterDeptAdminsRef.whereEqualTo("semesterId",semester).whereEqualTo("deptAdminId",deptAdminId)
                            .get().addOnCompleteListener(task -> {
                        try{
                            if(!task.isSuccessful()){
                                vmDeptAdmin.dataLoadError();
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deptId = document.getString("deptId");
                                break;
                            }
                            PersonType person = new PersonType();
                            person.setId(deptAdminId);
                            person.setName(deptAdminName);
                            person.setSurname(deptAdminSurname);
                            person.setDeptId(deptId);
                            vmDeptAdmin.onDeptAdminInfoResulted(person);
                        }
                        catch (Exception e){
                            Log.d("Exception", "Exception on Model_Dept_Admin class' semesterDeptAdminsRef.where.get.addOnCompleteListener method.");
                        }
                    });
                }
            }
            else{
                PersonType person = new PersonType();
                person.setId(deptAdminId);
                person.setName(deptAdminName);
                person.setSurname(deptAdminSurname);
                person.setDeptId(deptId);
                vmDeptAdmin.onDeptAdminInfoResulted(person);
            }
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getDeptAdminInfo method.");
        }
    }

    public void getCourseList(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            if(deptId == null){
                semesterDeptAdminsRef.whereEqualTo("semesterId",semester).whereEqualTo("deptAdminId",deptAdminId)
                        .get().addOnCompleteListener(task1 -> {
                    try{
                        if(!task1.isSuccessful()){
                            vmDeptAdmin.dataLoadError();
                            return;
                        }
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            deptId = document.getString("deptId");
                            break;
                        }
                        // TODO course list alınacak.
                    }
                    catch (Exception e){
                        Log.d("Exception", "Exception on Model_Dept_Admin class' semesterDeptAdminsRef.where.get.addOnCompleteListener method.");
                    }
                });
            }
            else{
                // TODO üstteki todo ile aynı şey yapılacak.
            }
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getCourseList method.");
        }
    }

    public void getLecturerList(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            semesterLecturersRef.whereEqualTo("semesterId",semester).orderBy("lecturerId", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        vmDeptAdmin.dataLoadError();
                        return;
                    }
                    lecturerList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("deptId").equals(deptId)){
                            String lecturerId = document.get("lecturerId").toString();
                            PersonType person = new PersonType();
                            person.setId(lecturerId);
                            person.setName(lecturersInfo.get(lecturerId)[0]);
                            person.setSurname(lecturersInfo.get(lecturerId)[1]);
                            person.setDeptId(document.getString("deptId").toUpperCase());
                            lecturerList.add(person);
                        }
                    }
                    vmDeptAdmin.onGetLecturerListResulted(lecturerList);
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' semesterLecturersRef.get method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getLecturerList method.");
        }
    }

    public void getStudentList(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            semesterStudentsRef.whereEqualTo("semesterId",semester).orderBy("studentId", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        vmDeptAdmin.dataLoadError();
                        return;
                    }
                    studentList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String studentId = document.get("studentId").toString();
                        PersonType person = new PersonType();
                        person.setId(studentId);
                        person.setName(studentsInfo.get(studentId)[0]);
                        person.setSurname(studentsInfo.get(studentId)[1]);
                        person.setDeptId(document.getString("deptId").toUpperCase());
                        studentList.add(person);
                    }
                    vmDeptAdmin.onGetStudentListResulted(studentList);
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' semesterStudentsRef.get method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getStudentList method.");
        }
    }

    public void getDepartmentList(String unprocessedSemester){
        try{
            String semester = Common_Services.convertUnprocessedSemester(unprocessedSemester);
            semesterDepartmentsRef.whereEqualTo("semesterId",semester).orderBy("deptName", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
                try{
                    if(!task.isSuccessful()){
                        vmDeptAdmin.dataLoadError();
                        return;
                    }
                    departmentList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DepartmentType department = new DepartmentType();
                        department.setDeptName(document.getString("deptName"));
                        department.setDeptId(document.getString("deptId").toUpperCase());
                        departmentList.add(department);
                    }
                    vmDeptAdmin.onGetDepartmentListResulted(departmentList);
                }
                catch (Exception e){
                    Log.d("Exception", "Exception on Model_Dept_Admin class' semesterDepartmentsRef.get method.");
                }
            });
        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getDepartmentList method.");
        }
    }

    public void getFilteredCourseList(CourseFilterType courseFilter){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getFilteredCourseList method.");
        }
    }

    public void getFilteredLecturerList(PersonFilterType personFilter){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getFilteredLecturerList method.");
        }
    }

    public void getFilteredStudentList(PersonFilterType personFilter){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' getFilteredStudentList method.");
        }
    }

    public void addCourse(String courseId, String unprocessedCourseName, String sections){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' addCourse method.");
        }
    }

    public void editCourse(String courseId, String unprocessedCourseName, String sections){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' editCourse method.");
        }
    }

    public void deleteCourses(ArrayList<String> idList){
        try{

        }
        catch (Exception e){
            Log.d("Exception", "Exception on Model_Dept_Admin class' deleteCourses method.");
        }
    }

    public void setVmDeptAdmin(Interface_Dept_Admin vmDeptAdmin) {
        this.vmDeptAdmin = vmDeptAdmin;
    }

    protected void setDeptAdminId(String deptAdminId) {
        this.deptAdminId = deptAdminId;
        storeInitialData();
    }
}