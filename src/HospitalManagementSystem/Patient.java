package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name : ");
        String name = scanner.next();
        System.out.print("Enter Patient Age : ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender : ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(patient_name, patient_age, pateint_gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0 ){
                System.out.println("Patient Added Successfully!!!");
            } else {
                System.out.println("Failed To Add Patient!!!");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query ="SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("patient_id");
                String name = resultSet.getString("patient_name");
                int age = resultSet.getInt("patient_age");
                String gender = resultSet.getString("pateint_gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");

            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE patient_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
