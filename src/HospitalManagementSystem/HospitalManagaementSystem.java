package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagaementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Ashish@28";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            Scanner scanner = new Scanner(System.in);
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice : ");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thank You for using Hospital Management System!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                        break;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter Patient Id : ");
        int pt_id = scanner.nextInt();
        System.out.println("Enter Doctor Id : ");
        int dr_id = scanner.nextInt();
        System.out.println("Enter Appointment Data (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(pt_id) && doctor.getDoctorById(dr_id)){
            if(checkDoctorAvailability(dr_id, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patients_id, doctors_id, appointment_date) VALUES(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,pt_id);
                    preparedStatement.setInt(2,dr_id);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0){
                        System.out.println("Appointment Booked Successfully!!!");
                    } else {
                        System.out.println("Failed to book Appointment!!!");
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date!!! ");
            }
        } else{
            System.out.println("Either Doctor or Patient Doesn't Exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int dr_id, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,dr_id);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                } else {
                    return false;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
